package com.jeff.kotlindialogs.bottom

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.ViewGroup
import android.view.WindowManager

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-12-04.
 * description ： Android 6.0新控件 BottomSheetDialog | 底部对话框 介绍及使用详情
 *
 * https://blog.csdn.net/lengxuechiwu1314/article/details/72835497
 */
class MyBottomSheetDialog(context: Context) : BottomSheetDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screenHeight = getScreenHeight(context)
        val statusBarHeight = getStatusBarHeight(context)
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            if (screenHeight == 0) ViewGroup.LayoutParams.MATCH_PARENT else screenHeight
        )
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.defaultDisplay.height
    }

}