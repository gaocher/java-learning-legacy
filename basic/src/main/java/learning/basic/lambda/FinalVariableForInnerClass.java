package learning.basic.lambda;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * static or member variables are no need to be final in inner class as inner class will refer them
 * for local variables and method parameters, they should be final as these value is copied in inner class
 * Created by isaac on 04/07/2017.
 */
public class FinalVariableForInnerClass {

    public Long a = 10000L;

    public Person p = new Person();

    public static class Person{
        private Integer age;

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    public static void main(String[] args) throws Exception {
        new FinalVariableForInnerClass().test();
    }

    public void print(){

    }
    public void test() throws Exception {

        System.out.println("person address: " + p);
        p.setAge(100);


        FinalVariableForInnerClass main = new FinalVariableForInnerClass() {
            @Override
            public void print() {

                System.out.println("inner's a "+a);
                System.out.println("inner's person address: " + p + " age:" + p.getAge());
                p.setAge(200);

            }
        };
        System.out.println("person address: " + p +" age:" + p.getAge());
//        resetValue();
//        System.out.println("main's a " + a);
        main.print();
//        resetValue();
        a = 4353L;
        p = new Person();

        final Person x = new Person();


        System.out.println("main's a " + a);
        main.print();

    }

    private static void resetValue() throws Exception {
        Field field = FinalVariableForInnerClass.class.getField("a");
        setFinalStatic(field,2312L);
        Field field2 = FinalVariableForInnerClass.class.getField("p");
        setFinalStatic(field2,new Person());
    }

    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }
}
