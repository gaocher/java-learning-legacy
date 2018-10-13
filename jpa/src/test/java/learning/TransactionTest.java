package learning;

import learning.dao.PersonDAO;
import learning.model.Address;
import learning.model.Person;
import learning.service.CommonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by isaac on 21/08/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TransactionTest {
    @Autowired
    private CommonService commonService;

    @Autowired
    private PersonDAO personDAO;

    @Test
    @Commit
    public void test() {
        commonService.transactionTest();
        List<Person> all = personDAO.findAll();
        System.out.println(all.size());
        System.out.println(all);
    }

    @Test
    public void detach_modify_for_eager_fetch() {
        Person person = new Person();
        person.setName("common");
        Address address = new Address();
        address.setAddr("abc");
        person.setEagerAddress(address);
        Person detach = personDAO.save(person);
        commonService.transactionModify(detach,"test");
        assertThat(person.getEagerAddress().getAddr(),is("test"));

    }
}
