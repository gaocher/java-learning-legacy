import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import methods.PersonAccess;
import model.Person;

/**
 * Created by isaac on 01/03/2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ValidationTest {

    @Autowired
    private PersonAccess personAccess;

    @Test
    public void test(){
        Person person = new Person();
        personAccess.print(person);
    }
    @Test
    public void t(){
        personAccess.get(null);
    }
}
