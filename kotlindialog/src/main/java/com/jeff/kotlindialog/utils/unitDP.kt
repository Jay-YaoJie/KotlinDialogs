package com.jeff.kotlindialog.utils

import android.content.Context

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-12-04.
 * description ：dip值到pix值转换：dip2pix，dip2px，dp2px实
 *
 * https://blog.csdn.net/zhangphil/article/details/80613879?utm_source=blogxgwz5
 */
object unitDP {


    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


}