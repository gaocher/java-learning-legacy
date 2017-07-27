package learning.jpaExtend.contextAware;

/**
 * Created by isaac on 27/07/2017.
 */
public class ContextAwareRunnable implements Runnable {
    private Runnable runnable;
    private TenantContext context;

    @Override
    public void run() {
        if(context != null){
            TenantContextHolder.setTenantContext(context);
        }
        try{
            runnable.run();
        }finally {
            TenantContextHolder.reset();
        }
    }

    public static ContextAwareRunnable of(Runnable runnable, TenantContext context){
        ContextAwareRunnable contextAwareRunnable = new ContextAwareRunnable();
        contextAwareRunnable.runnable = runnable;
        contextAwareRunnable.context = context;
        return contextAwareRunnable;
    }
}
