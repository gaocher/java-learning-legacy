package learning.basic.resourcefile;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by isaac on 24/04/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream("learning/basic/resourcefile/abc.sql");
        InputStream pp = Main.class.getClassLoader().getResourceAsStream("a.txt");

        DataInputStream dataInputStream = new DataInputStream(resourceAsStream);
        String s = dataInputStream.readLine();
        System.err.println(s);


        DataInputStream pp2 = new DataInputStream(pp);
        String s2 = pp2.readLine();
        System.err.println(s2);

    }
}
