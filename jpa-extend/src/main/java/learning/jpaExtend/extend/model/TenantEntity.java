package learning.jpaExtend.extend.model;

import javax.persistence.MappedSuperclass;

/**
 * Created by isaac on 24/07/2017.
 */
@MappedSuperclass
public class TenantEntity {
    public static String TENANT_ID_NAME = "tenantId";

    public static String PRIMARY_KEY_NAME = "id";

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
