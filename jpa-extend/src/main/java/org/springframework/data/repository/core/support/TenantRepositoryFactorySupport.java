/*
 * Copyright 2008-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.repository.core.support;

import learning.jpaExtend.extend.Reflections;
import learning.jpaExtend.extend.TenantUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.util.ClassUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory bean to create instances of a given repository interface. Creates a proxy implementing the configured
 * repository interface and apply an advice handing the control to the {@code QueryExecuterMethodInterceptor}. Query
 * detection strategy can be configured by setting {@link QueryLookupStrategy.Key}.
 *
 * @author Oliver Gierke
 */
public abstract class TenantRepositoryFactorySupport extends RepositoryFactorySupport {

    private static final boolean IS_JAVA_8 = org.springframework.util.ClassUtils.isPresent("java.util.Optional",
            TenantRepositoryFactorySupport.class.getClassLoader());
    private static final Class<?> TRANSACTION_PROXY_TYPE = getTransactionProxyType();

    private static Class<?> getTransactionProxyType() {
        try {
            return org.springframework.util.ClassUtils
                    .forName("org.springframework.transaction.interceptor.TransactionalProxy", null);
        } catch (ClassNotFoundException o_O) {
            return null;
        }
    }

    public BeanFactory getBeanFactory(){
        return (BeanFactory)Reflections.getField(RepositoryFactorySupport.class,this,"beanFactory");
    }

    public List<RepositoryProxyPostProcessor> getPostProcessors(){
        return (List)Reflections.getField(RepositoryFactorySupport.class,this,"postProcessors");
    }

    public ClassLoader getClassLoader(){
        return (ClassLoader)Reflections.getField(RepositoryFactorySupport.class,this,"classLoader");
    }

    public EvaluationContextProvider getEvaluationContextProvider(){
        return (EvaluationContextProvider)Reflections.getField(RepositoryFactorySupport.class,this,"evaluationContextProvider");
    }

    public  QueryLookupStrategy.Key getQueryLookupStrategyKey(){
        return (QueryLookupStrategy.Key)Reflections.getField(RepositoryFactorySupport.class,this,"queryLookupStrategyKey");
    }

    public  NamedQueries getNamedQueries(){
        return (NamedQueries)Reflections.getField(RepositoryFactorySupport.class,this,"namedQueries");
    }

    public List<QueryCreationListener<?>> getQueryPostProcessors(){
        return (List)Reflections.getField(RepositoryFactorySupport.class,this,"queryPostProcessors");
    }



    @Override
    public <T> T getRepository(Class<T> repositoryInterface, Object customImplementation) {

        RepositoryMetadata metadata = getRepositoryMetadata(repositoryInterface);
        Class<?> customImplementationClass = null == customImplementation ? null : customImplementation.getClass();
        RepositoryInformation information = getRepositoryInformation(metadata, customImplementationClass);

        validate(information,customImplementation);

        Object target = getTargetRepository(information);

        // Create proxy
        ProxyFactory result = new ProxyFactory();
        result.setTarget(target);
        result.setInterfaces(new Class[] { repositoryInterface, Repository.class });

        result.addAdvisor(ExposeInvocationInterceptor.ADVISOR);

        if (TRANSACTION_PROXY_TYPE != null) {
            result.addInterface(TRANSACTION_PROXY_TYPE);
        }

        for (RepositoryProxyPostProcessor processor : getPostProcessors()) {
            processor.postProcess(result, information);
        }

        if (IS_JAVA_8) {
            result.addAdvice(new DefaultMethodInvokingMethodInterceptor());
        }

        result.addAdvice(new QueryExecutorMethodInterceptor(information, customImplementation, target));

        return (T) result.getProxy(getClassLoader());
    }



    private void validate(RepositoryInformation repositoryInformation, Object customImplementation) {

        if (null == customImplementation && repositoryInformation.hasCustomMethod()) {

            throw new IllegalArgumentException(
                    String.format("You have custom methods in %s but not provided a custom implementation!",
                            repositoryInformation.getRepositoryInterface()));
        }

        validate(repositoryInformation);
    }

    /**
     * This {@code MethodInterceptor} intercepts calls to methods of the custom implementation and delegates the to it if
     * configured. Furthermore it resolves method calls to finders and triggers execution of them. You can rely on having
     * a custom repository implementation instance set if this returns true.
     *
     * @author Oliver Gierke
     */
    public class QueryExecutorMethodInterceptor implements MethodInterceptor {

        private final Map<Method, RepositoryQuery> queries = new ConcurrentHashMap<Method, RepositoryQuery>();

        private final Object customImplementation;
        private final RepositoryInformation repositoryInformation;
        private final QueryExecutionResultHandler resultHandler;
        private final Object target;

