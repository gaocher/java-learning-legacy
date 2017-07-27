package learning;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import learning.dao.AddressDAO;
import learning.dao.PageableResponse;
import learning.dao.PersonDAO;
import learning.model.Address;
import learning.model.Person;
import learning.service.AccountService;
import learning.service.CommonService;
import learning.service.PersonService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;

/**
 * Created by isaac on 27/03/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@Commit
@Sql("/scripts/init.sql")
public class JpaTest {
    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private AddressDAO addressDAO;

    @Autowired
    private PersonService personService;

    @Autowired
    private CommonService commonService;

    @Qualifier("accountService")
    @Autowired
    private AccountService accountService;

//    @Autowired
//    private CommandLineRunner commandLineRunner;
//
    @Autowired
    private Application application;

    @Autowired
    @Qualifier("asyncExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Rule
    public ExpectedException thrown = ExpectedException.none();



    @Before
    public void setUp(){

    }

    @Test
    public void test(){
        Person person = new Person();
        person.setAge(10);
        personDAO.save(person);
        System.err.println(person.getId());
        personService.saveNew();
        commonService.save();

        Page<Person> byAgeLessThan = personDAO.findByAgeLessThan(12, new PageRequest(1, 10));
        PageableResponse<Person> of = PageableResponse.of(byAgeLessThan);

        System.out.println("page==="+of);
        Person one = personDAO.findOne(person.getId());
        one.getLazyField();
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
//        application.runner().run(null);
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

        Person one = personDAO.findOne(person.getId());

        commonService.detachTest(one);
    }

    @Test
    public void cascadePersistTest(){
        Person person = new Person();
        person.setName("isaac");
        personDAO.save(person);

        Address address = new Address();

        address.setAddr("youyou century sqaure");
//        address.setPerson(person);
        person.setAddress(address);

        addressDAO.save(address);

//        personDAO.save(person);
    }


    @Test
    public void defaultInterfaceMethodTest(){
        accountService.doExecute();
    }


    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void uniqueTest(){
        Address address = new Address();
        address.setUniqueKey("a");
        addressDAO.save(address);
        Address a = addressDAO.findByUniqueKey("a");
        Assert.assertEquals(a.getUniqueKey(),"a");
        Address address2 = new Address();
        address2.setUniqueKey("a");
        addressDAO.save(address2);
        addressDAO.findByUniqueKey("a");
    }

    @Test
    public void customDAOInterfaceTest(){
        Person person = new Person();
        person.setName("isaac");
        person.setTenantId(1L);
        Person save = personDAO.save(person);
        Person byId = personDAO.findOne(1L,save.getId());
        assertThat(byId.getName(),is("isaac"));
        assertThat(byId.getId(),is(save.getId()));
        List<Person> all = personDAO.findAll();
        System.out.println(all);
    }




}
