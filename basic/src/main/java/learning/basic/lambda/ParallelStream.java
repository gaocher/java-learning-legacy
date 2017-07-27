package learning.basic.lambda;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by isaac on 04/07/2017.
 */
public class ParallelStream {
    public static void main(String[] args){
        List<String> collect = Stream.of(1, 2, 3, 4).parallel().sequential().map(a -> a * 2).peek(System.out::println).map(a -> "x" + a).peek(System.out::println).collect(Collectors.toList());
        System.out.println(collect);
    }
}
