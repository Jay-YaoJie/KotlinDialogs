package com.jeff.kotlindialogs.listener

import android.app.Dialog

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-17.
 * description ： 使用监听器来监听对话框的生命周期
 */
interface DialogLifeCycleL {
    abstract  fun onCreate(alertDialog: Dialog)
    abstract  fun onShow(alertDialog: Dialog)
    abstract  fun onDismiss()
}