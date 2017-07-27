package learning.jpaExtend;

import learning.jpaExtend.test.dao.PersonDAO;
import learning.jpaExtend.test.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by isaac on 25/07/2017.
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan
@EnableAsync
@Slf4j
public class Application {
    @Autowired
    private PersonDAO personDAO;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean CommandLineRunner runner2(){
        return (String... args) -> {
            test();
        };
    }

    private void test(){
        Person person = new Person();
        person.setTenantId(1L);
        person.setAge(8);
        person.setName("ppp");
        Person save = personDAO.save(person);
        Person one = personDAO.queryOne(save.getId());
//        Page<Person> byAgeLessThan = personDAO.findByAgeLessThan(new PageRequest(0, 1), 10);
//        log.info(byAgeLessThan.toString());
        Page<Person> page = personDAO.findId(new PageRequest(0, 1), save.getId());
        Page<Person> byAgeLessThan = personDAO.findByAgeLessThan(new PageRequest(0, 10), 10);
        assert(one==null);
        log.error(""+one);
        log.error(page.toString());
        log.error(byAgeLessThan.toString());
    }
}