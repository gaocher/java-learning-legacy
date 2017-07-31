package org.springframework.data.jpa.repository.query;

import learning.jpaExtend.extend.MethodWrapper;
import learning.jpaExtend.extend.Reflections;
import learning.jpaExtend.extend.TenantUtils;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by isaac on 26/07/2017.
 */
@Slf4j
public class TenantJpaQueryMethod extends JpaQueryMethod {
    private volatile JpaParameters parameters;

    private Integer skipParameterNum = null;
    /**
     * Creates a {@link JpaQueryMethod}.
     *
     * @param method    must not be {@literal null}
     * @param metadata  must not be {@literal null}
     * @param factory
     * @param extractor must not be {@literal null}
     */
    public TenantJpaQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory, QueryExtractor extractor) {
        super(method, metadata, factory, extractor);
    }

    @Override
    String getAnnotatedQuery() {
        if(!TenantUtils.isTenantMethod(getMethod())){
            return super.getAnnotatedQuery();
        }
        if(StringUtils.isNullOrEmpty(super.getAnnotatedQuery())){
            return super.getAnnotatedQuery();
        }
        String annotatedQuery = super.getAnnotatedQuery();
        return annotatedQuery+" and tenantId = ?" + (getParameters().getNumberOfParameters()-getSkipParamNum());
    }

    @Override
    public String getName() {
        if(!TenantUtils.isTenantMethod(getMethod())){
            return super.getName();
        }
        return super.getName() + "AndTenantId";
    }

    @Override
    public JpaParameters getParameters() {
        if(!TenantUtils.isTenantMethod(getMethod())){
            return super.getParameters();
        }
        if(parameters == null){
            parameters = createParameters(new MethodWrapper(getMethod()).getTenantMethod());
        }
        return parameters;
    }



    private Method getMethod(){
        return (Method)Reflections.getField(JpaQueryMethod.class, this, "method");
    }

    private int getSkipParamNum(){
        if(skipParameterNum == null){
            skipParameterNum = 0;
            List<Class<?>> types = Arrays.asList(getMethod().getParameterTypes());
            if(types.indexOf(Pageable.class)>=0)skipParameterNum++;
            if(types.indexOf(Sort.class)>=0)skipParameterNum++;
        }
        return skipParameterNum;
    }
}
