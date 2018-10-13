package learning.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by isaac on 22/05/2017.
 */
@Entity
@NoArgsConstructor
@Data
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(precision = 16, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @ManyToOne
    private Person person;

    @Version
    private Integer version;

    public Account(String name) {
        this.name = name;
    }

    public void deposit(BigDecimal d){
        amount = amount.add(d);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
