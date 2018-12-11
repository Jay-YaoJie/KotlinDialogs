package com.jeff.kotlindialog.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import com.jeff.kotlindialog.constants.DialogSettings
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-17.
 * description ：UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 */
class CrashHandler : Thread.UncaughtExceptionHandler {

    // 使用object关键字替代class关键字就可以声明一个单例对象
    //object Variable {
    val tag: String = "..CrashHandler";
    var sInstance: CrashHandler = CrashHandler()
    /*
    * Kotlin会使用null来对每一个用lateinit修饰的属性做初始化，
    * 系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    */
    lateinit var mDefaultCrashHandler: Thread.UncaughtExceptionHandler;


    //构造方法私有，防止外部构造多个实例，即采用单例模式
    companion object {
        //在静态区域内创建单例对象，在getInstantce这个方法中对对象加锁然后判断返回单例对象；
        private var mCrashHandler: CrashHandler? = null;

        fun getInit(): CrashHandler {
            if (mCrashHandler == null) {
                synchronized(CrashHandler::class.java) {
                    if (mCrashHandler == null) {
                        mCrashHandler = CrashHandler();
                    }
                }
            }
            return mCrashHandler!!;
        }
    }

    //完成初始化工作
    fun init() {
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //FlashLight.instance
    }

    private val execu: ExecutorService = Executors.newFixedThreadPool(1)
    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @SuppressLint("NewApi")
    override fun uncaughtException(t: Thread, ex: Throwable) {
        //To change body of created functions use File | Settings | File Templates.
        if (DialogSettings.CRASH_SAVESD) {
            try {
                execu.submit({
                    //导出异常信息到SD卡中
                    dumpExceptionToSDCard(ex)
//            //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
//            uploadExceptionToServer()
                });
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            //打印出当前调用栈信息
            ex.printStackTrace()
        }
        //如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        mDefaultCrashHandler.uncaughtException(t, ex)

    }

    var instance: Context? = null;
    @SuppressLint("NewApi")
    @Throws(IOException::class)
    private fun dumpExceptionToSDCard(ex: Throwable) {
        val sb: StringBuffer = StringBuffer();
        //应用的版本名称和版本号对象
        val pm = instance!!.getPackageManager();
        val pi = pm.getPackageInfo(DialogSettings.APP_NAME, PackageManager.GET_ACTIVITIES);
        //应用的版本名称和版本号
        sb.append("App :versionName =${pi.versionName}_versionCode=${pi.longVersionCode}");//versionCode
        //android版本号
        sb.append("OS :releaes=${Build.VERSION.RELEASE}_SDK_INT=${Build.VERSION.SDK_INT}");
        //手机制造商
        sb.append("manufactuer=${Build.MANUFACTURER}");
        //手机型号
        sb.append("model=${Build.MODEL}");
        //cpu架构
        sb.append("CPU ABI=${Build.SUPPORTED_ABIS}");//CPU_ABI
        //格式化异常信息
        val writer = StringWriter();
        val printWriter = PrintWriter(writer);
        ex.printStackTrace(printWriter);
        var cause: Throwable? = ex.cause;
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.cause;
        }
        printWriter.close();
        val result = writer.toString();
        sb.append(result);//保存异常信息
        //获得当前时间
        val current = System.currentTimeMillis();
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date(current));
        //以当前时间创建log文件
        val file = File(DialogSettings.CRASH_FILE_PATH + time + DialogSettings.CRASH_FILE_NAME);
        //写入到文件并保存数据
        FileUtils.writeText(file, sb.toString());
    }

    //上传到服务器上
    private fun uploadExceptionToServer() {
    }
}