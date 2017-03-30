package learning;

import learning.dao.PersonDAO;
import learning.model.Address;
import learning.model.Person;
import learning.service.CommonService;
import learning.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by isaac on 27/03/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@Commit
public class JpaTest {
    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private PersonService personService;

    @Autowired
    private CommonService commonService;

//    @Autowired
//    private CommandLineRunner commandLineRunner;
//
    @Autowired
    private Application application;

    @Autowired
    @Qualifier("asyncExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void test(){
        Person person = new Person();
        person.setAge(10);
        personDAO.save(person);
        System.err.println(person.getId());
        personService.saveNew();
        commonService.save();
    }

    @Test
    public void test2() throws Exception {
        personService.saveNew();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Person person2 = commonService.save();
                System.err.println(person2);
                System.err.println(Thread.currentThread().getName());

            }
        });
        thread.setDaemon(true);
        System.err.println(thread.isDaemon());
        thread.start();
//        thread.join();

    }

    @Test
    public void appTest() throws Exception {
        application.runner().run(null);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(30);
        threadPoolTaskExecutor.shutdown();

//        Thread.sleep(3000L);
    }

    @Test
    public void lazyTest(){
        Person person = commonService.lazySave();
        Address address = commonService.findAddress(person.getId());

        address.toDto();
        System.err.println(address.getId());
    }

}
