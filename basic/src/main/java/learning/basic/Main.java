package learning.basic;

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
    }

    @Override
    public String toString() {
        return "Main{" +
                "data=" + data +
                '}';
    }

    public static void main(String[] args){
        new Main().execute();
    }

}
