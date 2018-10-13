package learning.basic.inheritance;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * Created by isaac on 28/07/2017.
 */
@Slf4j
public class SuperCall {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        B b = new B();
        b.echo();
        ((A)b).echo();
        A.class.getMethod("echo").invoke(b);
        Consumer<B> bConsumer = B::echo;
        bConsumer.accept(b);
        Consumer<A> echo = A::echo;
        echo.accept(b);
    }

    static class A {
        public void echo(){
            System.out.println("A");
        }
    }

    static class B extends A {
        public void echo(){
            System.out.println("B");
        }
    }
}
