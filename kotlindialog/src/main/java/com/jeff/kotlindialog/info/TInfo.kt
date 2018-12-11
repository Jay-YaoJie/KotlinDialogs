package com.jeff.kotlindialog.info

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-17.
 * description ：
 */
 class TInfo {

    internal var fontSize = -1      //字号大小，值为-1时使用默认样式，单位：dp
    internal var gravity = -1       //对齐方式，值为-1时使用默认样式，取值可使用Gravity.CENTER等对齐方式
    internal var fontColor = -1     //文字颜色，值为-1时使用默认样式，取值可以用Color.rgb(r,g,b)等方式获取
    internal var bold = false   //是否粗体

    fun getFontSize(): Int {
        return fontSize
    }

    fun setFontSize(fontSize: Int): TInfo {
        this.fontSize = fontSize
        return this
    }

    fun getGravity(): Int {
        return gravity
    }

    fun setGravity(gravity: Int): TInfo {
        this.gravity = gravity
        return this
    }

    fun getFontColor(): Int {
        return fontColor
    }

    fun setFontColor(fontColor: Int): TInfo {
        this.fontColor = fontColor
        return this
    }

    fun isBold(): Boolean {
        return bold
    }

    fun setBold(bold: Boolean): TInfo {
        this.bold = bold
        return this
    }
}