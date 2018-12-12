package com.jeff.kotlindialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.jeff.kotlindialog.bottom.BottomMenu
import com.jeff.kotlindialog.custom.CustomDialog
import com.jeff.kotlindialog.listener.BindView
import com.jeff.kotlindialog.listener.DialogLifeCycleL
import com.jeff.kotlindialog.listener.OnMenuItemClickL
import org.jetbrains.anko.find
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
    open fun botn(v: View) {

        //bottomL(); //底部弹出view

        custom() //自定义view

    }

    //底部弹出view
    fun bottomL(){

//        BottomMenu.show(activity, list, object : OnMenuItemClickL {
//            override fun onClick(text: String, index: Int) {
//                Toast.makeText(activity, "菜单 " + text + " 被点击了", Toast.LENGTH_LONG).show();
//
//
//            }
//        })!!.setTitle("这里是标题测试")//.setCustomView(customView)
        BottomMenu.show(activity, list, object : OnMenuItemClickL {
            override fun onClick(text: String, index: Int) {
                Toast.makeText(activity, "菜单 " + text + " 被点击了", Toast.LENGTH_LONG).show();
            }
        },object : DialogLifeCycleL{

            override fun onShow(alertDialog: Dialog) {
                Toast.makeText(activity, "弹出了", Toast.LENGTH_LONG).show();
            }

            override fun onDismiss() {
                Toast.makeText(activity, "结束了", Toast.LENGTH_LONG).show();
            }
        })
    }
    var  customDialog:CustomDialog?=null;

    //自定义view
    fun  custom(){
       val  customView :View= LayoutInflater.from(activity).inflate(R.layout.layout_custom, null);
        customDialog=  CustomDialog.show(activity,customView, object : BindView {
            override fun onBind(rootView: View) {
              val btnOk=rootView.find<ImageView>(R.id.btn_ok )
                btnOk.onClick {
                    customDialog!!.doDismiss()
                }

            }
        })
    }


}
