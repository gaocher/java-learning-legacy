package learning.dao;

import learning.model.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Created by isaac on 22/05/2017.
 */
public interface AccountDAO extends JpaRepository<Account,Long>{
    @Query
    @Lock(LockModeType.PESSIMISTIC_READ)
    Account findById(Long id);

    List<Account> findByPersonId(Long personId);

    @Query("select sum(account.amount), account.person.id, max(version) from Account account group by account.person.id")
    List<?> aggregatedQuery(Pageable pageable);
}
