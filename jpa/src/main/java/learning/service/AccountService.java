package learning.service;

import learning.dao.AccountDAO;
import learning.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by isaac on 22/05/2017.
 */
@Qualifier("accountService")
@Service
public class AccountService extends ActionHandler {
    @Autowired
    private AccountDAO accountDAO;

    public Account create(){
        Account account = new Account();
        return accountDAO.save(account);
    }

    @Transactional
    public Account deposit(Long id, int amount){
        try {
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Account one = accountDAO.findById(id);
        one.deposit(new BigDecimal(amount));
        return one;
    }

    @Transactional
    public void doExecute() {
        create();
    }
}
