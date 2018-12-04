package com.jeff.kotlindialogs.bottom.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.jeff.kotlindialogs.R
import com.jeff.kotlindialogs.info.TInfo

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-12-04.
 * description ：灰黑色的适配器 NormalMenuArrayAdapter
 * https://stackoverflow.com/questions/41923557/arrayadapter-use-kotlin-android
 */
open class NormalMenuArrayAdapter(context: Context, resource: Int, list: ArrayList<String>) : ArrayAdapter<String>(context, resource, list) {
    var resource: Int
    var list: ArrayList<String>

    var customMenuTextInfo: TInfo? = null

    init {
        this.resource = resource;
        this.list = list;
    }

    class ViewHolder {
        var textView: TextView? = null
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): String? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var holder: ViewHolder?;
        var view:View?=convertView;
        if (view == null) {
            holder = ViewHolder()
            view= LayoutInflater.from(context).inflate(resource,null)
            holder.textView=view.findViewById(R.id.text)
            view.tag=holder;
        } else {
            holder = view.tag as ViewHolder;
        }
        val text:String=list[position];
        if (text!=null){
            holder.textView!!.text=text;
            if (customMenuTextInfo!!.fontSize>0){
                holder.textView!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, customMenuTextInfo!!.fontSize.toFloat())
            }
            if (customMenuTextInfo!!.gravity!== -1) {
                holder!!.textView!!.setGravity(customMenuTextInfo!!.gravity)
            }
            if (customMenuTextInfo!!.fontColor !== -1) {
                holder.textView!!.setTextColor(customMenuTextInfo!!.fontColor)
            }
            holder!!.textView!!.getPaint().setFakeBoldText(customMenuTextInfo!!.bold)
        }

        return view
    }

}




