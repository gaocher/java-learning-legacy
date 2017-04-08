package learning.springcloud.client.interfaces;

/**
 * Created by isaac on 07/04/2017.
 */
public interface TestStrategy {
    default boolean canWork(){
        System.err.println("it's in test work method");
        return true;
    }
}
