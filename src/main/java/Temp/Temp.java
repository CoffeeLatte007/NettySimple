package Temp;

import java.util.concurrent.ExecutorService;

/**
 * Created by lz on 2016/8/13.
 */
public class Temp<T> {
    public static int n = 0;
    private T a;
    Temp(){
        n++;
    }
    Temp(T a){
        this.a = a;
        n++;
    }
    public static void main(String[] args) {
        Temp<Integer> b = new Temp<Integer>(5);
        System.out.println(b.a);
        Integer ad = 5;
        int bd = ad;
        System.out.println(ad);

//        Temp[] a =new Temp[5];
//        System.out.println(n);
//        String s ="s"+"d";
    }
}
