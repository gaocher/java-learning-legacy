package learning;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Created by isaac on 24/02/2017.
 */
public class EnumMessageSourceHolder {
    private static ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    static {
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("classpath:i18n/enum-messages");
    }
    public static MessageSource getInstance(){
        return messageSource;
    }
}
