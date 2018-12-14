package com.jeff.kotlindialog.info

import android.text.InputType

/**
 * author : Jeff  5899859876@qq.com
 * Csdn :https://blog.csdn.net/Jeff_YaoJie
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-19.
 * description ：
 */
class InputInfo {
    private var MAX_LENGTH: Int = 0
    private var inputType: Int = 0          //类型详见 android.text.InputType

    fun getMAX_LENGTH(): Int {
        return MAX_LENGTH
    }

    fun setMAX_LENGTH(MAX_LENGTH: Int): InputInfo {
        this.MAX_LENGTH = MAX_LENGTH
        return this
    }

    fun getInputType(): Int {
        return inputType
    }

    /**
     * 文本输入类型
     * [InputType].
     * @see InputType
     */
    fun setInputType(inputType: Int): InputInfo {
        this.inputType = inputType
        return this
    }
}