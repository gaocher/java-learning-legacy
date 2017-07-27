package learning.jpaExtend.test.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import learning.jpaExtend.extend.Tenant;
import learning.jpaExtend.test.model.Person;


/**
 * Created by isaac on 27/03/2017.
 */
public interface PersonDAO extends CustomDAO<Person,Long> {
    @Tenant
    Page<Person> findByAgeLessThan(Pageable pageable, int age);

//    Page<Person> findByAgeLessThanAndId(Pageable pageable, int age, Long id);

    @Tenant
    @Query("select person from Person person where id = ?1")
    Page<Person> findId(Pageable pageable, Long id);


    @Tenant
    @Query("select person from Person person where id = ?1")
    Person queryOne(Long id);
}
