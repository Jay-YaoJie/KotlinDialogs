package com.jeff.kotlindialog.bottom.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jeff.kotlindialog.R
import com.jeff.kotlindialog.info.TInfo



/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-12-04.
 * description ： 底部菜单iOS风格
 *
 * https://stackoverflow.com/questions/41923557/arrayadapter-use-kotlin-android
 */
class IOSMenuArrayAdapter(context: Context, resource: Int, list: List<String>,customMenuTextInfo: TInfo) :
    NormalMenuArrayAdapter(context, resource, list,customMenuTextInfo) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var holder:ViewHolder?=null;
        var view:View?=convertView;
        if (view == null) {
            holder= ViewHolder()
            view=  LayoutInflater.from(context).inflate(resource,null)
            holder.textView=view.findViewById(R.id.text) as TextView

        }
        val textStr=list[position]

             holder!!.textView!!.text=textStr
            if (customMenuTextInfo.fontSize>0){
                holder.textView!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, customMenuTextInfo.fontSize.toFloat())
            }
            if (customMenuTextInfo.gravity != -1) {
                holder.textView!!.setGravity(customMenuTextInfo.gravity)
            }
            if (customMenuTextInfo.fontColor != -1) {
                holder.textView!!.setTextColor(customMenuTextInfo.fontColor)
            }
            holder.textView!!.getPaint().setFakeBoldText(customMenuTextInfo.bold)

            if (list.size == 1) {
                if (!textStr.trim().isEmpty()) {
                    holder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_bottom)
                } else {
                    if (view!!.visibility== View.VISIBLE) {
                        holder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_all)
                    } else {
                        holder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_all)
                    }
                }
            } else {
                if (position == 0) {
                    if (!textStr.trim().isEmpty()) {
                        holder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_middle)
                    } else {
                        if (view!!.visibility == View.VISIBLE) {
                            holder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_middle)
                        } else {
                            holder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_top)
                        }
                    }
                } else if (position == list.size - 1) {
                    holder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_bottom)
                } else {
                    holder.textView!!.setBackgroundResource(R.drawable.button_menu_ios_middle)
                }
            }

        return view
    }
}