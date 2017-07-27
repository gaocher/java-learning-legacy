package learning.basic;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by isaac on 08/04/2017.
 */
public class Main {

    private Data data = new Data("member",1);

    public void execute(){
//        Data data = new Data("data", 100);
        data.setValue(101);
        data = new Data("reassign",10);
        new Outer(){
            public void calc() {
                System.out.println("hello inner class "+Main.this.toString());
                data = new Data("reassign2",10);
            }

            @Override
            public String toString() {
                return "Annomious Inner class";
            }
        }.calc();
        Outer outer = () -> {
            System.out.println("hello lamda class " + toString());
//            data = new Data("ressing2",12);
            data.setValue(10);
        };
        outer.calc();
        System.out.println(data);
//        return outer;
//        try {
//            shellCommandTest();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        numTest();
    }

    public void shellCommandTest() throws IOException {
        Runtime.getRuntime().exec("/bin/sh /Users/isaac/Workspace/myProjects/learning/basic/src/main/resources/create.sh abc.txt");
    }

    public void ex(){
        final Data data = new Data("tj",100);
//        data = new Data("xx",100);
        data.setValue(101);
    }

    @Override
    public String toString() {
        return "Main{" +
                "data=" + data +
                '}';
    }

    public void numTest(){
        BigDecimal bigDecimal = new BigDecimal(10.3435);
        BigDecimal v = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(v);
    }

    public static void main(String[] args){
        new Main().execute();
    }




}
