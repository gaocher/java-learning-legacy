package learning.jpaExtend.contextAware;

import learning.jpaExtend.extend.exception.TenantContextNotFoundException;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

/**
 * Created by isaac on 26/07/2017.
 */
public class TenantContextHolder {
    public static Long getTenantId(){
        TenantContext tenantContext = getTenantContext();
        if(tenantContext == null){
            throw new TenantContextNotFoundException();
        }
        return tenantContext.getTenantId();
    }

    private static final ThreadLocal<TenantContext> contextHolder =
            new NamedThreadLocal<>("tenant context");

    private static final ThreadLocal<TenantContext> inheritableContextHolder =
            new NamedInheritableThreadLocal<>("tenant inheritable context");

    /**
     * Reset the TenantContext for the current thread.
     */
    public static void reset() {
        contextHolder.remove();
        inheritableContextHolder.remove();
    }

    public static void setTenantContext(TenantContext tenantContext) {
        setTenantContext(tenantContext, true);
    }

    public static void setTenantContext(TenantContext tenantContext, boolean inheritable) {
        if (tenantContext == null) {
            reset();
        }
        else {
            if (inheritable) {
                inheritableContextHolder.set(tenantContext);
                contextHolder.remove();
            }
            else {
                contextHolder.set(tenantContext);
                inheritableContextHolder.remove();
            }
        }
    }

    /**
     * Return the RequestAttributes currently bound to the thread.
     * @return the RequestAttributes currently bound to the thread,
     * or {@code null} if none bound
     */
    public static TenantContext getTenantContext() {
        TenantContext tenantContext = contextHolder.get();
        if (tenantContext == null) {
            tenantContext = inheritableContextHolder.get();
        }
        return tenantContext;
    }


}
