package learning.jpaExtend.contextAware;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * this class should be immutable
 * Created by isaac on 26/07/2017.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TenantContext {
    private Long tenantId;

    public static TenantContext of(Long tenantId){
        TenantContext tenantContext = new TenantContext(tenantId);
        return tenantContext;
    }
}
