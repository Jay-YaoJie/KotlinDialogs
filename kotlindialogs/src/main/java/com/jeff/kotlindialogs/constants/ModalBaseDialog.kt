package com.jeff.kotlindialogs.constants

import com.jeff.kotlindialogs.utils.LogUtils
import java.util.*

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-19.
 * description ：
 */
abstract class ModalBaseDialog: BaseDialog() {
    protected var modalDialogList: List<BaseDialog> = ArrayList()         //对话框模态化队列

    protected fun showNextModalDialog() {
        LogUtils.i("ModalBaseDialog", "showNextModalDialog: " + modalDialogList.size)
        modalDialogList[0].doShowDialog()
    }
}