package learning.jpaExtend.configurer;

import learning.jpaExtend.extend.aop.TenantContextInsertAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by isaac on 29/07/2017.
 */
@Configuration
@EnableAspectJAutoProxy
public class AopConfigurer {

    @Bean
    public TenantContextInsertAspect contextInsertAop() {
        return new TenantContextInsertAspect();
    }
}