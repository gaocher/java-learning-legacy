package learning.jpaExtend.test.model;

import learning.jpaExtend.extend.model.TenantEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by isaac on 27/03/2017.
 */
@Entity
@Data
public class Person extends TenantEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Integer age;

//    @OneToOne(mappedBy = "person",fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
//    private Address address;

    @Version
    private Long version;


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


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
