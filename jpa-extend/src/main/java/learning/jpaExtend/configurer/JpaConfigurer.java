package learning.jpaExtend.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.TenantJpaRepositoryFactoryBean;

import learning.jpaExtend.test.dao.PersonDAO;
import learning.jpaExtend.test.dao.TenantJpaRepository;

/**
 * Created by isaac on 25/07/2017.
 */
@Configuration
//@EnableJpaRepositories(basePackageClasses = PersonDAO.class, repositoryBaseClass = TenantJpaRepository.class)
@EnableJpaRepositories(basePackageClasses = PersonDAO.class, repositoryFactoryBeanClass = TenantJpaRepositoryFactoryBean.class, repositoryBaseClass = TenantJpaRepository.class)
public class JpaConfigurer {
}
