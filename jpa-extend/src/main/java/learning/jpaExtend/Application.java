package learning.jpaExtend;

import learning.jpaExtend.test.dao.PersonDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
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
}