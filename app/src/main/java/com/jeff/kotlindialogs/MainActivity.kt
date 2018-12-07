package com.jeff.kotlindialogs

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jeff.kotlindialogs.bottom.BottomMenu
import com.jeff.kotlindialogs.listener.DialogLifeCycleL
import com.jeff.kotlindialogs.listener.OnMenuItemClickL
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {
    lateinit var activity: AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        list += "菜单0"
        list += "菜单1"
        list += "菜单2"
        list += "菜单3"

    }
    //底部弹出菜单
    var list: List<String> = ArrayList()

    lateinit var customView: View
    //此处使用的是来自com.jeff.kotlindialogs.bottom 的 BottomMenu 类。
    open fun bottom(v: View) {

        BottomMenu.show(activity, list, object : OnMenuItemClickL {
            override fun onClick(text: String, index: Int) {
                Toast.makeText(activity, "菜单 " + text + " 被点击了", Toast.LENGTH_LONG).show();


            }
        })!!.setTitle("这里是标题测试")//.setCustomView(customView)


        bottomL();
    }



    fun bottomL(){
        BottomMenu.show(activity, list, object : OnMenuItemClickL {
            override fun onClick(text: String, index: Int) {
                Toast.makeText(activity, "菜单 " + text + " 被点击了", Toast.LENGTH_LONG).show();
            }
        },object : DialogLifeCycleL{
            override fun onCreate(alertDialog: Dialog) {

                Toast.makeText(activity, "创建好了", Toast.LENGTH_LONG).show();
            }

            override fun onShow(alertDialog: Dialog) {
                Toast.makeText(activity, "弹出了", Toast.LENGTH_LONG).show();
            }

            override fun onDismiss() {
                Toast.makeText(activity, "结束了", Toast.LENGTH_LONG).show();
            }
        })
    }


}
