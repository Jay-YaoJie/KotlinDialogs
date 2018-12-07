package com.jeff.kotlindialogs.utils

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-17.
 * description ：自定义委托对象by Delegates.notNull()
 */
object DelegatesExt {
    //现在你可以创建一个对象，然后添加函数使用你的委托：
    fun <T> notNullSingleValue(): ReadWriteProperty<Any?, T> = NotNullSingleValueVar()
    fun <T> getPreference(context: Context, name: String, default: T): ReadWriteProperty<Any?, T> =
        Preference(context, name, default)
}

/**
 * Kotlin库提供了几个接口，我们自己的委托必须要实
 * 现： ReadOnlyProperty 和 ReadWriteProperty 。具体取决于我们被委托的对
 * 象是 val 还是 var 。
 * 我们要做的第一件事就是创建一个类然后继承 ReadWriteProperty */
class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {

    private var value: T? = null
    //Getter函数 如果已经被初始化，则会返回一个值，否则会抛异常。
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("${property.name} not initialized")
    }

    //Setter函数 如果仍然是null，则赋值，否则会抛异常。
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value else throw IllegalStateException("${property.name} not initialized")
    }
}

/**
 * https://www.ctolib.com/docs/sfile/kotlin-for-android-developers-zh/fan_xing_preference_wei_tuo.html
 * 泛型preference委托
 * https://www.ctolib.com/docs/sfile/kotlin-for-android-developers-zh/fang_wen_shared_preferences.html
 * 访问Shared Preferences
 * **/
class Preference<T>(val context: Context, val name: String, val default: T) : ReadWriteProperty<Any?, T> {
    val ref: SharedPreferences by lazy { context.getSharedPreferences("News", Context.MODE_PRIVATE) }
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreferenceByName(name, default) }
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }
     // https://www.cnblogs.com/nexiyi/p/how_to_avoid_unchecked_cast.html
    // 在这里会产生 Unchecked cast: Any! to T
    @SuppressWarnings("unchecked")
    private fun findPreferenceByName(name: String, default: T): T = with(ref) {
        val res:Any?= when (default) {
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is String -> getString(name, default)
            is Long -> getLong(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("this type not support")
        }
        return res!! as T
    }
    private fun putPreference(name: String, value: T) = with(ref.edit()) {
        when (value) {
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is String -> putString(name, value)
            is Long -> putLong(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("this type not support")
        }.apply()
    }

}