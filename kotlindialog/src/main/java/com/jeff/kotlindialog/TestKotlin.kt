package com.jeff.kotlindialog

import com.google.gson.Gson
import java.util.*


/**
 * author : Jeff  5899859876@qq.com
 * Csdn :https://blog.csdn.net/Jeff_YaoJie
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-12-03.
 * description ：
 */
fun main(ager: Array<String>) {
    var players = arrayOf(
        "Rafael Nadal",
        "Novak Djokovic",
        "Stanislas Wawrinka",
        "David Ferrer",
        "Roger Federer",
        "Andy Murray",
        "Tomas Berdych",
        "Juan Martin Del Potro",
        "Richard Gasquet",
        "John Isner"
    )
    Arrays.sort(
        players,
        { s1: String, s2: String ->
            (s1.substring(s1.indexOf(" ")).compareTo(s2.substring(s2.indexOf(" "))))
        }
    )
    System.out.println(Gson().toJson(players))

    var  list:List<String> = ArrayList()
    list+="str1"
    list+="str2"
    list+="str3"
    list+="str4"
    list+="str5"
    list+="str6"
    System.out.println(list.toString())

    list-="str4"
    System.out.println(list.toString())








}



