package com.jeff.kotlindialogs.constants

import com.jeff.kotlindialogs.constants.DialogSettings.LOG_DEBUG
import com.jeff.kotlindialogs.listener.DialogLifeCycleListener
import java.util.*

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-17.
 * description ：baseDialog 全局Dialog   抽象类对象
 */
 abstract class BaseDialog {
    companion object {

        var dialogList: List<BaseDialog> = ArrayList()         //对话框队列
        fun unloadAllDialog() {
            try {
                for (baseDialog in dialogList) {
                    baseDialog.doDismiss()
                }
                dialogList = ArrayList()
            } catch (e: Exception) {
                if (LOG_DEBUG) e.printStackTrace()
            }

        }
    }


    var isDialogShown: Boolean
        get() = false
        set(value) = TODO()

    private var dialogLifeCycleListener: DialogLifeCycleListener? = null


    fun setDialogLifeCycleListener(listener: DialogLifeCycleListener) {
        dialogLifeCycleListener = listener
    }

    fun getDialogLifeCycleListener(): DialogLifeCycleListener? {
        return dialogLifeCycleListener!!
    }

    fun cleanDialogLifeCycleListener() {
        dialogLifeCycleListener = null
    }

    abstract fun showDialog()

    abstract fun doDismiss()


}