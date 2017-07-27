package learning.basic;

import learning.basic.classloader.ClassMain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;

/**
 * Created by isaac on 18/04/2017.
 */
@RunWith(JUnit4.class)
public class ClassLoaderTest {
    @Test
    public void test() throws FileNotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException {
//        Class<?> aClass = getClass().getClassLoader().loadClass("learning.basic.classloader.ClassMain");
        Class<ClassMain> classMainClass = ClassMain.class;
//        ClassMain classMain = new ClassMain();
        classMainClass.newInstance();
    }
}