        /**
         * Creates a new {@link QueryExecutorMethodInterceptor}. Builds a model of {@link QueryMethod}s to be invoked on
         * execution of repository interface methods.
         */
        public QueryExecutorMethodInterceptor(RepositoryInformation repositoryInformation, Object customImplementation,
                                              Object target) {

            Assert.notNull(repositoryInformation, "RepositoryInformation must not be null!");
            Assert.notNull(target, "Target must not be null!");

            this.resultHandler = new QueryExecutionResultHandler();
            this.repositoryInformation = repositoryInformation;
            this.customImplementation = customImplementation;
            this.target = target;

            QueryLookupStrategy lookupStrategy = getQueryLookupStrategy(getQueryLookupStrategyKey(),
                    getEvaluationContextProvider());
            Iterable<Method> queryMethods = repositoryInformation.getQueryMethods();

            if (lookupStrategy == null) {

                if (queryMethods.iterator().hasNext()) {
                    throw new IllegalStateException("You have defined query method in the repository but "
                            + "you don't have any query lookup strategy defined. The "
                            + "infrastructure apparently does not support query methods!");
                }

                return;
            }

            SpelAwareProxyProjectionFactory factory = new SpelAwareProxyProjectionFactory();
            factory.setBeanClassLoader(getClassLoader());
            factory.setBeanFactory(getBeanFactory());

            for (Method method : queryMethods) {

                RepositoryQuery query = lookupStrategy.resolveQuery(method, repositoryInformation, factory, getNamedQueries());

                invokeListeners(query);
                queries.put(method, query);
            }
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        private void invokeListeners(RepositoryQuery query) {

            for (QueryCreationListener listener : getQueryPostProcessors()) {
                Class<?> typeArgument = GenericTypeResolver.resolveTypeArgument(listener.getClass(),
                        QueryCreationListener.class);
                if (typeArgument != null && typeArgument.isAssignableFrom(query.getClass())) {
                    listener.onCreation(query);
                }
            }
        }

        /*
         * (non-Javadoc)
         * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
         */
        public Object invoke(MethodInvocation invocation) throws Throwable {

            Object result = doInvoke(invocation);

            // Looking up the TypeDescriptor for the return type - yes, this way o.O
            Method method = invocation.getMethod();
            MethodParameter parameter = new MethodParameter(method, -1);
            TypeDescriptor methodReturnTypeDescriptor = TypeDescriptor.nested(parameter, 0);

            return resultHandler.postProcessInvocationResult(result, methodReturnTypeDescriptor);
        }

        private Object doInvoke(MethodInvocation invocation) throws Throwable {

            Method method = invocation.getMethod();
            Object[] arguments = invocation.getArguments();

            if (isCustomMethodInvocation(invocation)) {
                Method actualMethod = repositoryInformation.getTargetClassMethod(method);
                return executeMethodOn(customImplementation, actualMethod, arguments);
            }

            if (hasQueryFor(method)) {
                arguments = TenantUtils.extendArgs(invocation);
                return queries.get(method).execute(arguments);
            }

            // Lookup actual method as it might be redeclared in the interface
            // and we have to use the repository instance nevertheless
            Method actualMethod = repositoryInformation.getTargetClassMethod(method);
            return executeMethodOn(target, actualMethod, arguments);
        }

        /**
         * Executes the given method on the given target. Correctly unwraps exceptions not caused by the reflection magic.
         *
         * @param target
         * @param method
         * @param parameters
         * @return
         * @throws Throwable
         */
        private Object executeMethodOn(Object target, Method method, Object[] parameters) throws Throwable {

            try {
                return method.invoke(target, parameters);
            } catch (Exception e) {
                ClassUtils.unwrapReflectionException(e);
            }

            throw new IllegalStateException("Should not occur!");
        }

        /**
         * Returns whether we know of a query to execute for the given {@link Method};
         *
         * @param method
         * @return
         */
        private boolean hasQueryFor(Method method) {
            return queries.containsKey(method);
        }

        /**
         * Returns whether the given {@link MethodInvocation} is considered to be targeted as an invocation of a custom
         * method.
         *
         * @return
         */
        private boolean isCustomMethodInvocation(MethodInvocation invocation) {

            if (null == customImplementation) {
                return false;
            }

            return repositoryInformation.isCustomMethod(invocation.getMethod());
        }
    }

    /**
     * {@link QueryCreationListener} collecting the {@link QueryMethod}s created for all query methods of the repository
     * interface.
     *
     * @author Oliver Gierke
     */
    private static class QueryCollectingQueryCreationListener implements QueryCreationListener<RepositoryQuery> {

        private List<QueryMethod> queryMethods = new ArrayList<QueryMethod>();

        /**
         * Returns all {@link QueryMethod}s.
         *
         * @return
         */
        public List<QueryMethod> getQueryMethods() {
            return queryMethods;
        }

        /* (non-Javadoc)
         * @see org.springframework.data.repository.core.support.QueryCreationListener#onCreation(org.springframework.data.repository.query.RepositoryQuery)
         */
        public void onCreation(RepositoryQuery query) {
            this.queryMethods.add(query.getQueryMethod());
        }
    }

    /**
     * Simple value object to build up keys to cache {@link RepositoryInformation} instances.
     *
     * @author Oliver Gierke
     */
    private static class RepositoryInformationCacheKey {

        private final String repositoryInterfaceName;
        private final String customImplementationClassName;

        /**
         * Creates a new {@link RepositoryInformationCacheKey} for the given {@link RepositoryMetadata} and cuytom
         * implementation type.
         *
         */
        public RepositoryInformationCacheKey(RepositoryMetadata metadata, Class<?> customImplementationType) {
            this.repositoryInterfaceName = metadata.getRepositoryInterface().getName();
            this.customImplementationClassName = customImplementationType == null ? null : customImplementationType.getName();
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {

            if (!(obj instanceof RepositoryInformationCacheKey)) {
                return false;
            }

            RepositoryInformationCacheKey that = (RepositoryInformationCacheKey) obj;
            return this.repositoryInterfaceName.equals(that.repositoryInterfaceName)
                    && ObjectUtils.nullSafeEquals(this.customImplementationClassName, that.customImplementationClassName);
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {

            int result = 31;

            result += 17 * repositoryInterfaceName.hashCode();
            result += 17 * ObjectUtils.nullSafeHashCode(customImplementationClassName);

            return result;
        }
    }
}
