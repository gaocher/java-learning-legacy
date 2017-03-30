package learning.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import learning.model.Person;

/**
 * Created by isaac on 27/03/2017.
 */
public interface PersonDAO extends JpaRepository<Person,Long> {
}
