package learning.springcloud.client;

import learning.springcloud.client.interfaces.Test2;
import learning.springcloud.client.interfaces.TestStrategy;

/**
 * Created by isaac on 07/04/2017.
 */
public class Application implements TestStrategy, Test2 {
    public static void main(String[] args){
        new Application().canWork();
    }

    @Override
    public boolean canWork() {
        return Test2.super.canWork();
    }

//    @Override
//    public boolean canWork() {
//        System.err.println("in application");
//        return false;
//    }
}
