package learning.dao;

import learning.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by isaac on 27/03/2017.
 */
public interface PersonDAO extends CustomDAO<Person,Long> {
    Page<Person> findByAgeLessThan(int age, Pageable pageable);
}
