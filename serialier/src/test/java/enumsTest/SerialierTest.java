package enumsTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import learning.enums.GenderType;
import learning.enums.PersonType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static learning.enums.PersonType.Man;


/**
 * Created by isaac on 24/02/2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SerialierTest {

    @Test
    public void test() throws IOException {
        System.out.println("learning");
        ObjectMapper objectMapper = new ObjectMapper();
        print(objectMapper, Man);
        print(objectMapper, GenderType.MALE);
        print(objectMapper, Man);
        print(objectMapper, GenderType.MALE);
//
//        String s = objectMapper.writeValueAsString(Normal.A);
//        Normal normal = objectMapper.readValue(s, Normal.class);
        String s = objectMapper.writeValueAsString(Man);
        PersonType personType = objectMapper.readValue(s, PersonType.class);
        System.out.println("======="+personType);


    }

    private void print(ObjectMapper mapper, Object obj) throws JsonProcessingException {
        String s = mapper.writeValueAsString(obj);
        System.out.println(s);
    }
}
