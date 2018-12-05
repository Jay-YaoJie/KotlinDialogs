package com.jeff.kotlindialogs.constants

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.jeff.kotlindialogs.constants.DialogSettings.LOG_DEBUG
import com.jeff.kotlindialogs.info.TInfo
import com.jeff.kotlindialogs.listener.DialogLifeCycleListener
import com.jeff.kotlindialogs.utils.LogUtils
import com.jeff.kotlindialogs.widget.BlurView
import java.util.*

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-17.
 * description ：baseDialog 全局Dialog   抽象类对象
 */
open abstract class BaseDialog {

    companion object {
        //保存实例化对象，以便后面使用
        lateinit var dialogValue: BaseDialog;

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


    internal var isDialogShown: Boolean
        get() = false
        set(value) {
            isDialogShown = value;
        }

    internal var dialogLifeCycleListener: DialogLifeCycleListener?
        get() = dialogLifeCycleListener
        set(value) {
            dialogLifeCycleListener = value
        }

    //在这里设置对话打印日志

    //在这里设置对话打印日志
    //internal var tag: String = "DialogSDK>>>>"
    internal fun mLog(msgString: String) {
        LogUtils.d("DialogSDK>>>>", msgString)
    }

    //弹出对话框对象
    lateinit var mAlertDialog: AlertDialog
    lateinit var mBilder: AlertDialog.Builder
    lateinit var mContext: Context//当前上下文对象

    //保存信息对象，对话框弹出的值  val mList: List<Int> = listOf(1, 3, 5, 7, 9)
    lateinit var valueListStr: ArrayList<String>

    lateinit var title: String//标题
    internal var cancelButton = "取消"//取消按钮

    //决定对话框按钮文字样式
    internal var dialogButtonTextInfo: TInfo
        get() {
            if (dialogButtonTextInfo == null) {
                dialogButtonTextInfo = TInfo()
            }
            return dialogButtonTextInfo
        }
        set(value) {
            dialogButtonTextInfo = value
        }
    //决定菜单文字样式
    internal var menuTextInfo: TInfo
        get() {
            if (menuTextInfo == null) {
                menuTextInfo = TInfo()
            }
            return menuTextInfo
        }
        set(value) {
            menuTextInfo = value
        }
    //决定提示框文本样式
    internal var tipTextInfo: TInfo
        get() {
            if (tipTextInfo == null) {
                tipTextInfo = TInfo()
            }
            return tipTextInfo;
        }
        set(value) {
            tipTextInfo = value
        }
    //决定对话框内容文字样式
    internal var dialogContentTextInfo: TInfo
        get() {
            if (dialogContentTextInfo == null) {
                dialogContentTextInfo = TInfo()
            }
            return dialogContentTextInfo
        }
        set(value) {
            dialogContentTextInfo = value;
        }


    lateinit var blur: BlurView
    lateinit var viewGroup: ViewGroup
    lateinit var txtTitle: TextView//标题

    lateinit var splitHorizontal: ImageView
    lateinit var btnSelectNegative: TextView
    lateinit var splitVertical: ImageView
    lateinit var btnSelectPositive: TextView
    lateinit var btnCancel: TextView //取消按钮
    //自定义view对象
    lateinit var mCustomView: RelativeLayout //自己定义的，要使用RelativeLayout做为首布局文件

    internal open fun doShowDialog() {
        mLog(valueListStr!!.toString())

    }

    internal fun doDismiss() {
        mAlertDialog!!.dismiss()

    }


}