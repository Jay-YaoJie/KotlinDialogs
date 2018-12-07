package com.jeff.kotlindialogs.constants

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.jeff.kotlindialogs.constants.DialogSettings.LOG_DEBUG
import com.jeff.kotlindialogs.info.TInfo
import com.jeff.kotlindialogs.listener.DialogLifeCycleL

import com.jeff.kotlindialogs.utils.LogUtils
import com.jeff.kotlindialogs.widget.BlurView
import java.util.*

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-17.
 * description ：baseDialog 全局Dialog   抽象类对象
 */
 abstract class BaseDialog {

    companion object {
        //保存实例化对象，以便后面使用
        lateinit var dialogValue: BaseDialog;

        var dialogList: List<BaseDialog> = ArrayList()       //对话框队列
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


    internal var isDialogShown: Boolean=false

    //使用监听器来监听对话框的生命周期
    internal  var dialogLifeCycleL: DialogLifeCycleL?=null

    //在这里设置对话打印日志

    //在这里设置对话打印日志
    //internal var tag: String = "DialogSDK>>>>"
    internal fun mLog(msgString: String) {
        LogUtils.d("DialogSDK>>>>", msgString)
    }

    //弹出对话框对象
    internal var mAlertDialog: AlertDialog?=null
    lateinit var mBilder: AlertDialog.Builder
    /* WindowManager windowManager = activity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = display.getWidth();
            //如下显示
            mWindow.attributes.width=(mContext.windowManager.defaultDisplay).width
            */
    lateinit var mWindow: Window;
    lateinit var mContext: Context//当前上下文对象

    //保存信息对象，对话框弹出的值  val mList: List<Int> = listOf(1, 3, 5, 7, 9)
    lateinit var valueListStr: List<String>

    internal var title: String? = null//标题
    internal var cancelButton = "取消"//取消按钮

    //决定对话框按钮文字样式
    internal var dialogButtonTextInfo: TInfo= TInfo()
    //决定菜单文字样式
    internal var menuTextInfo: TInfo=TInfo()

    //决定提示框文本样式
    internal  var tipTextInfo: TInfo = TInfo()

    //决定对话框内容文字样式
    internal var dialogContentTextInfo: TInfo= TInfo()



    lateinit var blur: BlurView
    lateinit var blurList: BlurView
    lateinit var viewGroup: ViewGroup


    lateinit var txtTitle: TextView//标题
    lateinit var imgTitle: ImageView//标题图片
    lateinit var btnCancel: TextView //取消按钮


    lateinit var splitHorizontal: ImageView
    lateinit var btnSelectNegative: TextView
    lateinit var splitVertical: ImageView
    lateinit var btnSelectPositive: TextView

    //当前 view对象
    lateinit var mCustomView: RelativeLayout
    ///自己定义的，要使用RelativeLayout做为首布局文件  传入自定义的布局文view
    internal var cCustomView: RelativeLayout? = null

    internal open fun doShowDialog() {
        mLog("doShowDialog()="+valueListStr.toString())

    }

    internal fun doDismiss() {
            mAlertDialog!!.dismiss()
    }


}