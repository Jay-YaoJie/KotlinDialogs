#Kotlin Dialogs

> 对话框工具，Dialog集合工具，引用了V7包的AlertDialog   使用的 kotlin_version = '1.3.11'写的
引用包可以直接拿来使用到项目中

提供最简单的调用方式以实现消息框、选择框、输入框、等待提示、警告提示、完成提示、错误提示等弹出样式。以下是目前包含的所有对话框样式预览图：
![Alt](https://github.com/Jay-YaoJie/Dialogs/blob/master/diagram/Dialogs.png)

底部弹出窗口
![Alt](https://github.com/Jay-YaoJie/Dialogs/blob/master/diagram/BottomMenu.png)

气泡提示
![Alt](https://github.com/Jay-YaoJie/Dialogs/blob/master/diagram/Pop.png)

自定义布局
![Alt](https://github.com/Jay-YaoJie/KotlinDialogs/blob/master/diagram/img_custom_dialog.png)

        //是否打印日志
        // public static boolean DEBUGMODE = true;
        DialogSettings.DEBUGMODE = true;

        //决定等待框、提示框以及iOS风格的对话框是否启用模糊背景
        //public static boolean use_blur = true;

        //决定等待框、提示框以及iOS风格的对话框的模糊背景透明度（50-255）
        // public static int blur_alpha = 200;

        //决定对话框的默认样式，请使用 TYPE_MATERIAL、TYPE_KONGZUE、TYPE_IOS 赋值
        //  public static int type = 0;
        DialogSettings.type = DialogSettings.TYPE_IOS;

        //决定对话框的模式（亮色和暗色两种），请使用 THEME_LIGHT、THEME_DARK 赋值
        //public static int dialog_theme = 0;
        DialogSettings.dialog_theme = DialogSettings.THEME_LIGHT;

        //决定对话框的默认背景色
        // public static int dialog_background_color = -1;

        //决定提示框的模式（亮色和暗色两种），请使用 THEME_LIGHT、THEME_DARK 赋值
        //public static int tip_theme = 1;
        DialogSettings.tip_theme = DialogSettings.THEME_LIGHT;

