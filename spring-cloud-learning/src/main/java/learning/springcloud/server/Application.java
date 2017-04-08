package learning.springcloud.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by isaac on 07/04/2017.
 */
//@EnableEurekaServer
@SpringBootApplication
public class Application {
    @Autowired
    Environment environment;

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner commandLineRunner(){
        return new CommandLineRunner(){

            @Override
            public void run(String... args) throws Exception {
                String property = environment.getProperty("server.port");
                System.err.println(property);
                List<Object> collect = Arrays.asList(1, 2, 3).stream().map(item -> {
                    if (item == 2) {
                        return null;
                    } else {
                        return 2 * item;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
                System.err.println(collect);
            }
        };
    }
}