package org.springframework.data.jpa.repository.support;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

/**
 * Created by isaac on 25/07/2017.
 */
@Slf4j
public class TenantJpaRepositoryFactoryBean extends JpaRepositoryFactoryBean {
    @Override
    protected TenantJpaRepositoryFactory createRepositoryFactory(EntityManager entityManager) {
        log.info("createRepositoryFactory in TenantJpaRepositoryFactoryBean");
        return new TenantJpaRepositoryFactory(entityManager);
    }
}
