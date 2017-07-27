package learning.dao;

import learning.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by isaac on 16/05/2017.
 */
public interface AddressDAO extends JpaRepository<Address,Long> {
    Address findByUniqueKey(String key);
}
