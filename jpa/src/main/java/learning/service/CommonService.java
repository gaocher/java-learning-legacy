package learning.service;

import learning.dao.PersonDAO;
import learning.model.Address;
import learning.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by isaac on 27/03/2017.
 */
@Service
public class CommonService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PersonDAO personDAO;

    @Transactional
    public Person save(){
        System.err.println(entityManager.toString());
        Person person = new Person();
        person.setName("common");
        System.err.println("start to save");
        Person save = personDAO.save(person);
        System.err.println("end to save" + save);
        return save;
    }


    @Transactional
    public Person outSave(){
        Person person2 = save();
        System.err.println(Thread.currentThread().getName()+person2);
        Person one = personDAO.findOne(2L);
        System.err.println(Thread.currentThread().getName()+one);
        throw new IllegalArgumentException("run");
    }

    @Transactional
    public Person lazySave(){
        Person person = new Person();
        Address address = new Address();
        person.setAddress(address);
        person.setName("isaac");
        address.setAddr("youyou century sqaure");
        return personDAO.save(person);
    }

    @Transactional
    public Address findAddress(Long personId){
        Person one = personDAO.findOne(personId);
        Address address = one.getAddress();
        address.toDto();
        return address;
    }
}