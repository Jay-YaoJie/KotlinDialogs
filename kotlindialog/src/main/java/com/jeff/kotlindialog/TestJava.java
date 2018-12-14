package com.jeff.kotlindialog;

import java.util.Arrays;
import java.util.Comparator;

import static java.lang.System.out;

/**
 * author : Jeff  5899859876@qq.com
 *Csdn :https://blog.csdn.net/Jeff_YaoJie
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-12-03.
 * description ：
 */
public class TestJava {
  static   String[] players = {"Rafael Nadal", "Novak Djokovic",
            "Stanislas Wawrinka", "David Ferrer",
            "Roger Federer", "Andy Murray",
            "Tomas Berdych", "Juan Martin Del Potro",
            "Richard Gasquet", "John Isner"};

    public static void main( String[] args){
        // 1.1 使用匿名内部类根据 name 排序 players
//        Arrays.sort(players, new Comparator<String>() {
//            @Override
//            public int compare(String s1, String s2) {
//                return (s1.compareTo(s2));
//            }
//        });

        Arrays.sort(players,
                (String s1, String s2) ->
                ( s1.substring(s1.indexOf(" ")).
                        compareTo(
                        s2.substring(s2.indexOf(" "))
                        )
                )
        );


        outTest(players);


        System.out.print("cast="+cast(135463431));



    }
    public static void outTest(String[] players){
        String sts = null;
        for (String string: players) {
            sts+=string+",";

        }
        out.println(sts);
    }


    public static <T> T cast(Object obj) {
        return (T) obj;
    }

}
