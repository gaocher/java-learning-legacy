package learning;

import learning.dao.PageableRequest;
import learning.dao.PersonDAO;
import learning.dao.TenantJpaRepository;
import learning.service.CommonService;
import learning.service.MockRequestAttributes;
import learning.service.PersonService;
import learning.service.multiTenant.AsyncService;
import learning.service.multiTenant.TenantTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.stream.LongStream;

import static learning.service.multiTenant.TenantTask.TENANT_TASK;

/**
 * Created by isaac on 27/03/2017.
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan
@EnableAsync
@EnableJpaRepositories(repositoryBaseClass = TenantJpaRepository.class)
@Slf4j
public class Application {
    @Autowired
    private PersonDAO  personDAO;
    @Autowired
    private PersonService personService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private AsyncService asyncService;

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }


//    @Bean
//    public CommandLineRunner runner(){
//        return new CommandLineRunner() {
////            @Transactional
//            @Override
//            public void run(String... args) throws Exception {
//                log.info("command line runner");
//                final Person person = new Person();
//                person.setName("2xfehi");
//                personDAO.save(person);
//                System.err.println(person);
//                final Person person1 = personService.saveNew();
//                log.info(Thread.currentThread().getName()+person1);
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        try {
////                            commonService.outSave();
////                        }catch (Exception e){
////
////                        }
////                        personDAO.findOne(2L);
////                        person1.setName("in2 thread");
////                        personDAO.save(person1);
////                    }
////                }).start();
//                personService.asyncSave();
////                Thread.sleep(1000);
//                multiTenantRun();
//            }
//        };
//    }

    @Bean CommandLineRunner runner2(){
        return (String... args) -> {
            personDAO.findByAgeLessThan(10,new PageableRequest(0,1));
        };
    }

    private void setContext(TenantTask tenantTask){
        MockRequestAttributes attributes = new MockRequestAttributes();
        attributes.setAttribute(TENANT_TASK,tenantTask,0);
        RequestContextHolder.setRequestAttributes(attributes,true);
    }

    private void multiTenantRun(){
        LongStream.range(1,2).forEach(i -> {
            new Thread(()->{
                TenantTask tenantTask = new TenantTask();
                tenantTask.setTenantId(i);
                tenantTask.setTaskName(Thread.currentThread().getName() + " [id:"+i+"]");
                setContext(tenantTask);
                asyncService.asynRun(tenantTask);
                Object attribute = RequestContextHolder.getRequestAttributes().getAttribute(TENANT_TASK,0);
                log.info(attribute.toString());
            }).start();
        });
    }

}

