package com.reactive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

/**
 * Created by isaac on 15/05/2017.
 */
public class Main {
    public static void  main(String[] args) throws InterruptedException {
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");
        Flux<String> map = seq1.log().map(s -> {
            System.out.println(s);
            return s+"0";
        });

        map.subscribe(new Subscriber<String>() {
            private Subscription s = null;
            @Override
            public void onSubscribe(Subscription s) {
                this.s = s;
                s.request(1);
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext"+s);
                this.s.request(1);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });


    }
}
