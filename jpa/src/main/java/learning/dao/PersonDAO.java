package learning.dao;

import learning.model.CmpKey;
import learning.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by isaac on 27/03/2017.
 */
public interface PersonDAO extends CustomDAO<Person,Long> {
    Page<Person> findByAgeLessThan(int age, Pageable pageable);

    List<Person> findByVersionAndAgeOrName(Long version,Integer age, String name);

    Person findByKey(CmpKey key);

//    List<Person> findAggregatedPerson(String accountName, Pageable pageable);
}