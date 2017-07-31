package learning.jpaExtend.extend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.TenantJpaRepositoryFactory;

import javax.persistence.EntityManager;

/**
 * Created by isaac on 25/07/2017.
 */
@Slf4j
public class TenantJpaRepositoryFactoryBean extends JpaRepositoryFactoryBean {
    @Override
    protected TenantJpaRepositoryFactory createRepositoryFactory(EntityManager entityManager) {
        return new TenantJpaRepositoryFactory(entityManager);
    }
}
