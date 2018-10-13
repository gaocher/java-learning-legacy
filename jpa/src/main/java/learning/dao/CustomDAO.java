package learning.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by isaac on 24/07/2017.
 */
@NoRepositoryBean
public interface CustomDAO<T,ID extends Serializable> extends JpaRepository<T,ID> {
    T findOne(Long tenantId, ID id);
}
