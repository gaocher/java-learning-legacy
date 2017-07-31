package learning.jpaExtend.extend.aop;

import learning.jpaExtend.contextAware.TenantContextHolder;
import learning.jpaExtend.extend.TenantContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by isaac on 29/07/2017.
 */
@Aspect
public class TenantContextInsertAspect {
    @Pointcut("@annotation(tenantContext) || @within(tenantContext)")
    public void tenantContext(TenantContext tenantContext) {
    }

    @Before("tenantContext(tenantContext)")
    public void setTenantId(JoinPoint joinPoint, TenantContext tenantContext) {
        Object[] args = joinPoint.getArgs();
        TenantContextHolder.setTenantContext(new learning.jpaExtend.contextAware.TenantContext((Long)args[0]));
    }
}
