package learning.jpaExtend.test;

import learning.jpaExtend.contextAware.ContextAwarePoolExecutor;
import learning.jpaExtend.contextAware.TenantContext;
import learning.jpaExtend.contextAware.TenantContextHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by isaac on 27/07/2017.
 */
@Slf4j
@RunWith(JUnit4.class)
public class ContextAwareTest {
    @Test
    public void self_get_set_match(){
        TenantContext tenantContext = new TenantContext(1L);
        TenantContextHolder.setTenantContext(tenantContext);
        assertTo(tenantContext);
    }

    @Test
    public void self_multiple_get_set_match(){
        TenantContext first = new TenantContext(1L);
        TenantContextHolder.setTenantContext(first);
        assertTo(first);
        TenantContext second = new TenantContext(11L);
        TenantContextHolder.setTenantContext(second);
        assertTo(second);
    }

    @Test
    public void parent_set_match_child_get(){
        TenantContext tenantContext = new TenantContext(1L);
        TenantContextHolder.setTenantContext(tenantContext);
        assertChildThread(tenantContext);
    }

    @Test
    public void parent_set_not_match_child_get_by_no_inheritable(){
        TenantContext tenantContext = new TenantContext(1L);
        TenantContextHolder.setTenantContext(tenantContext,false);
        assertChildThread(null); // expect null
    }

    @Test
    public void parent_multiple_set_child_only_match_first_get_value() throws InterruptedException {
        TenantContext first = new TenantContext(1L);
        TenantContext second = new TenantContext(11L);
        List<Runnable> build = new StepRunnerBuilder().add(() -> assertTo(first)).add(() -> assertTo(first)).build();
        TenantContextHolder.setTenantContext(first);
        StepExecutor stepExecutor = new StepExecutor(Thread.currentThread(), build);
        stepExecutor.start();
        wait(stepExecutor);
        TenantContextHolder.setTenantContext(second);
        notify(Thread.currentThread());
        stepExecutor.join();
    }

    @Test
    public void child_set_not_change_parent(){
        TenantContext parentContext = new TenantContext(1L);
        TenantContextHolder.setTenantContext(parentContext);
        TenantContext childContext = new TenantContext(10L);
        childThreadSet(childContext);
        assertTo(parentContext);
    }

    @Test
    public void work_thread_match_parent() throws InterruptedException {
        TenantContext first = new TenantContext(1L);
        TenantContextHolder.setTenantContext(first);
        ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
        executor.initialize();
        NotifyRunner notifyRunner = new NotifyRunner(() -> assertTo(first));
        executor.execute(notifyRunner);
        notifyRunner.waits();
    }

    @Test
    public void work_thread_match_multiple_parent_set() throws InterruptedException {
        TenantContext first = new TenantContext(1L);
        TenantContext second = new TenantContext(2L);
        TenantContextHolder.setTenantContext(first);
        ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
        executor.initialize();
        NotifyRunner firstRunner = new NotifyRunner(() -> assertTo(first));
        executor.execute(firstRunner);

        //parent set second context
        TenantContextHolder.setTenantContext(second);
        NotifyRunner secondRunner = new NotifyRunner(() -> assertTo(second));

        executor.execute(secondRunner);
        firstRunner.waits();
        secondRunner.waits();
    }

    private void wait(Object obj) throws InterruptedException {
        synchronized (obj){
            log.info("wait on " + obj + " " + TenantContextHolder.getTenantContext());
            obj.wait();
        }
    }

    private void notify(Object obj) throws InterruptedException {
        synchronized (obj){
            log.info("notify on " + obj+" " + TenantContextHolder.getTenantContext());
            obj.notify();
        }
    }
    

    /**
     * child set and then parent set and then child don't change and parent don't change either
     */
    @Test
    public void child_set_parent_set_not_change_child() throws InterruptedException {
        TenantContext first = new TenantContext(1L);
        TenantContext second = new TenantContext(11L);
        TenantContext childFirst = new TenantContext(201L);
        List<Runnable> build = new StepRunnerBuilder()
                .add(() -> assertTo(first))
                .add(() -> TenantContextHolder.setTenantContext(childFirst))
                .add(() -> assertTo(childFirst)).build();
        TenantContextHolder.setTenantContext(first);
        StepExecutor stepExecutor = new StepExecutor(Thread.currentThread(), build);
        stepExecutor.start(); //assert child
        wait(stepExecutor);
        notify(Thread.currentThread()); //child set 201
        wait(stepExecutor);
        assertTo(first);
        TenantContextHolder.setTenantContext(second);
        notify(Thread.currentThread()); // assert child
        stepExecutor.join();
        assertTo(second);
    }


    private void assertTo(TenantContext expected){
        assertThat(TenantContextHolder.getTenantContext(),is(expected));
    }

    private void assertChildThread(TenantContext expected){
        AsyncTester.test(() -> {
            assertTo(expected);
        });
    }

    private Thread childThreadSet(TenantContext tenantContext){
        return AsyncTester.test(() -> {
            TenantContextHolder.setTenantContext(tenantContext);
        });
    }

    static class NotifyRunner implements Runnable{
        private Runnable runnable;

        private AssertionError ex;

        public NotifyRunner(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (AssertionError ex){
                this.ex = ex;
            } finally {
                notifies();
            }
        }

        public void waits() throws InterruptedException {
            synchronized (this){
                wait();
            }
            assertion();
        }

        public void notifies(){
            synchronized (this){
                notify();
            }
        }

        private void assertion(){
            if (ex != null){
                throw ex;
            }
        }
    }

    static class AsyncTester{
        private Thread thread;
        private volatile AssertionError exc;

        public AsyncTester(final Runnable runnable){
            thread = new Thread(new Runnable(){
                public void run(){
                    try{
                        runnable.run();
                    }catch(AssertionError e){
                        exc = e;
                    }
                }
            });
        }

        public Thread test() throws Exception {
            thread.start();
            thread.join();
            if (exc != null)
                throw exc;
            return thread;
        }

        public static Thread test(Runnable runnable){
            try {
               return new AsyncTester(runnable).test();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class StepRunnerBuilder {
        private List<Runnable> tasks = Lists.newArrayList();

        public StepRunnerBuilder add(Runnable runable){
            tasks.add(runable);
            return this;
        }

        public List<Runnable> build(){
            return tasks;
        }
    }
    @Data
    static class StepExecutor extends Thread {
        private Thread counterparty;
        private List<Runnable> tasks;

        private volatile AssertionError ex;

        public StepExecutor(Thread counterparty, List<Runnable> tasks) {
            this.tasks = tasks;
            this.counterparty = counterparty;
        }

        @Override
        public void run() {
            for (int i = 0; i < tasks.size(); i++) {
                log.info("start run step " + i  + " " + TenantContextHolder.getTenantContext());
                tasks.get(i).run();
                try {
                    notify(this);
                } catch (AssertionError ex){
                    this.ex = ex;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    if(i != tasks.size() -1 ){
                        wait(counterparty);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void wait(Object obj) throws InterruptedException {
            synchronized (obj){
                log.info("wait on " + obj+" " + TenantContextHolder.getTenantContext());
                obj.wait();
            }
            assertion();
        }

        private void notify(Object obj) throws InterruptedException {
            synchronized (obj){
                log.info("notify on " + obj+" " + TenantContextHolder.getTenantContext());
                obj.notify();
            }
            assertion();
        }

        private void assertion(){
            if(ex != null){
                throw ex;
            }
        }

        @Override
        public String toString() {
            return "StepExecutor{} " + super.toString();
        }
    }

}
