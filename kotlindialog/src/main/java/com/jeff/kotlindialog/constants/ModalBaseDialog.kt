package com.jeff.kotlindialog.constants

import com.jeff.kotlindialog.utils.LogUtils
import java.util.*

/**
 * author : Jeff  5899859876@qq.com
 * Csdn :https://blog.csdn.net/Jeff_YaoJie
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-19.
 * description ：
 */
abstract class ModalBaseDialog: BaseDialog() {
    protected var modalDialogList: List<BaseDialog> = ArrayList()

    //对话框模态化队列
    internal fun showNextModalDialog() {
        LogUtils.i("ModalBaseDialog", "showNextModalDialog: " + modalDialogList.size)
        if ( modalDialogList.size>0){
            modalDialogList[0].doShowDialog()
        }

    }
}