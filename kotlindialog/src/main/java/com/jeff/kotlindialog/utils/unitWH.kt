package com.jeff.kotlindialog.utils

import android.content.Context
import android.util.DisplayMetrics
import org.jetbrains.anko.windowManager

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-12-06.
 * description ： 【Android】 getWidth 、getHight过时替换
 * https://blog.csdn.net/sinat_30220049/article/details/79412589
 */
object unitWH {

//    DisplayMetrics dm = new DisplayMetrics();
//
//    getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//    int width = dm.widthPixels;
//
//    int height = dm.heightPixels;

    //获得当前宽度
    fun getWidth(context: Context): Int {
//        DisplayMetrics dm  = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
        val dm: DisplayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)

        return dm.widthPixels
    }

    fun getHight(context: Context): Int {
//        DisplayMetrics dm  = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
        val dm: DisplayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)

        return dm.heightPixels
    }

}