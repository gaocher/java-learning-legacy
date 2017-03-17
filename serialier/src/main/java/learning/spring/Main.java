package learning.spring;

import learning.Application;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by isaac on 27/02/2017.
 */
public class Main {
    public static void main(String[] args){
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Application.class);
        Object helloController = annotationConfigApplicationContext.getBean("helloController");
        System.err.println(helloController);
    }
}
