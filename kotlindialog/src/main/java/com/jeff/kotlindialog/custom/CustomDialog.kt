package com.jeff.kotlindialog.custom

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import com.jeff.kotlindialog.R
import com.jeff.kotlindialog.constants.ModalBaseDialog
import com.jeff.kotlindialog.listener.BindView

/**
 * author : Jeff  5899859876@qq.com
 * CSDN ： https://blog.csdn.net/Jeff_YaoJie
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-12-11.
 * description ：自定义对话框
 * CustomDialog 的创建方式支持通过 layout 资源 ID 或直接使用 View 创建，其也支持对话框的模态化以及生命周期管理。

需要在自定义的对话框中关闭 CustomDialog，您可以在全局接收 CustomDialog 的 show 或 build 返回的组件，并执行其 doDismiss() 方法：
 */
class CustomDialog : ModalBaseDialog() {
    var bindView: BindView? = null

    companion object {
        //  View customView = LayoutInflater.from(context).inflate(layoutResId, null);
        fun show(context: Context, rootView: View): CustomDialog {
            build(context, rootView, null).doShowDialog()
            return dialogValue as CustomDialog
        }


        //  View customView = LayoutInflater.from(context).inflate(layoutResId, null);
        fun show(context: Context, rootView: View, bindView: BindView): CustomDialog {
            build(context, rootView, bindView).doShowDialog()
            return dialogValue as CustomDialog
        }


        @Synchronized
        fun build(context: Context, rootView: View, bindView: BindView?): CustomDialog {
            dialogValue = CustomDialog()
            dialogValue.mContext = context
            (dialogValue as CustomDialog).bindView = bindView
            dialogValue.rootView = rootView
            (dialogValue as CustomDialog).modalDialogList += (dialogValue as CustomDialog)
            return dialogValue as CustomDialog
        }

    }


    override fun doShowDialog() {
        super.doShowDialog()
        isCanCancel = false
        dialogList += dialogValue
        // //对话框模态化队列
        modalDialogList -= dialogValue
        //创建AlertDialog
        mBilder = AlertDialog.Builder(mContext, R.style.lightMode)
        mBilder.setCancelable(isCanCancel)
        mAlertDialog = mBilder.create()
        if (isCanCancel) {
            mAlertDialog!!.setCanceledOnTouchOutside(isCanCancel)
        }
        mAlertDialog!!.setOnDismissListener {
            dialogList -= (dialogValue as CustomDialog)
            rootView = null
            if (dialogLifeCycleL != null) {
                //添加监听
                dialogLifeCycleL!!.onDismiss()
            }
            isDialogShown = false
            if (modalDialogList.isEmpty()) {
                showNextModalDialog()
            }
        }
        mWindow = mAlertDialog!!.window!!

        mAlertDialog!!.show();



        if (dialogLifeCycleL != null) {
            //添加监听
            dialogLifeCycleL!!.onShow(mAlertDialog!!)
        }
        mWindow.setContentView(rootView)
        bindView!!.onBind(rootView!!)
    }

    //设置关闭
    fun setCanCancel(cancCancel: Boolean): CustomDialog {
        isCanCancel = cancCancel
        mAlertDialog!!.setCancelable(cancCancel)
        return this
    }
}