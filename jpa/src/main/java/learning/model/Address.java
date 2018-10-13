package learning.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by isaac on 29/03/2017.
 */

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    private String addr;

    @ManyToOne
    private Person person;

    private String uniqueKey;

    @Version
    private Integer version;

    public Address() {
    }

    public Address(String addr) {
        this.addr = addr;
    }

    public void toDto(){
        System.err.println(this.id);
        System.err.println(this.addr);
    }
}
