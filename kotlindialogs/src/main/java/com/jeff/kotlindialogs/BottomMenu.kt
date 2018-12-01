package com.jeff.kotlindialogs

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.StyleRes
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.*
import android.view.textservice.TextInfo
import android.widget.*
import com.jeff.kotlindialogs.constants.BaseDialog
import com.jeff.kotlindialogs.constants.DialogSettings.TYPE_IOS
import com.jeff.kotlindialogs.constants.DialogSettings.TYPE_KONGZUE
import com.jeff.kotlindialogs.constants.DialogSettings.TYPE_MATERIAL
import com.jeff.kotlindialogs.constants.DialogSettings.blur_alpha
import com.jeff.kotlindialogs.constants.DialogSettings.dialogButtonTextInfo
import com.jeff.kotlindialogs.constants.DialogSettings.menuTextInfo
import com.jeff.kotlindialogs.constants.DialogSettings.type
import com.jeff.kotlindialogs.constants.DialogSettings.use_blur
import com.jeff.kotlindialogs.listener.OnMenuItemClickListener
import com.jeff.kotlindialogs.widget.BlurView
import android.view.WindowManager
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.LinearLayout
import android.view.Gravity


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

    private var bottomMenu: BottomMenu? = null
    private var menuText: List<String>? = null
    private var alertDialog: AlertDialog? = null
    private var activity: AppCompatActivity? = null
    private var isShowCancelButton = false
    private var onMenuItemClickListener: OnMenuItemClickListener? = null
    private var title: String? = null
    private var cancelButtonCaption = "取消"

    private var customMenuTextInfo: TextInfo? = null
    private var customButtonTextInfo: TextInfo? = null


    //Fast Function
    fun show(activity: AppCompatActivity, menuText: Array<String>): BottomMenu {
        val list = ArrayList();
        for (s in menuText) {
            list.add(s)
        }
        return show(activity, list, null, true, "取消")
    }

    fun show(activity: AppCompatActivity, menuText: List<String>): BottomMenu {
        return show(activity, menuText, null, true, "取消")
    }

    fun show(
        activity: AppCompatActivity,
        menuText: Array<String>,
        onMenuItemClickListener: OnMenuItemClickListener
    ): BottomMenu {
        val list = ArrayList()
        for (s in menuText) {
            list.add(s)
        }
        return show(activity, list, onMenuItemClickListener, true, "取消")
    }

    fun show(
        activity: AppCompatActivity,
        menuText: List<String>,
        onMenuItemClickListener: OnMenuItemClickListener
    ): BottomMenu {
        return show(activity, menuText, onMenuItemClickListener, true, "取消")
    }

    fun show(
        activity: AppCompatActivity,
        menuText: Array<String>,
        onMenuItemClickListener: OnMenuItemClickListener,
        isShowCancelButton: Boolean
    ): BottomMenu {
        val list = ArrayList()
        for (s in menuText) {
            list.add(s)
        }
        return show(activity, list, onMenuItemClickListener, isShowCancelButton, "取消")
    }

    fun show(
        activity: AppCompatActivity,
        menuText: List<String>,
        onMenuItemClickListener: OnMenuItemClickListener,
        isShowCancelButton: Boolean
    ): BottomMenu {
        return show(activity, menuText, onMenuItemClickListener, isShowCancelButton, "取消")
    }

    fun show(
        activity: AppCompatActivity,
        menuText: Array<String>,
        onMenuItemClickListener: OnMenuItemClickListener,
        isShowCancelButton: Boolean,
        cancelButtonCaption: String
    ): BottomMenu {
        val list = ArrayList()
        for (s in menuText) {
            list.add(s)
        }
        return show(activity, list, onMenuItemClickListener, isShowCancelButton, cancelButtonCaption)
    }

    fun show(
        activity: AppCompatActivity,
        menuText: List<String>,
        onMenuItemClickListener: OnMenuItemClickListener?,
        isShowCancelButton: Boolean,
        cancelButtonCaption: String
    ): BottomMenu {
        synchronized(BottomMenu::class.java) {
            val bottomMenu = BottomMenu()
            bottomMenu.cleanDialogLifeCycleListener()
            bottomMenu.activity = activity
            bottomMenu.menuText = menuText
            bottomMenu.isShowCancelButton = isShowCancelButton
            bottomMenu.onMenuItemClickListener = onMenuItemClickListener
            bottomMenu.cancelButtonCaption = cancelButtonCaption
            bottomMenu.title = ""
            if (menuText.isEmpty()) {
                bottomMenu.log("未启动底部菜单 -> 没有可显示的内容")
                return bottomMenu
            }
            bottomMenu.log("装载底部菜单 -> " + menuText.toString())
            bottomMenu.showDialog()
            bottomMenu.bottomMenu = bottomMenu
            return bottomMenu
        }
    }

    private var bottomSheetDialog: MyBottomSheetDialog? = null
    private var menuArrayAdapter: ArrayAdapter<*>? = null

    private var txtTitle: TextView? = null
    private var listMenu: ListView? = null
    private var btnCancel: TextView? = null
    private var boxCancel: ViewGroup? = null
    private var splitLine: ImageView? = null
    private var customView: RelativeLayout? = null

    private var blurList: BlurView? = null
    private var blurCancel: BlurView? = null

    private var boxList: RelativeLayout? = null

    override fun showDialog() {
        log("启动底部菜单 -> " + menuText!!.toString())
        BaseDialog.dialogList.add(bottomMenu)

        if (customMenuTextInfo == null) {
            customMenuTextInfo = menuTextInfo
        }
        if (customButtonTextInfo == null) {
            customButtonTextInfo = dialogButtonTextInfo
        }

        if (type === TYPE_MATERIAL) {
            bottomSheetDialog = MyBottomSheetDialog(activity!!)
            val box_view = LayoutInflater.from(activity).inflate(R.layout.bottom_menu_material, null)

            listMenu = box_view.findViewById(R.id.list_menu)
            btnCancel = box_view.findViewById(R.id.btn_cancel)
            txtTitle = box_view.findViewById(R.id.title)
            customView = box_view.findViewById(R.id.box_custom)

            if (customButtonTextInfo!!.getFontSize() > 0) {
                btnCancel!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, customButtonTextInfo!!.getFontSize())
            }
            if (customButtonTextInfo!!.getGravity() !== -1) {
                btnCancel!!.gravity = customButtonTextInfo!!.getGravity()
            }
            if (customButtonTextInfo!!.getFontColor() !== -1) {
                btnCancel!!.setTextColor(customButtonTextInfo!!.getFontColor())
            }
            btnCancel!!.paint.isFakeBoldText = customButtonTextInfo!!.isBold()
            btnCancel!!.text = cancelButtonCaption

            if (title != null && !title!!.trim { it <= ' ' }.isEmpty()) {
                txtTitle!!.text = title
                txtTitle!!.visibility = View.VISIBLE
            } else {
                txtTitle!!.visibility = View.GONE
            }

            menuArrayAdapter = NormalMenuArrayAdapter(activity, R.layout.item_bottom_menu_material, menuText)
            listMenu!!.adapter = menuArrayAdapter

            listMenu!!.onItemClickListener = object : AdapterView.OnItemClickListener {
                fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (onMenuItemClickListener != null)
                        onMenuItemClickListener!!.onClick(menuText!![position], position)
                    bottomSheetDialog!!.dismiss()
                }
            }

            bottomSheetDialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            bottomSheetDialog!!.setContentView(box_view)
            bottomSheetDialog!!.setCancelable(true)
            bottomSheetDialog!!.setCanceledOnTouchOutside(true)
            bottomSheetDialog!!.setOnDismissListener {
                BaseDialog.dialogList.remove(bottomMenu)
                if (customView != null) customView!!.removeAllViews()
                if (getDialogLifeCycleListener() != null)
                    getDialogLifeCycleListener()!!.onDismiss()
                isDialogShown = false
                activity = null
                try {
                    finalize()
                } catch (throwable: Throwable) {
                    if (DEBUGMODE) throwable.printStackTrace()
                }
            }
            if (getDialogLifeCycleListener() != null)
                getDialogLifeCycleListener()!!.onCreate(bottomSheetDialog!!)
            bottomSheetDialog!!.show()
            if (getDialogLifeCycleListener() != null)
                getDialogLifeCycleListener()!!.onShow(bottomSheetDialog!!)
        } else {
            val builder: AlertDialog.Builder
            builder = AlertDialog.Builder(activity!!, R.style.bottom_menu)
            builder.setCancelable(true)
            alertDialog = builder.create()
            alertDialog!!.setCanceledOnTouchOutside(true)
            if (getDialogLifeCycleListener() != null)
                getDialogLifeCycleListener()!!.onCreate(alertDialog!!)
            alertDialog!!.setOnDismissListener {
                BaseDialog.dialogList.remove(bottomMenu)
                if (customView != null) customView!!.removeAllViews()
                if (getDialogLifeCycleListener() != null)
                    getDialogLifeCycleListener()!!.onDismiss()
                isDialogShown = false
                activity = null
            }
            alertDialog!!.show()
            val window = alertDialog!!.window
            val windowManager = activity!!.windowManager
            val display = windowManager.defaultDisplay
            val lp = window!!.attributes
            lp.width = display.width
            window.setGravity(Gravity.BOTTOM)
            window.attributes = lp
            window.setWindowAnimations(R.style.bottomMenuAnimStyle)

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
            window.setContentView(resId)

            listMenu = window.findViewById(R.id.list_menu)
            btnCancel = window.findViewById(R.id.btn_cancel)
            txtTitle = window.findViewById(R.id.title)
            splitLine = window.findViewById(R.id.title_split_line)
            customView = window.findViewById(R.id.box_custom)

            if (title != null && !title!!.trim { it <= ' ' }.isEmpty()) {
                txtTitle!!.text = title
                txtTitle!!.visibility = View.VISIBLE
                splitLine!!.visibility = View.VISIBLE
            } else {
                txtTitle!!.visibility = View.GONE
                splitLine!!.visibility = View.GONE
            }

            if (customButtonTextInfo!!.getFontSize() > 0) {
                btnCancel!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, customButtonTextInfo!!.getFontSize())
            }
            if (customButtonTextInfo!!.getGravity() !== -1) {
                btnCancel!!.gravity = customButtonTextInfo!!.getGravity()
            }
            if (customButtonTextInfo!!.getFontColor() !== -1) {
                btnCancel!!.setTextColor(customButtonTextInfo!!.getFontColor())
            }
            btnCancel!!.paint.isFakeBoldText = customButtonTextInfo!!.isBold()

            btnCancel!!.text = cancelButtonCaption

            when (type) {
                TYPE_KONGZUE -> boxCancel = window.findViewById(R.id.box_cancel) as LinearLayout
                TYPE_IOS -> {
                    boxList = window.findViewById(R.id.box_list)
                    boxCancel = window.findViewById(R.id.box_cancel) as RelativeLayout
                    if (use_blur) {
                        boxList!!.post {
                            blurList = BlurView(activity!!, null)
                            val params =
                                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxList!!.height)
                            blurList!!.setOverlayColor(Color.argb(blur_alpha, 255, 255, 255))
                            blurList!!.setRadius(activity!!, 11f, 11f)
                            boxList!!.addView(blurList, 0, params)
                        }
                        boxCancel!!.post {
                            blurCancel = BlurView(activity!!, null)
                            val params =
                                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxCancel!!.height)
                            blurCancel!!.setOverlayColor(Color.argb(blur_alpha, 255, 255, 255))
                            blurCancel!!.setRadius(activity!!, 11f, 11f)
                            boxCancel!!.addView(blurCancel, 0, params)
                        }
                    } else {
                        boxList!!.setBackgroundResource(R.drawable.rect_button_bottom_menu_ios)
                        boxCancel!!.setBackgroundResource(R.drawable.rect_button_bottom_menu_ios)
                    }
                }
            }

            if (isShowCancelButton) {
                if (boxCancel != null) boxCancel!!.visibility = View.VISIBLE
            } else {
                if (boxCancel != null) boxCancel!!.visibility = View.GONE
            }

            when (type) {
                TYPE_KONGZUE -> {
                    menuArrayAdapter = NormalMenuArrayAdapter(activity, item_resId, menuText)
                    listMenu!!.adapter = menuArrayAdapter
                }
                TYPE_IOS -> {
                    menuArrayAdapter = IOSMenuArrayAdapter(activity, item_resId, menuText)
                    listMenu!!.adapter = menuArrayAdapter
                }
            }

            listMenu!!.onItemClickListener = object : AdapterView.OnItemClickListener {
                fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (onMenuItemClickListener != null)
                        onMenuItemClickListener!!.onClick(menuText!![position], position)
                    alertDialog!!.dismiss()
                }
            }

            btnCancel!!.setOnClickListener(object : View.OnClickListener() {
                fun onClick(v: View) {
                    alertDialog!!.dismiss()
                }
            })
            if (getDialogLifeCycleListener() != null)
                getDialogLifeCycleListener()!!.onShow(alertDialog!!)
        }
    }

    override fun doDismiss() {
        if (alertDialog != null) alertDialog!!.dismiss()
    }

    internal inner class NormalMenuArrayAdapter(var context: Context, var resoureId: Int, var objects: List<String>) :
        ArrayAdapter<*>(context, resoureId, objects) {

        inner class ViewHolder {
            internal var textView: TextView? = null
        }

        override fun getCount(): Int {
            return objects.size
        }

        override fun getItem(position: Int): String? {
            return objects[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            var viewHolder: ViewHolder? = null
            if (convertView == null) {
                viewHolder = ViewHolder()
                val mInflater = LayoutInflater.from(context)
                convertView = mInflater.inflate(resoureId, null)
                viewHolder.textView = convertView!!.findViewById(R.id.text)
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }
            val text = objects[position]
            if (null != text) {
                viewHolder.textView!!.text = text
                if (customMenuTextInfo!!.getFontSize() > 0) {
                    viewHolder.textView!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, customMenuTextInfo!!.getFontSize())
                }
                if (customMenuTextInfo!!.getGravity() !== -1) {
                    viewHolder.textView!!.gravity = customMenuTextInfo!!.getGravity()
                }
                if (customMenuTextInfo!!.getFontColor() !== -1) {
                    viewHolder.textView!!.setTextColor(customMenuTextInfo!!.getFontColor())
                }
                viewHolder.textView!!.paint.isFakeBoldText = customMenuTextInfo!!.isBold()
            }

            return convertView
        }
    }

    internal inner class IOSMenuArrayAdapter(context: Context, resourceId: Int, objects: List<String>) :
        NormalMenuArrayAdapter(context, resourceId, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            var viewHolder: NormalMenuArrayAdapter.ViewHolder? = null
            if (convertView == null) {
                viewHolder = NormalMenuArrayAdapter.ViewHolder()
                val mInflater = LayoutInflater.from(context)
                convertView = mInflater.inflate(resoureId, null)
                viewHolder!!.textView = convertView!!.findViewById(R.id.text)
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as NormalMenuArrayAdapter.ViewHolder
            }
            val text = objects.get(position)
            if (null != text) {
                viewHolder.textView!!.setText(text)

                if (customMenuTextInfo!!.getFontSize() > 0) {
                    viewHolder.textView!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, customMenuTextInfo!!.getFontSize())
                }
                if (customMenuTextInfo!!.getGravity() !== -1) {
                    viewHolder.textView!!.gravity = customMenuTextInfo!!.getGravity()
                }
                if (customMenuTextInfo!!.getFontColor() !== -1) {
                    viewHolder.textView!!.setTextColor(customMenuTextInfo!!.getFontColor())
                }
                viewHolder.textView!!.paint.isFakeBoldText = customMenuTextInfo!!.isBold()

                if (objects.size == 1) {
                    if (title != null && !title!!.trim { it <= ' ' }.isEmpty()) {
                        viewHolder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_bottom)
                    } else {
                        if (customView!!.visibility == View.VISIBLE) {
                            viewHolder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_all)
                        } else {
                            viewHolder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_all)
                        }
                    }
                } else {
                    if (position == 0) {
                        if (title != null && !title!!.trim { it <= ' ' }.isEmpty()) {
                            viewHolder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_middle)
                        } else {
                            if (customView!!.visibility == View.VISIBLE) {
                                viewHolder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_middle)
                            } else {
                                viewHolder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_top)
                            }
                        }
                    } else if (position == objects.size - 1) {
                        viewHolder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_bottom)
                    } else {
                        viewHolder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_middle)
                    }
                }
            }

            return convertView
        }
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?): BottomMenu {
        this.title = title
        when (type) {
            TYPE_MATERIAL -> if (bottomSheetDialog != null && txtTitle != null) {
                if (title != null && !title.trim { it <= ' ' }.isEmpty()) {
                    txtTitle!!.text = title
                    txtTitle!!.visibility = View.VISIBLE
                } else {
                    txtTitle!!.visibility = View.GONE
                }
            }
            else -> if (alertDialog != null && txtTitle != null) {
                if (title != null && !title.trim { it <= ' ' }.isEmpty()) {
                    txtTitle!!.text = title
                    txtTitle!!.visibility = View.VISIBLE
                    splitLine!!.visibility = View.VISIBLE
                } else {
                    txtTitle!!.visibility = View.GONE
                }
            }
        }
        if (menuArrayAdapter != null) menuArrayAdapter!!.notifyDataSetChanged()
        return this
    }

    internal inner class MyBottomSheetDialog : BottomSheetDialog {

        constructor(context: Context) : super(context) {}

        constructor(context: Context, @StyleRes theme: Int) : super(context, theme) {}

        protected constructor(
            context: Context,
            cancelable: Boolean,
            cancelListener: DialogInterface.OnCancelListener
        ) : super(context, cancelable, cancelListener) {
        }

        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            val screenHeight = getScreenHeight(context)
            val statusBarHeight = getStatusBarHeight(context)
            window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                if (screenHeight == 0) ViewGroup.LayoutParams.MATCH_PARENT else screenHeight
            )
        }

        fun getStatusBarHeight(context: Context): Int {
            var result = 0
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

        fun getScreenHeight(context: Context): Int {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            return wm.defaultDisplay.height
        }
    }

    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun setCustomView(view: View?): BottomMenu {
        if (alertDialog != null && view != null) {
            customView!!.visibility = View.VISIBLE
            splitLine!!.visibility = View.VISIBLE
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


