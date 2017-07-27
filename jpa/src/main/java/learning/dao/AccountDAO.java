package learning.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import learning.model.Account;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;

/**
 * Created by isaac on 22/05/2017.
 */
public interface AccountDAO extends JpaRepository<Account,Long>{
    @Query
    @Lock(LockModeType.PESSIMISTIC_READ)
    Account findById(Long id);
}
