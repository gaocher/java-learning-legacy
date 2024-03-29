/*
 * Copyright 2008-2016 the original author or authors.
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
package org.springframework.data.jpa.repository.query;

import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;

/**
 * Query lookup strategy to support multi-tenant
 */
public final class TenantJpaQueryLookupStrategy {

    private TenantJpaQueryLookupStrategy() {}


    private abstract static class AbstractQueryLookupStrategy implements QueryLookupStrategy {

        private final EntityManager em;
        private final QueryExtractor provider;


        public AbstractQueryLookupStrategy(EntityManager em, QueryExtractor extractor) {

            this.em = em;
            this.provider = extractor;
        }

        @Override
        public final RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
                                                  NamedQueries namedQueries) {
            //use TenantJpaQueryMethod to override
            return resolveQuery(new TenantJpaQueryMethod(method, metadata, factory, provider), em, namedQueries);
        }

        protected abstract RepositoryQuery resolveQuery(JpaQueryMethod method, EntityManager em, NamedQueries namedQueries);
    }

    private static class CreateQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private final PersistenceProvider persistenceProvider;

        public CreateQueryLookupStrategy(EntityManager em, QueryExtractor extractor) {

            super(em, extractor);
            this.persistenceProvider = PersistenceProvider.fromEntityManager(em);
        }

        @Override
        protected RepositoryQuery resolveQuery(JpaQueryMethod method, EntityManager em, NamedQueries namedQueries) {

            try {
                return new PartTreeJpaQuery(method, em, persistenceProvider);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        String.format("Could not create query metamodel for method %s!", method.toString()), e);
            }
        }

    }

    private static class DeclaredQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private final EvaluationContextProvider evaluationContextProvider;

        public DeclaredQueryLookupStrategy(EntityManager em, QueryExtractor extractor,
                                           EvaluationContextProvider evaluationContextProvider) {

            super(em, extractor);
            this.evaluationContextProvider = evaluationContextProvider;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy.AbstractQueryLookupStrategy#resolveQuery(org.springframework.data.jpa.repository.query.JpaQueryMethod, javax.persistence.EntityManager, org.springframework.data.repository.core.NamedQueries)
         */
        @Override
        protected RepositoryQuery resolveQuery(JpaQueryMethod method, EntityManager em, NamedQueries namedQueries) {

            RepositoryQuery query = JpaQueryFactory.INSTANCE.fromQueryAnnotation(method, em, evaluationContextProvider);

            if (null != query) {
                return query;
            }

            query = JpaQueryFactory.INSTANCE.fromProcedureAnnotation(method, em);

            if (null != query) {
                return query;
            }

            String name = method.getNamedQueryName();
            if (namedQueries.hasQuery(name)) {
                return JpaQueryFactory.INSTANCE.fromMethodWithQueryString(method, em, namedQueries.getQuery(name),
                        evaluationContextProvider);
            }

            query = NamedQuery.lookupFrom(method, em);

            if (null != query) {
                return query;
            }

            throw new IllegalStateException(
                    String.format("Did neither find a NamedQuery nor an annotated query for method %s!", method));
        }
    }

    private static class CreateIfNotFoundQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private final DeclaredQueryLookupStrategy lookupStrategy;
        private final CreateQueryLookupStrategy createStrategy;

        /**
         * Creates a new {@link CreateIfNotFoundQueryLookupStrategy}.
         *
         * @param em
         * @param extractor
         * @param createStrategy
         * @param lookupStrategy
         * @param
         */
        public CreateIfNotFoundQueryLookupStrategy(EntityManager em, QueryExtractor extractor,
                                                   CreateQueryLookupStrategy createStrategy, DeclaredQueryLookupStrategy lookupStrategy) {

            super(em, extractor);

            this.createStrategy = createStrategy;
            this.lookupStrategy = lookupStrategy;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy.AbstractQueryLookupStrategy#resolveQuery(org.springframework.data.jpa.repository.query.JpaQueryMethod, javax.persistence.EntityManager, org.springframework.data.repository.core.NamedQueries)
         */
        @Override
        protected RepositoryQuery resolveQuery(JpaQueryMethod method, EntityManager em, NamedQueries namedQueries) {

            try {
                return lookupStrategy.resolveQuery(method, em, namedQueries);
            } catch (IllegalStateException e) {
                return createStrategy.resolveQuery(method, em, namedQueries);
            }
        }
    }

    public static QueryLookupStrategy create(EntityManager em, Key key, QueryExtractor extractor,
                                             EvaluationContextProvider evaluationContextProvider) {

        Assert.notNull(em, "EntityManager must not be null!");
        Assert.notNull(extractor, "QueryExtractor must not be null!");
        Assert.notNull(evaluationContextProvider, "EvaluationContextProvider must not be null!");

        switch (key != null ? key : Key.CREATE_IF_NOT_FOUND) {
            case CREATE:
                return new CreateQueryLookupStrategy(em, extractor);
            case USE_DECLARED_QUERY:
                return new DeclaredQueryLookupStrategy(em, extractor, evaluationContextProvider);
            case CREATE_IF_NOT_FOUND:
                return new CreateIfNotFoundQueryLookupStrategy(em, extractor, new CreateQueryLookupStrategy(em, extractor),
                        new DeclaredQueryLookupStrategy(em, extractor, evaluationContextProvider));
            default:
                throw new IllegalArgumentException(String.format("Unsupported query lookup strategy %s!", key));
        }
    }
}
