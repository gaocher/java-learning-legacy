package learning.thirdparty;

import java.io.Serializable;

/**
 * Created by isaac on 19/04/2017.
 */
public class Vector3d implements Serializable{
    int a;
//    int b;

    Line deed;
    int c;
//    int d;
//    String d;
    Line e;
    Line dd;

//    private Vector3d() {
//    }

    public Vector3d( int a, int b, int c) {
        this.a = a;
//        this.b = b;
        this.c = c;
    }

//    public int getA() {
//        return a;
//    }
//
//    public void setA(int a) {
//        this.a = a;
//    }

//    public int getB() {
//        return b;
//    }
//
//    public void setB(int b) {
//        this.b = b;
//    }
//
//    public int getC() {
//        return c;
//    }
//
//    public void setC(int c) {
//        this.c = c;
//    }

    @Override
    public String toString() {
        return "Vector3d{" +
                "a=" + a +
//                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
