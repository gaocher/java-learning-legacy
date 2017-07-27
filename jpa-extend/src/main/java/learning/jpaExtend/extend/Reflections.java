package learning.jpaExtend.extend;

import java.lang.reflect.Field;

/**
 * Created by isaac on 26/07/2017.
 */
public class Reflections {
    public static Object getField(Object target, String name) {
        return getField(target.getClass(),target,name);
    }

    public static Object getField(Class clazz, Object target, String name) {
        try{
            Field declaredField = clazz.getDeclaredField(name);
            declaredField.setAccessible(true);
            return declaredField.get(target);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void setField(Object target, String name, Object value) {
        try{
            Field declaredField = target.getClass().getDeclaredField(name);
            declaredField.setAccessible(true);
            declaredField.set(target,value);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
