package model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
/**
 * Created by isaac on 02/03/2017.
 */
@Validated
public class Person {
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}
