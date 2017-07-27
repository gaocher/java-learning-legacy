package learning.basic.cache;

import com.google.common.cache.*;

import learning.basic.Data;

import java.util.concurrent.ExecutionException;

/**
 * Created by isaac on 11/05/2017.
 */
public class Main {
    public static void main(String[] args) throws ExecutionException {
        LoadingCache<Long, Data> cache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .removalListener(new RemovalListener<Long, Data>() {

                    @Override
                    public void onRemoval(RemovalNotification<Long, Data> notification) {
                        System.err.println("removing " + notification.getKey()+":"+notification.getValue());
                    }
                })
                .build(
                        new CacheLoader<Long, Data>() {
                            public Data load(Long key) {
                                System.err.println("loading " + key);
                                return new Data("a",key.intValue());
                            }
                        });

        Data data = cache.get(1L);
        Data data2 = cache.get(2L);
        System.out.println(data);
        System.out.println(data2);
        data.setValue(100);
        data.setName("axxx");
        Data data3 = cache.get(1L);
        System.out.println(data3);
        cache.get(4L);

    }
}
