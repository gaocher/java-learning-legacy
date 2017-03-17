package learning.serializer;

import java.io.Serializable;

/**
 * Created by isaac on 24/02/2017.
 */

public class Payload implements Serializable {
    private String code;

    private String message;

    private String clazz;

    public Payload(){}
    public Payload(String code, String message, String clazz){
        this.code = code;
        this.message = message;
        this.clazz = clazz;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}

