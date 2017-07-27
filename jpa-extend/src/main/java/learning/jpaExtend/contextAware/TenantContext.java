package learning.jpaExtend.contextAware;

import lombok.Data;

/**
 * Created by isaac on 26/07/2017.
 */
@Data
public class TenantContext {
    private Long tenantId;

    public static TenantContext of(Long tenantId){
        TenantContext tenantContext = new TenantContext();
        tenantContext.setTenantId(tenantId);
        return tenantContext;
    }
}
