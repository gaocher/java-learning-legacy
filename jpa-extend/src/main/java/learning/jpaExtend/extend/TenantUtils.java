package learning.jpaExtend.extend;

import learning.jpaExtend.contextAware.TenantContextHolder;
import learning.jpaExtend.extend.exception.TenantContextNotFoundException;
import learning.jpaExtend.extend.model.TenantJpaRepository;
import org.aopalliance.intercept.MethodInvocation;
import org.assertj.core.util.Lists;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by isaac on 28/07/2017.
 */
public class TenantUtils {
    public static boolean isTenantMethod(Method method){
        if (TenantJpaRepository.class.isAssignableFrom(method.getDeclaringClass())){
            return true;
        }
        Tenant annotation = method.getAnnotation(Tenant.class);
        if(annotation == null){
          return method.getDeclaringClass().getAnnotation(Tenant.class) != null;
        } else{
            return true;
        }
    }

    public static Object[] extendArgs(MethodInvocation invocation){
        return extendArgs(invocation.getArguments(),invocation.getMethod());
    }

    public static Object[] extendArgs(Object[] args, Method method){
        if (!isTenantMethod(method)){
            return args;
        } else {
            return extendArgs(args);
        }
    }

    public static Object[] extendArgs(Object[] args){
        Long tenantId = TenantContextHolder.getTenantId();
        if(tenantId == null){
            throw new TenantContextNotFoundException();
        }
        ArrayList<Object> objects = Lists.newArrayList(args);
        objects.add(tenantId);
        return objects.toArray(new Object[objects.size()]);
    }

}
