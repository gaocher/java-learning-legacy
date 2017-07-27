package learning.jpaExtend.contextAware;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by isaac on 27/07/2017.
 */
public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {
    @Override
    public void execute(Runnable task) {
        super.execute(ContextAwareRunnable.of(task,TenantContextHolder.getTenantContext()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(ContextAwareRunnable.of(task,TenantContextHolder.getTenantContext()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(ContextAwareCallable.of(task,TenantContextHolder.getTenantContext()));
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return super.submitListenable(ContextAwareRunnable.of(task,TenantContextHolder.getTenantContext()));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return super.submitListenable(ContextAwareCallable.of(task,TenantContextHolder.getTenantContext()));
    }
}
