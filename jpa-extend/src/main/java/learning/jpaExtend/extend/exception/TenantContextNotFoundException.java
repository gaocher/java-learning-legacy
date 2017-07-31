package learning.jpaExtend.extend.exception;

/**
 * Created by isaac on 28/07/2017.
 */
public class TenantContextNotFoundException extends IllegalStateException {
    public TenantContextNotFoundException() {
        super("TenantContext not found in thread [" + Thread.currentThread().getName() + "]");
    }
}
