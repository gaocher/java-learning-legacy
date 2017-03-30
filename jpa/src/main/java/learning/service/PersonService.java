package learning.service;

import learning.dao.PersonDAO;
import learning.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by isaac on 27/03/2017.
 */
@Service
public class PersonService {
    @Autowired
    private PersonDAO personDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Person saveNew(){
        System.err.println("PersonService"+entityManager.toString());
        Person person = new Person();
        person.setAge(11);
        return personDAO.save(person);
    }

    @Async
    public Person asyncSave() throws InterruptedException {
        Thread.sleep(2000L);
        System.err.println(Thread.currentThread().getName() + " PersonService async Save"+entityManager.toString());
        Person person = new Person();
        person.setName("asyncSave");
        Person save = personDAO.save(person);
        return save;
    }
}
