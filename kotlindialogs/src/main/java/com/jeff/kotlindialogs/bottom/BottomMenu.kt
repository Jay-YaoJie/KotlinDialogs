package com.jeff.kotlindialogs.bottom

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import com.jeff.kotlindialogs.R
import com.jeff.kotlindialogs.bottom.adapters.NormalMenuArrayAdapter
import com.jeff.kotlindialogs.constants.BaseDialog
import com.jeff.kotlindialogs.constants.DialogSettings.TYPE_MATERIAL
import com.jeff.kotlindialogs.constants.DialogSettings.type
import com.jeff.kotlindialogs.listener.OnMenuItemClickListener
import com.jeff.kotlindialogs.widget.BlurView
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor


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
//        fun dip2px(context: Context, dpValue: Float): Int {
//            val scale = context.resources.displayMetrics.density
//            return (dpValue * scale + 0.5f).toInt()
//        }
//        fun show(context: Context, menuText: ArrayList<String>): BottomMenu? {
//
//            return null;
//        }


        @Synchronized
        fun show(
            context: Context,
            menuText: ArrayList<String>?,
            onMenuItemClickListener: OnMenuItemClickListener,
            isShowCancelButton: Boolean,
            cancelButtonCaption: String
        ): BottomMenu {
            // Kotlin 语法 https://www.jianshu.com/p/1ea733ea197d
            synchronized(BottomMenu::class.java) {
                dialogValue = BottomMenu();
                dialogValue.dialogLifeCycleListener = null
                dialogValue.mContext = context;
                dialogValue.valueListStr = menuText!!;
                (dialogValue as BottomMenu).mOnMenuItemClickListener = onMenuItemClickListener;
                dialogValue.isDialogShown = isShowCancelButton;
                dialogValue.cancelButton = cancelButtonCaption
                if (menuText .isEmpty() || menuText.size < 1) {
                    dialogValue.mLog("未启动底部菜单 -> 没有可显示的内容")
                    return (dialogValue as BottomMenu)
                }
                dialogValue.mLog("装载底部菜单 -> " + menuText.toString())
                (dialogValue as BottomMenu).doShowDialog()

            }
            return (dialogValue as BottomMenu);
        }


    }


    private lateinit var bottomSheetDialog: MyBottomSheetDialog
    private lateinit var menuArrayAdapter: NormalMenuArrayAdapter

    private lateinit var mOnMenuItemClickListener: OnMenuItemClickListener

    private lateinit var listMenu: ListView

    private var splitLine: ImageView? = null

    private var blurCancel: BlurView? = null

    private var boxList: RelativeLayout? = null
    //弹出对话框
    override fun doShowDialog() {
        super.doShowDialog()
        //https://blog.csdn.net/Jeff_YaoJie/article/details/84847262
        dialogList += dialogValue
        if (type == TYPE_MATERIAL) {
            bottomSheetDialog = MyBottomSheetDialog(mContext)
            val box_view = LayoutInflater.from(mContext).inflate(R.layout.bottom_menu_material, null)
            listMenu = box_view.find(R.id.list_menu)
            btnCancel = box_view.find(R.id.btn_cancel)
            txtTitle = box_view.find(R.id.title)
            mCustomView = box_view.find(R.id.box_custom)
            if (dialogButtonTextInfo.fontSize > 0) {
                btnCancel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialogButtonTextInfo.fontSize.toFloat())

            }
            if (dialogButtonTextInfo.gravity != -1) {
                btnCancel.setGravity(dialogButtonTextInfo.gravity)
            }
            if (dialogButtonTextInfo.fontColor != -1) {
                btnCancel.textColor = dialogButtonTextInfo.fontColor
            }
            btnCancel.paint.isFakeBoldText = dialogButtonTextInfo.bold
            btnCancel.text = cancelButton
            if (title != null && title!!.trim().isEmpty()) {
                txtTitle.text = title
                txtTitle.visibility = View.VISIBLE
            } else {
                txtTitle.visibility = View.GONE
            }
            //初始化适配器
            menuArrayAdapter = NormalMenuArrayAdapter(mContext, R.layout.item_bottom_menu_material, valueListStr)
            listMenu.adapter = menuArrayAdapter
            // parent, view,  id   参数从未使用过，可以改名为  _  或着在Gradle中 添加retrolambda {
            //    jvmArgs '-noverify'
            //}
            //https://github.com/evant/gradle-retrolambda/issues/105
            listMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                mOnMenuItemClickListener.onClick(valueListStr[position], position)
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            bottomSheetDialog.setContentView(box_view)
            bottomSheetDialog.setCancelable(true)
            bottomSheetDialog.setCanceledOnTouchOutside(true)
            bottomSheetDialog.setOnDismissListener {
                dialogList -= (dialogValue as BottomMenu)
                mCustomView.removeAllViews()
                if (dialogLifeCycleListener != null) {
                    dialogLifeCycleListener!!.onDismiss()
                }
                isDialogShown = false
            }
            if (dialogLifeCycleListener != null) {
                dialogLifeCycleListener!!.onCreate(bottomSheetDialog)
                bottomSheetDialog.show()
            }
            if (dialogLifeCycleListener != null) {
                dialogLifeCycleListener!!.onShow(bottomSheetDialog)
            }

        } else {
            mBilder = AlertDialog.Builder(mContext, R.style.bottom_menu)
            mBilder.setCancelable(true)
            mAlertDialog = mBilder.create()
            mAlertDialog.setCanceledOnTouchOutside(true)
            if (dialogLifeCycleListener != null) {
                dialogLifeCycleListener!!.onCreate(mAlertDialog)
            }

            mAlertDialog.setOnDismissListener {
                //https://blog.csdn.net/Jeff_YaoJie/article/details/84847262
                dialogList -= dialogValue
                //  dialogList.remove(dialogValue)
            }


        }


    }

    //private var title: String? = null
//    fun getTitle(): String? {
//
//        return title
//    }
//
    //设置标题
//    fun setTitle(title: String): BottomMenu {
//        this.title = title
//        when (type) {
//            TYPE_MATERIAL -> if (bottomSheetDialog != null && txtTitle != null) {
//                if (title != null && !title.trim { it <= ' ' }.isEmpty()) {
//                    txtTitle!!.setText(title)
//                    txtTitle!!.setVisibility(View.VISIBLE)
//                } else {
//                    txtTitle!!.setVisibility(View.GONE)
//                }
//            }
//            else -> if (mAlertDialog != null && txtTitle != null) {
//                if (title != null && !title.trim { it <= ' ' }.isEmpty()) {
//                    txtTitle!!.setText(title)
//                    txtTitle!!.setVisibility(View.VISIBLE)
//                    splitLine!!.setVisibility(View.VISIBLE)
//                } else {
//                    txtTitle!!.setVisibility(View.GONE)
//                }
//            }
//        }
//        if (menuArrayAdapter != null) menuArrayAdapter!!.notifyDataSetChanged()
//        return this
//    }

//
//    fun setCustomView(view: View?): BottomMenu {
//        if (mAlertDialog != null && view != null) {
//            mCustomView!!.setVisibility(View.VISIBLE)
//            splitLine!!.setVisibility(View.VISIBLE)
//            mCustomView!!.addView(view)
//            menuArrayAdapter!!.notifyDataSetChanged()
//        }
//        return this
//    }


}





