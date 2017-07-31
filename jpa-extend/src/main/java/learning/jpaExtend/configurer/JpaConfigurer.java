package learning.jpaExtend.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import learning.jpaExtend.extend.TenantJpaRepositoryFactoryBean;
import learning.jpaExtend.test.dao.PersonDAO;

/**
 * Created by isaac on 25/07/2017.
 */
@Configuration
//@EnableJpaRepositories(basePackageClasses = PersonDAO.class, repositoryBaseClass = TenantJpaRepository.class)
@EnableJpaRepositories(basePackageClasses = PersonDAO.class, repositoryFactoryBeanClass = TenantJpaRepositoryFactoryBean.class)
//@EnableJpaRepositories(basePackageClasses = PersonDAO.class, repositoryFactoryBeanClass = TenantJpaRepositoryFactoryBean.class, repositoryBaseClass = TenantJpaRepositoryImpl.class)
public class JpaConfigurer {
}
