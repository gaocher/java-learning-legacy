package learning.basic.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by isaac on 09/04/2017.
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Clock clock = Clock.systemUTC();
        print(clock);
        print(clock.millis());
        print(clock.getZone());
        print(clock.instant());

        Thread.sleep(1000);

        print(clock);
        print(clock.millis());
        print(clock.getZone());
        print(clock.instant());

        Clock clock1 = Clock.systemDefaultZone();

        print(clock1);
        print(clock1.millis());
        print(clock1.getZone());
        print(clock1.instant());
        print(clock1.instant().atZone(clock1.getZone()));

        LocalDateTime localDateTime = LocalDateTime.now();

        print(clock.instant());
//        print(localDateTime.toInstant());
//        clock1.instant().
        print(TimeZone.getDefault().toZoneId().getClass());
        print(((ZoneOffset)clock.getZone()).getTotalSeconds());
        print(clock1.getZone().normalized().getClass());
        print(localDateTime);

        Date date = new Date();

        print(clock.instant() + "==" + clock1.instant());
        print(date.toInstant());
        print(date.getTime());
        print(date);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println(dateFormat.format(date));

//        test1();

    }

    public static void test1() throws InterruptedException {
        //时钟提供给我们用于访问某个特定 时区的 瞬时时间、日期 和 时间的。
        Clock c1 = Clock.systemUTC(); //系统默认UTC时钟（当前瞬时时间 System.currentTimeMillis()）
        System.out.println(c1.millis()); //每次调用将返回当前瞬时时间（UTC）

        Clock c2 = Clock.systemDefaultZone(); //系统默认时区时钟（当前瞬时时间）

        Clock c31 = Clock.system(ZoneId.of("Europe/Paris")); //巴黎时区
        System.out.println(c31.millis()); //每次调用将返回当前瞬时时间（UTC）

        Clock c32 = Clock.system(ZoneId.of("Asia/Shanghai"));//上海时区
        System.out.println(c32.millis());//每次调用将返回当前瞬时时间（UTC）

        Clock c4 = Clock.fixed(Instant.now(), ZoneId.of("Asia/Shanghai"));//固定上海时区时钟
        System.out.println(c4.millis());
        Thread.sleep(1000);
        System.out.println(c4.millis()); //不变 即时钟时钟在那一个点不动

        Clock c5 = Clock.offset(c1, Duration.ofSeconds(2)); //相对于系统默认时钟两秒的时钟
        System.out.println(c1.millis());
        System.out.println(c5.millis());
    }

    public static void print(Object obj){
        System.out.println(obj);
    }
}
