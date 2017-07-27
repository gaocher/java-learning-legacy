package learning.jpaExtend.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by isaac on 22/05/2017.
 */
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(precision = 16, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    public void deposit(BigDecimal d){
        amount = amount.add(d);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
