package learning.web;

import learning.enums.GenderType;

/**
 * Created by isaac on 24/02/2017.
 */
public class RequsetParam {
    private String value;

    private GenderType genderType;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderType genderType) {
        this.genderType = genderType;
    }
}
