package learning.service.multiTenant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import static learning.service.multiTenant.TenantTask.TENANT_TASK;

/**
 * Created by isaac on 25/07/2017.
 */
@Slf4j
@Service
public class AsyncService {
    @Async
    public void asynRun(TenantTask tenantTask){
        Object attribute = RequestContextHolder.getRequestAttributes().getAttribute(TENANT_TASK,0);
        assert(tenantTask.equals(attribute));
        log.info(((TenantTask)attribute).getTaskName());
    }
}
