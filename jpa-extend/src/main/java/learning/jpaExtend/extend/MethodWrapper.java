package learning.jpaExtend.extend;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by isaac on 26/07/2017.
 */
@Slf4j
public class MethodWrapper {
    private Method method;

    public MethodWrapper(Method method){
        this.method = method;
    }

    public Method getTenantMethod() {
        try {
            return copyToNewMethod();
        } catch (Exception e) {
            log.error("got exception", e);
            throw new RuntimeException(e);
        }
    }


    private Method copyToNewMethod() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Class<?>  clazz = getFieldValue("clazz",Class.class);
        String name = getFieldValue("name",String.class);
        Class<?>[] parameterTypes = getFieldValue("parameterTypes",Class[].class);
        Class<?> returnType = getFieldValue("returnType",Class.class);
        Class<?>[]  exceptionTypes = getFieldValue("exceptionTypes",Class[].class);
        int  modifiers = getFieldValue("modifiers",int.class);
        int  slot = getFieldValue("slot",int.class);
        String  signature = getFieldValue("signature",String.class);
        byte[]  annotations = getFieldValue("annotations",byte[].class);
        byte[]  parameterAnnotations = getFieldValue("parameterAnnotations",byte[].class);
        byte[]  annotationDefault = getFieldValue("annotationDefault",byte[].class);

        parameterTypes = extendParameterTypes(parameterTypes);
        signature = extendSignature(signature);
//
//        setFieldValue("parameterTypes",parameterTypes);
//        setFieldValue("signature",signature);
//        return method;

        Constructor<Method> constructor = Method.class.getDeclaredConstructor(Class.class, String.class, Class[].class, Class.class, Class[].class, int.class, int.class, String.class, byte[].class, byte[].class, byte[].class);
        constructor.setAccessible(true);
        return constructor.newInstance(clazz, name, parameterTypes, returnType, exceptionTypes, modifiers, slot, signature, annotations, parameterAnnotations, annotationDefault);
    }

    private Class<?>[] extendParameterTypes(Class<?>[] parameterTypes){
        ArrayList<Class<?>> classes = Lists.newArrayList(parameterTypes);
        classes.add(Long.class);
        return classes.toArray(new Class<?>[classes.size()]);
    }

    private String extendSignature(String sig){
        if(sig!=null){
            sig = sig.replace(")", "Ljava/lang/Long;)");
//            (Lorg/springframework/data/domain/Pageable;I)Lorg/springframework/data/domain/Page<Llearning/jpaExtend/test/model/Person;>;
//            (Lorg/springframework/data/domain/Pageable;ILjava/lang/Long;)Lorg/springframework/data/domain/Page<Llearning/jpaExtend/test/model/Person;>;
//            (Lorg/springframework/data/domain/Pageable;Ljava/lang/Long;)Lorg/springframework/data/domain/Page<Llearning/jpaExtend/test/model/Person;>;
        }
        return sig;
    }


    private <T> T getFieldValue(String fieldName, Class<T> fieldType) throws NoSuchFieldException, IllegalAccessException {
        Field f = method.getClass().getDeclaredField(fieldName); //NoSuchFieldException
        f.setAccessible(true);
        return (T) f.get(method); //IllegalAccessException
    }

    private void setFieldValue(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = method.getClass().getDeclaredField(fieldName); //NoSuchFieldException
        f.setAccessible(true);
        f.set(method,value); //IllegalAccessException
    }
}
