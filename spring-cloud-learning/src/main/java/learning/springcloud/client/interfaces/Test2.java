package learning.springcloud.client.interfaces;

/**
 * Created by isaac on 07/04/2017.
 */
public interface Test2 {
    default boolean canWork(){
        System.err.println("Test2 can work");
        return true;
    }
}
