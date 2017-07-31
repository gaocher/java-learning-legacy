package learning.jpaExtend.test;

import learning.jpaExtend.Application;
import learning.jpaExtend.test.service.ContextTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by isaac on 29/07/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class TenantContextAopTest {
    @Autowired
    private ContextTest contextTest;
    @Test
    public void test(){
        contextTest.test(11L,"xxx");
    }
}
