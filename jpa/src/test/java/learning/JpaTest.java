package learning;

import learning.dao.AccountDAO;
import learning.dao.AddressDAO;
import learning.dao.PageableResponse;
import learning.dao.PersonDAO;
import learning.model.Account;
import learning.model.Address;
import learning.model.CmpKey;
import learning.model.Person;
import learning.service.AccountService;
import learning.service.CommonService;
import learning.service.PersonService;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by isaac on 27/03/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@Commit
//@Sql("/scripts/init.sql")
public class JpaTest {
    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private AddressDAO addressDAO;

    @Autowired
    private AccountDAO accountDAO;

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

    @Test
    public void and_or_test(){
        Person person = new Person();
        person.setName("isaac");
        person.setAge(10);
        person.setVersion(1L);
        personDAO.save(person);
        Person person2 = new Person();
        person.setName("isaac");
        person.setAge(10);
        person.setVersion(2L);
        personDAO.save(person);
        List<Person> isaac = personDAO.findByVersionAndAgeOrName(1L, 10, "isaac");
        System.out.println(isaac);
    }

    @Test
    public void composite_key_query_test() {
        Person person = new Person();
        person.setName("isaac");
        person.setAge(10);
        person.setVersion(1L);
        CmpKey cmpKey = new CmpKey(1L, CmpKey.KeyType.A);
        person.setKey(cmpKey);
        personDAO.save(person);
        Person byKey = personDAO.findByKey(cmpKey);
        assertThat(byKey,notNullValue());
    }

    @Test
    public void list_content_save_test() {
        Person person = new Person();
        person.setName("isaac");
        person.setAge(10);
        person = personDAO.save(person); // detached entity - save
        final Person save = personDAO.findOne(person.getId());
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account("1"));
        accounts.add(new Account("2"));
        save.setAccounts(accounts);
        accounts.forEach(a -> a.setPerson(save));
        personDAO.save(save); // detached entity - person2
        Person person2 = personDAO.findOne(save.getId());
        List<Account> accountList = accountDAO.findByPersonId(save.getId());
        accountList.get(0).setName("change 1");
        accountList.add(new Account("3"));
        person2.setAccounts(accountList);
        accountList.forEach(a -> a.setPerson(save));
        personDAO.save(person2);

    }

    @Test
    public void aggregatedQueryTest() {
        Person person = new Person();
        person.setName("person 1");
        Account account = new Account();
        account.setName("account 1");
        account.setAmount(new BigDecimal(10));
        account.setVersion(1);
        account.setPerson(person);

        Account account2 = new Account();
        account2.setName("account 2");
        account2.setAmount(new BigDecimal(20));
        account2.setVersion(2);
        account2.setPerson(person);

        person.setAccounts(Arrays.asList(account,account2));

        personDAO.save(person);

        Person person2 = new Person();
        person.setName("person 1");
        Account account21 = new Account();
        account21.setName("account 21");
        account21.setAmount(new BigDecimal(10));
        account21.setVersion(1);
        account21.setPerson(person2);

        person2.setAccounts(Arrays.asList(account21));

        personDAO.save(person2);

        List<?> accountList = accountDAO.aggregatedQuery(new PageRequest(1, 1));
        accountList.forEach(a -> System.out.println(a));

    }



}
