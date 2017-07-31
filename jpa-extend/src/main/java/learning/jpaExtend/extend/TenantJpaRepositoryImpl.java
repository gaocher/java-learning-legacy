package learning.jpaExtend.extend;

import learning.jpaExtend.contextAware.TenantContextHolder;
import learning.jpaExtend.extend.exception.TenantContextNotFoundException;
import learning.jpaExtend.extend.model.TenantEntity;
import learning.jpaExtend.extend.model.TenantJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.List;

/**
 * Created by isaac on 24/07/2017.
 */
public class TenantJpaRepositoryImpl<T extends TenantEntity,ID extends Serializable> extends SimpleJpaRepository<T,ID> implements TenantJpaRepository<T,ID> {
    public TenantJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public TenantJpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    @Override
    public T findOne(ID id){
        return findByTenantIdAndId(getTenantId(), id);
    }

    @Override
    public List<T> findAll() {
        return findByTenantId(getTenantId());
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        List<T> all = super.findAll(ids);
        all.forEach(this::checkTenantId);
        return all;
    }

    @Override
    public List<T> findAll(Sort sort) {
        return findByTenantId(getTenantId(),sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return findByTenantId(getTenantId(),pageable);
    }

    @Transactional
    @Override
    public <S extends T> S save(S entity) {
        checkTenantId(entity);
        return super.save(entity);
    }

    @Transactional
    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        entities.forEach(this::checkTenantId);
        return super.save(entities);
    }

    @Transactional
    @Override
    public void delete(T entity) {
        super.delete(entity);
    }

    private Long getTenantId(){
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            throw new TenantContextNotFoundException();
        }
        return tenantId;
    }

    private void checkTenantId(T entity){
        if (!getTenantId().equals(entity.getTenantId())) {
            throw new IllegalArgumentException("the entity "+entity+" has unmatched tenant id " + getTenantId());
        }
    }

    private T findByTenantIdAndId(Long tenantId, ID id) {
        Specification<T> spec = (root, query, cb) -> {
            Predicate tenantId1 = cb.equal(root.get(TenantEntity.TENANT_ID_NAME), tenantId);
            Predicate id1 = cb.equal(root.get(TenantEntity.PRIMARY_KEY_NAME), id);
            return cb.and(tenantId1,id1);
        };
        return findOne(spec);
    }

    private List<T> findByTenantId(Long tenantId) {
        Specification<T> spec = (root, query, cb) ->  cb.equal(root.get(TenantEntity.TENANT_ID_NAME), tenantId);
        return findAll(spec);
    }

    private Page<T> findByTenantId(Long tenantId, Pageable pageable) {
        Specification<T> spec = (root, query, cb) ->  cb.equal(root.get(TenantEntity.TENANT_ID_NAME), tenantId);
        return findAll(spec,pageable);
    }

    private List<T> findByTenantId(Long tenantId, Sort sort) {
        Specification<T> spec = (root, query, cb) ->  cb.equal(root.get(TenantEntity.TENANT_ID_NAME), tenantId);
        return findAll(spec,sort);
    }
}
