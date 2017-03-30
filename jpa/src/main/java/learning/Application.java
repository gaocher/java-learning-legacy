package learning;

import learning.dao.PersonDAO;
import learning.model.Person;
import learning.service.CommonService;
import learning.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by isaac on 27/03/2017.
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan
@EnableAsync
public class Application {
    @Autowired
    private PersonDAO  personDAO;
    @Autowired
    private PersonService personService;

    @Autowired
    private CommonService commonService;

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }


//    @Bean
    public CommandLineRunner runner(){
        return new CommandLineRunner() {
//            @Transactional
            @Override
            public void run(String... args) throws Exception {
                System.err.println("command line runner");
                final Person person = new Person();
                person.setName("2xfehi");
                personDAO.save(person);
                System.err.println(person);
                final Person person1 = personService.saveNew();

                System.err.println(Thread.currentThread().getName()+person1);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            commonService.outSave();
//                        }catch (Exception e){
//
//                        }
//                        personDAO.findOne(2L);
//                        person1.setName("in2 thread");
//                        personDAO.save(person1);
//                    }
//                }).start();
                personService.asyncSave();
//                Thread.sleep(1000);
            }
        };
    }





}

