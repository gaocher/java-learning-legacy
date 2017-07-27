package learning.jpaExtend.contextAware;

import java.util.concurrent.Callable;

/**
 * Created by isaac on 27/07/2017.
 */
public class ContextAwareCallable<V> implements Callable<V> {
    private Callable<V> callable;
    private TenantContext context;


    @Override
    public V call() throws Exception {
        if (context != null){
            TenantContextHolder.setTenantContext(context);
        }
        try {
            return callable.call();
        } finally {
            TenantContextHolder.reset();
        }
    }

    public static <V> ContextAwareCallable<V> of(Callable<V> callable, TenantContext tenantContext){
        ContextAwareCallable<V> contextAwareCallable = new ContextAwareCallable<>();
        contextAwareCallable.callable = callable;
        contextAwareCallable.context = tenantContext;
        return contextAwareCallable;
    }
}
