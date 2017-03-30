package learning.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by isaac on 29/03/2017.
 */

@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    private String addr;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void toDto(){
        System.err.println(this.id);
    }
}
