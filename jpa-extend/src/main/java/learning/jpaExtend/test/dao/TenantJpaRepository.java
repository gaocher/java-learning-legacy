package learning.jpaExtend.test.dao;

import learning.jpaExtend.test.model.TenantEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;

/**
 * Created by isaac on 24/07/2017.
 */
@Transactional(readOnly = true)
public class TenantJpaRepository<T extends TenantEntity,ID extends Serializable> extends SimpleJpaRepository<T,ID> implements CustomDAO<T,ID>{
    public TenantJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public TenantJpaRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    public T findOne(Long tenantId, ID id){
        System.out.println("findOne");
        return findByTenantIdAndId(tenantId,id);
    }

    private T findByTenantIdAndId(Long tenantId, ID id) {
        Specification<T> spec = (root, query, cb) -> {
            Predicate tenantId1 = cb.equal(root.get("tenantId"), tenantId);
            Predicate id1 = cb.equal(root.get("id"), id);
            return cb.and(tenantId1,id1);
        };
        return findOne(spec);
    }
}
