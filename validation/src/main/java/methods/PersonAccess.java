package methods;

import model.Person;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by isaac on 02/03/2017.
 */
@Validated
public class PersonAccess {
    public void print(@NotNull @Valid Person person){
        System.out.println(person);
    }

    public void get(@NotNull @Min(1) Integer i){
        System.out.println(i);
    }
}
