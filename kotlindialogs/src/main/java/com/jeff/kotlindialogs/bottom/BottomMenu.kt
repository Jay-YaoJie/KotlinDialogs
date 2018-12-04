package com.jeff.kotlindialogs.bottom

import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.view.textservice.TextInfo
import android.widget.*
import com.jeff.kotlindialogs.bottom.adapters.NormalMenuArrayAdapter
import com.jeff.kotlindialogs.constants.BaseDialog
import com.jeff.kotlindialogs.constants.DialogSettings.TYPE_MATERIAL
import com.jeff.kotlindialogs.constants.DialogSettings.type
import com.jeff.kotlindialogs.widget.BlurView


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
    companion object {
        //这里的方法全可以调用，都使用静态

    }

    private var alertDialog: AlertDialog? = null


    private var bottomSheetDialog: MyBottomSheetDialog? = null
    private var menuArrayAdapter: NormalMenuArrayAdapter? = null

    private var txtTitle: TextView? = null
    private var listMenu: ListView? = null
    private var btnCancel: TextView? = null
    private var boxCancel: ViewGroup? = null
    private var splitLine: ImageView? = null
    private var customView: RelativeLayout? = null;

    private var blurList: BlurView? = null
    private var blurCancel: BlurView? = null

    private var boxList: RelativeLayout? = null
    override fun showDialog() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun doDismiss() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        alertDialog!!.dismiss()
    }

    private var title: String? = null
    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?): BottomMenu {
        this.title = title
        when (type) {
            TYPE_MATERIAL -> if (bottomSheetDialog != null && txtTitle != null) {
                if (title != null && !title.trim { it <= ' ' }.isEmpty()) {
                    txtTitle!!.setText(title)
                    txtTitle!!.setVisibility(View.VISIBLE)
                } else {
                    txtTitle!!.setVisibility(View.GONE)
                }
            }
            else -> if (alertDialog != null && txtTitle != null) {
                if (title != null && !title.trim { it <= ' ' }.isEmpty()) {
                    txtTitle!!.setText(title)
                    txtTitle!!.setVisibility(View.VISIBLE)
                    splitLine!!.setVisibility(View.VISIBLE)
                } else {
                    txtTitle!!.setVisibility(View.GONE)
                }
            }
        }
        if (menuArrayAdapter != null) menuArrayAdapter!!.notifyDataSetChanged()
        return this
    }


    fun setCustomView(view: View?): BottomMenu {
        if (alertDialog != null && view != null) {
            customView!!.setVisibility(View.VISIBLE)
            splitLine!!.setVisibility(View.VISIBLE)
            customView!!.addView(view)
            menuArrayAdapter!!.notifyDataSetChanged()
        }
        return this
    }

    fun setMenuTextInfo(textInfo: TextInfo): BottomMenu {
        customMenuTextInfo = textInfo
        return this
    }

    fun setButtonTextInfo(textInfo: TextInfo): BottomMenu {
        customButtonTextInfo = textInfo
        return this
    }


}


