package learning;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by isaac on 22/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CurrencyTest {
//    @Autowired
//    private AccountDAO accountDAO;
//
//    @Qualifier("accountService")
//    @Autowired
//    private AccountService accountService;
//
//    @Test
//    public void test(){
//        Account account = accountService.create();
//        accountService.deposit(account.getId(),10);
//
//        Runnable runnable = new Runnable() {
//
//            @Override
//            public void run() {
//                accountService.deposit(account.getId(),10);
//            }
//        };
//
//        List<Thread> threadStream = IntStream.range(0, 5).mapToObj(i -> new Thread(runnable)).collect(Collectors.toList());
//        threadStream.forEach(thread -> {
//            thread.start();
//        });
//
//        threadStream.forEach(t -> {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        System.out.println(accountDAO.findOne(account.getId()));
//    }
}
