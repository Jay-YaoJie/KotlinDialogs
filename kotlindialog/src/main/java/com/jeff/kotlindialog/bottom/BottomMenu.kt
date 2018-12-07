package com.jeff.kotlindialogs.bottom

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.util.Half.toFloat
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import com.jeff.kotlindialog.R
import com.jeff.kotlindialogs.bottom.adapters.IOSMenuArrayAdapter
import com.jeff.kotlindialogs.bottom.adapters.NormalMenuArrayAdapter
import com.jeff.kotlindialogs.constants.BaseDialog
import com.jeff.kotlindialogs.constants.DialogSettings.TYPE_IOS
import com.jeff.kotlindialogs.constants.DialogSettings.TYPE_KONGZUE
import com.jeff.kotlindialogs.constants.DialogSettings.TYPE_MATERIAL
import com.jeff.kotlindialogs.constants.DialogSettings.blur_alpha
import com.jeff.kotlindialogs.constants.DialogSettings.type
import com.jeff.kotlindialogs.constants.DialogSettings.use_blur
import com.jeff.kotlindialogs.listener.DialogLifeCycleL
import com.jeff.kotlindialogs.listener.OnMenuItemClickL
import com.jeff.kotlindialogs.utils.unitWH
import com.jeff.kotlindialogs.widget.BlurView
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onItemClick
import org.jetbrains.anko.textColor
import java.util.*


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

        fun show(context: Context, menuText: List<String>): BottomMenu? {
            return show(context, menuText, null);
        }

        fun show(context: Context, menuText: List<String>, onMenuItemClickListener: OnMenuItemClickL?): BottomMenu? {
            return show(context, menuText, onMenuItemClickListener, true, "取消", null);
        }

        fun show(context: Context, menuText: List<String>, onMenuItemClickListener: OnMenuItemClickL?, dialogLifeCycleL: DialogLifeCycleL?): BottomMenu? {
            return show(context, menuText, onMenuItemClickListener, true, "取消", dialogLifeCycleL);
        }

        fun show(context: Context, menuText: List<String>, onMenuItemClickListener: OnMenuItemClickL?, isShowCancelButton: Boolean): BottomMenu? {
            return show(context, menuText, onMenuItemClickListener, isShowCancelButton, "取消", null);
        }

        fun show(context: Context, menuText: List<String>, onMenuItemClickListener: OnMenuItemClickL?, isShowCancelButton: Boolean, dialogLifeCycleL: DialogLifeCycleL?): BottomMenu? {
            return show(context, menuText, onMenuItemClickListener, isShowCancelButton, "取消", dialogLifeCycleL);
        }


        @Synchronized
        fun show(
                context: Context,
                menuText: List<String>,
                onMenuItemClickListener: OnMenuItemClickL?,
                isShowCancelButton: Boolean,
                cancelButtonCaption: String,
                dialogLifeCycleL: DialogLifeCycleL?
        ): BottomMenu {
            // Kotlin 语法 https://www.jianshu.com/p/1ea733ea197d
            synchronized(BottomMenu::class.java) {
                dialogValue = BottomMenu();
                dialogValue.mContext = context;
                dialogValue.valueListStr = menuText;
                (dialogValue as BottomMenu).mOnMenuItemClickListener = onMenuItemClickListener;
                dialogValue.isDialogShown = isShowCancelButton;
                dialogValue.cancelButton = cancelButtonCaption
                if (dialogLifeCycleL != null) {
                    dialogValue.dialogLifeCycleL = dialogLifeCycleL//添加监听生命周期
                }

                if (menuText.isEmpty() || menuText.size < 1) {
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
    private var mOnMenuItemClickListener: OnMenuItemClickL? = null
    private lateinit var listMenu: ListView

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
            cCustomView = box_view.find(R.id.box_custom)
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
            menuArrayAdapter = NormalMenuArrayAdapter(mContext, R.layout.item_bottom_menu_material, valueListStr, menuTextInfo)
            listMenu.adapter = menuArrayAdapter
            // parent, view,  id   参数从未使用过，可以改名为  _  或着在Gradle中 添加retrolambda {
            //    jvmArgs '-noverify'
            //}
            //https://github.com/evant/gradle-retrolambda/issues/105
            listMenu.onItemClick { _, _, position, _ ->
                mOnMenuItemClickListener!!.onClick(valueListStr[position], position)
                mLog("当前点击了valueListStr[position]="+valueListStr[position]+"---position="+position)
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            bottomSheetDialog.setContentView(box_view)
            bottomSheetDialog.setCancelable(true)
            bottomSheetDialog.setCanceledOnTouchOutside(true)
            bottomSheetDialog.setOnDismissListener {
                dialogList -= (dialogValue as BottomMenu)
                cCustomView!!.removeAllViews()
                if (dialogLifeCycleL != null) {
                    dialogLifeCycleL!!.onDismiss()
                }

                isDialogShown = false
            }
            bottomSheetDialog.show()
            if (dialogLifeCycleL != null) {
                dialogLifeCycleL!!.onShow(bottomSheetDialog)
            }


        } else {
            mBilder = AlertDialog.Builder(mContext, R.style.bottom_menu)
            mBilder.setCancelable(true)
            mAlertDialog = mBilder.create()
            if (mAlertDialog == null) {
                mLog("当前 AlertDialog 对象为空")
                return;
            }
            mAlertDialog!!.setCanceledOnTouchOutside(true)
            // dialogLifeCycleL!!.onCreate(mAlertDialog!!)
            mAlertDialog!!.setOnDismissListener {
                //https://blog.csdn.net/Jeff_YaoJie/article/details/84847262
                dialogList -= dialogValue //remove
                //  dialogList.remove(dialogValue)
                cCustomView!!.removeAllViews()
                dialogLifeCycleL!!.onDismiss()
                isDialogShown = false
            }
            mAlertDialog!!.show()
            mWindow = mAlertDialog!!.window!!
            mWindow.setGravity(Gravity.BOTTOM)
            /*  WindowManager windowManager = activity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = display.getWidth();*/
            mWindow.attributes.width = unitWH.getWidth(mContext)// (mContext.windowManager.defaultDisplay).width
            mWindow.setWindowAnimations(R.style.bottomMenuAnimStyle)
            var resId = R.layout.bottom_menu_kongzue
            var item_resId = R.layout.item_bottom_menu_kongzue
            when (type) {
                TYPE_KONGZUE -> {
                    resId = R.layout.bottom_menu_kongzue
                    item_resId = R.layout.item_bottom_menu_kongzue
                }
                TYPE_IOS -> {
                    resId = R.layout.bottom_menu_ios
                    item_resId = R.layout.item_bottom_menu_ios
                }
            }
            mWindow.setContentView(resId)
            listMenu = mWindow.findViewById(R.id.list_menu)
            btnCancel = mWindow.findViewById(R.id.btn_cancel)
            txtTitle = mWindow.findViewById(R.id.title)
            imgTitle = mWindow.findViewById(R.id.title_split_line)
            cCustomView = mWindow.findViewById(R.id.box_custom)
            if (title.isNullOrEmpty()) {
                txtTitle.visibility = View.GONE
                imgTitle.visibility = View.GONE
            } else {
                txtTitle.text = title
                txtTitle.visibility = View.VISIBLE
                imgTitle.visibility = View.VISIBLE
            }
            if (dialogButtonTextInfo.fontSize > 0) {
                btnCancel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialogButtonTextInfo.fontSize.toFloat())
            }
            if (dialogButtonTextInfo.gravity != -1) {
                btnCancel.gravity = dialogButtonTextInfo.gravity
            }
            if (dialogButtonTextInfo.fontColor != -1) {
                btnCancel.setTextColor(dialogButtonTextInfo.fontColor)
            }
            btnCancel.paint.isFakeBoldText = dialogButtonTextInfo.bold
            btnCancel.text = cancelButton
            when (type) {
                TYPE_KONGZUE -> {
                    viewGroup = mWindow.findViewById(R.id.box_cancel) as LinearLayout
                }
                TYPE_IOS -> {
                    mCustomView = mWindow.findViewById(R.id.box_list)
                    viewGroup = mWindow.findViewById(R.id.box_cancel) as RelativeLayout
                    if (use_blur) {
                        //决定等待框、提示框以及iOS风格的对话框是否启用模糊背景
                        mCustomView.post {
                            blurList = BlurView(mContext, null)
                            val params =
                                    RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mCustomView.height)
                            blurList.setOverlayColor(Color.argb(blur_alpha, 255, 255, 255))
                            blurList.setRadius(mContext, 11F, 11F)
                            mCustomView.addView(blurList, 0, params)
                        }
                        viewGroup.post {
                            blur = BlurView(mContext, null)
                            val parmas =
                                    RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewGroup.height)
                            blur.setOverlayColor(Color.argb(blur_alpha, 255, 255, 255))
                            blur.setRadius(mContext, 11F, 11F)
                            viewGroup.addView(blur, 0, parmas)

                        }
                    } else {
                        mCustomView.backgroundResource = R.drawable.rect_button_bottom_menu_ios
                        viewGroup.backgroundResource = R.drawable.rect_button_bottom_menu_ios

                    }
                }
            }
            if (isDialogShown) {
                viewGroup.visibility = View.VISIBLE
            } else {
                viewGroup.visibility = View.GONE
            }
            when (type) {
                TYPE_KONGZUE -> {
                    menuArrayAdapter = NormalMenuArrayAdapter(mContext, item_resId, valueListStr, menuTextInfo)
                    listMenu.adapter = menuArrayAdapter
                }
                TYPE_IOS -> {
                    menuArrayAdapter = IOSMenuArrayAdapter(mContext, item_resId, valueListStr, menuTextInfo)
                    listMenu.adapter = menuArrayAdapter
                }
            }
            // parent, view,  id   参数从未使用过，可以改名为  _  或着在Gradle中 添加retrolambda {
            //    jvmArgs '-noverify'
            //}
            //https://github.com/evant/gradle-retrolambda/issues/105
            listMenu.onItemClick { _, _, position, _ ->
                mOnMenuItemClickListener!!.onClick(valueListStr[position], position)
                mLog("当前点击了valueListStr[position]="+valueListStr[position]+"---position="+position)
                mAlertDialog!!.dismiss()
            }
            btnCancel.onClick {
                mAlertDialog!!.dismiss()
            }
            dialogLifeCycleL!!.onShow(mAlertDialog!!)

        }
    }

    //private var title: String? = null
//    fun getTitle(): String? {
//
//        return title
//    }
//
    //设置标题
    fun setTitle(title: String): BottomMenu {
        this.title = title
        when (type) {
            TYPE_MATERIAL -> {
                if (!title.isNullOrEmpty()) {
                    txtTitle.text = title
                    txtTitle.visibility = View.VISIBLE

                } else {
                    txtTitle.visibility = View.GONE
                }
            }
            else -> {
                if (!title.isNullOrEmpty()) {
                    txtTitle.text = title
                    txtTitle.visibility = View.VISIBLE
                    imgTitle.setVisibility(View.VISIBLE)
                } else {
                    txtTitle.visibility = View.GONE
                }
            }

        }
        menuArrayAdapter.notifyDataSetChanged()
        return dialogValue as BottomMenu
    }

    //自定义View
    fun setCustomView(view: View): BottomMenu {
        cCustomView!!.setVisibility(View.VISIBLE)
        imgTitle.setVisibility(View.VISIBLE)
        cCustomView!!.addView(view)
        menuArrayAdapter.notifyDataSetChanged()
        return dialogValue as BottomMenu
    }

}





