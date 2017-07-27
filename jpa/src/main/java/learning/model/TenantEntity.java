package learning.model;

import javax.persistence.MappedSuperclass;

/**
 * Created by isaac on 24/07/2017.
 */
@MappedSuperclass
public class TenantEntity {
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "TenantEntity{" +
                "tenantId=" + tenantId +
                '}';
    }
}
