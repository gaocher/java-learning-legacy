package learning.jpaExtend.test.service;

import learning.jpaExtend.contextAware.TenantContextHolder;
import learning.jpaExtend.extend.TenantContext;
import org.springframework.stereotype.Service;

/**
 * Created by isaac on 29/07/2017.
 */
@Service
@TenantContext
public class ContextTest {
    public void test(Long tenantId, Object arg){
        System.out.println(arg.toString() + TenantContextHolder.getTenantContext().getTenantId());

    }
}
