package com.jeff.kotlindialog.constants

import android.view.textservice.TextInfo
import com.jeff.kotlindialog.info.TInfo

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-17.
 * description ：  全局属性   var是一个可变变量，val是一个只读变量相当于java中的final变量。
 * 组件启用前请先初始化全局的风格样式，具体方法为
DialogSettings.type = TYPE_MATERIAL;
Material 风格对应 DialogSettings.TYPE_MATERIAL，

Kongzue 风格对应 DialogSettings.TYPE_KONGZUE，

iOS 风格对应 DialogSettings.TYPE_IOS

需要注意的是风格设置仅针对对话框，提示框样式不会改变。

要启用 Light & Dark 黑白主题模式，请调用以下语句实现：
DialogSettings.tip_theme = THEME_LIGHT;         //设置提示框主题为亮色主题
DialogSettings.dialog_theme = THEME_DARK;       //设置对话框主题为暗色主题
 */
object DialogSettings {
    //全局属性   var是一个可变变量，val是一个只读变量相当于java中的final变量。
    //不可变的变量
    const val APP_NAME: String = "kotlinDialogs"
    const val THEME_LIGHT = 0
    const val THEME_DARK = 1

    const val TYPE_MATERIAL = 0
    const val TYPE_KONGZUE = 1
    const val TYPE_IOS = 2
    /************ 可改变的值声名所以不用添加  const 关键字   ***********************/

    //是否打印日志
    //var DEBUGMODE = true
//保存根目录文件地址
    var ROOT_DIR: String = "";//在Application初始化中赋值，其他地方直接调用即可

    //CRASH保存的异常文件目录和文件名称
    var CRASH_FILE_PATH: String? = "${ROOT_DIR}/${DialogSettings.APP_NAME}/crash/";//保存文件地址，有可能为空
    var CRASH_FILE_NAME: String = ".crash";//保存的异常文件名为.crash
    var CRASH_SAVESD: Boolean = true;//true打打印到机器则为调试，false则保存到本地

    //LOG保存平常打印的信息文件地址和文件名称
    var LOG_FILE_PATH: String? = "${ROOT_DIR}/${DialogSettings.APP_NAME}/log/";//保存文件地址，有可能为空
    var LOG_FILE_NAME: String = ".log";//保存的异常文件名为.log
    var LOG_SAVESD: Boolean = false;//是否保存到文件//是否存log到sd卡
    var LOG_DEBUG: Boolean = true;//保存是否开启打印模式//是否打印log


    //此方法用于关闭所有已加载的 Dialog，防止 WindowLeaked 错误，建议将它加在你的 Activity 的 onDestroy() 里调用
    fun unloadAllDialog() {
        BaseDialog.unloadAllDialog()
    }

    //决定等待框、提示框以及iOS风格的对话框是否启用模糊背景
    var use_blur = true

    //决定等待框、提示框以及iOS风格的对话框的模糊背景透明度（50-255）
    var blur_alpha = 200

    //决定对话框的默认样式，请使用 TYPE_MATERIAL、TYPE_KONGZUE、TYPE_IOS 赋值
    var type = 0

    //决定对话框的模式（亮色和暗色两种），请使用 THEME_LIGHT、THEME_DARK 赋值
    var dialog_theme = 0

    //决定对话框的默认背景色
    var dialog_background_color = -1

    //决定提示框的模式（亮色和暗色两种），请使用 THEME_LIGHT、THEME_DARK 赋值
    var tip_theme = 1


    /*
     *  文字大小设定
     *  注意，此值必须大于0才生效，否则使用默认值。另外，我们使用的是dp单位，非sp单位，若有特殊需要请自行转换
     *  另外，暂时不支持Material风格对话框设定字体大小
     */

    //决定对话框标题文字样式
    var dialogTitleTextInfo = TInfo()

    //决定对话框内容文字样式
    var dialogContentTextInfo = TInfo()

    //决定对话框按钮文字样式
    var dialogButtonTextInfo = TInfo()

    //决定对话框积极按钮（一般为确定按钮）文字样式，若未设置此样式则会使用 dialogButtonTextInfo 代替
    var dialogOkButtonTextInfo: TextInfo? = null

    //决定提示框文本样式
    var tipTextInfo = TInfo()


    //决定 Notification 默认文字样式信息
    var notificationTextInfo = TInfo()

    //决定输入框输入文本字样大小（单位：dp），当值<=0时使用默认大小
    var dialog_input_text_size = 0

    //决定对话框组件默认是否可点击遮罩区域关闭
    var dialog_cancelable_default = false


}