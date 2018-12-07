package com.jeff.kotlindialogs.listener

import android.app.Dialog

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-17.
 * description ：
 */
interface InputDialogOkButtonClickL {
    abstract  fun onClick(dialog: Dialog, inputText: String)
}