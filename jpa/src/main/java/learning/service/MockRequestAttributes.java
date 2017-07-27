package learning.service;

import com.google.common.collect.Maps;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

/**
 * Created by isaac on 25/07/2017.
 */
public class MockRequestAttributes implements RequestAttributes {

    private Map<String,Object> values = Maps.newHashMap();
    @Override
    public Object getAttribute(String name, int scope) {
        return values.get(name);
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {
        values.put(name,value);
    }

    @Override
    public void removeAttribute(String name, int scope) {

    }

    @Override
    public String[] getAttributeNames(int scope) {
        return new String[0];
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback, int scope) {

    }

    @Override
    public Object resolveReference(String key) {
        return null;
    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public Object getSessionMutex() {
        return null;
    }
}
