package learning.thirdparty;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.*;

/**
 * Created by isaac on 19/04/2017.
 */
public class FSTTest {
    public static void test() throws Exception {
        FSTTest fstTest = new FSTTest();
//        fstTest.mywriteMethod(new FileOutputStream("FSTTest.ser"), new Vector3d(1,2,3));
        Vector3d vector3d = fstTest.myreadMethod(new FileInputStream("FSTTest.ser"));
        System.out.println(vector3d);
    }

    public Vector3d myreadMethod( InputStream stream ) throws Exception {
        FSTObjectInput in = new FSTObjectInput(stream);
        Vector3d result = (Vector3d)in.readObject(Vector3d.class);
        in.close(); // required !
        return result;
    }

    public void mywriteMethod(OutputStream stream, Vector3d toWrite ) throws IOException
    {
        FSTObjectOutput out = new FSTObjectOutput(stream);
        out.writeObject( toWrite );
        out.close(); // required !
    }
}
