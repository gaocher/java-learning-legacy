package learning.model;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by isaac on 27/03/2017.
 */
@Entity
@Getter
@Setter
public class Person extends TenantEntity{
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Integer age;

    @OneToOne(mappedBy = "person",fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private Address address;

    @OneToOne(mappedBy = "person",fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private Address eagerAddress;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private List<Account> accounts = Lists.newArrayList();

    @Version
    private Long version;

    @Embedded
    private CmpKey key;


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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void getLazyField(){
        System.err.println(address);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}'+super.toString();
    }
}
