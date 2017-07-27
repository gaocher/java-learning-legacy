package learning.service.multiTenant;

import lombok.Data;

/**
 * Created by isaac on 25/07/2017.
 */
@Data
public class TenantTask {
    public static final String TENANT_TASK = "tenantTask";
    private Long tenantId;

    private String taskName;

}
