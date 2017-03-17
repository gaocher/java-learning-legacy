package learning.web;

import learning.enums.PersonType;

/**
 * Created by isaac on 24/02/2017.
 */
public class ResponseResult {
    private String result;
    private PersonType personType;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "result='" + result + '\'' +
                ", personType=" + personType +
                '}';
    }
}
