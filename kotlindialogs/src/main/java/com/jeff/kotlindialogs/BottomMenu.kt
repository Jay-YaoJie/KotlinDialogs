package com.jeff.kotlindialogs

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.textservice.TextInfo
import com.jeff.kotlindialogs.constants.BaseDialog


/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-19.
 * description ：底部菜单

List<String> list = new ArrayList<>();
list.add("菜单1");
list.add("菜单2");
list.add("菜单3");
BottomMenu.show(context, list);
为底部对话框添加标题：
BottomMenu.show(context, list).setTitle("这里是标题测试");
 */
 class BottomMenu : BaseDialog() {
    private var bottomMenu: BottomMenu? = null
    private var menuText: List<String>? = null
    private var alertDialog: AlertDialog? = null
    private var activity: AppCompatActivity? = null
    private var isShowCancelButton = false
    private var onMenuItemClickListener: OnMenuItemClickListener? = null
    private var title: String? = null
    private var cancelButtonCaption = "取消"

    private var customMenuTextInfo: TInfo? = null
    private var customButtonTextInfo: TInfo? = null

    override fun showDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun doDismiss() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}