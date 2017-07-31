package learning.jpaExtend.extend.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by isaac on 28/07/2017.
 */
@NoRepositoryBean
public interface TenantJpaRepository<T,ID extends Serializable> extends JpaRepository<T,ID>{

}
