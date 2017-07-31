package learning.jpaExtend.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import learning.jpaExtend.Application;
import learning.jpaExtend.contextAware.TenantContext;
import learning.jpaExtend.contextAware.TenantContextHolder;
import learning.jpaExtend.test.dao.PersonDAO;
import learning.jpaExtend.test.model.Person;

/**
 * Created by isaac on 28/07/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class JpaExtendTest {
    @Autowired
    private PersonDAO personDAO;

    @Test
    public void saveAndQuery(){
        TenantContextHolder.setTenantContext(new TenantContext(1L));
        Person person = new Person();
        person.setTenantId(1L);
        person.setAge(8);
        person.setName("ppp");
        Person save = personDAO.save(person);
        Person one = personDAO.findOne(save.getId());
//        Page<Person> byAgeLessThan = personDAO.findByAgeLessThan(new PageRequest(0, 1), 10);
//        log.info(byAgeLessThan.toString());
        Page<Person> page = personDAO.findId(new PageRequest(0, 1), save.getId());
        Page<Person> byAgeLessThan = personDAO.findByAgeLessThan(new PageRequest(0, 10), 10);
        log.error(""+one);
        log.error(page.toString());
        log.error(byAgeLessThan.toString());
    }
}
