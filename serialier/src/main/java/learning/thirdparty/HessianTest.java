package learning.thirdparty;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import org.springframework.data.domain.PageRequest;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by isaac on 19/04/2017.
 */
public class HessianTest {
    public static void test() throws IOException {
        HessianTest hessianTest = new HessianTest();
        hessianTest.write(new FileOutputStream("FSTTest.ser"), new Vector3d(1,2,3));
        Vector3d vector3d = (Vector3d) hessianTest.read(new FileInputStream("FSTTest.ser"));
        System.out.println(vector3d);
    }

    public static void pageTest() throws IOException {
        HessianTest hessianTest = new HessianTest();
        hessianTest.write(new FileOutputStream("FSTTest.ser"), new PageableRequest(1,2));
        PageRequest vector3d = (PageRequest) hessianTest.read(new FileInputStream("FSTTest.ser"));
        System.out.println(vector3d);
    }

    public static void pageResponseTest() throws IOException {
        HessianTest hessianTest = new HessianTest();
        List<Vector3d> vector3ds = Arrays.asList(new Vector3d(1, 2, 3), new Vector3d(1, 2, 3), new Vector3d(1, 2, 3), new Vector3d(1, 2, 3));

        hessianTest.write(new FileOutputStream("FSTTest.ser"), new PageableResponse<Vector3d>(vector3ds));
        PageableResponse vector3d = (PageableResponse) hessianTest.read(new FileInputStream("FSTTest.ser"));
        System.out.println(vector3d);
        int totalPages = vector3d.getTotalPages();
        System.out.println(totalPages);
        vector3d.getContent().forEach(o -> System.out.println(o));
    }

    public Object read(InputStream inputStream) throws IOException {

        Hessian2Input in = new Hessian2Input(inputStream);

        in.startMessage();

        Object o = in.readObject();

        in.completeMessage();

        in.close();
        return o;
    }

    public void write(OutputStream outputStream,Object vector3d) throws IOException {

        Hessian2Output out = new Hessian2Output(outputStream);

        out.startMessage();

        out.writeObject(vector3d);

        out.completeMessage();

        out.close();
    }
}
