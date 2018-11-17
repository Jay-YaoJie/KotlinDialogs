package com.jeff.kotlindialogs.utils

import android.os.Environment
import com.jeff.kotlindialogs.constants.DialogSettings
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-17.
 * description ：文件操作工具类
 */
object FileUtils {

    /**
     * 获取根目录
     */
    fun getRootDir(): String {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageDirectory().absolutePath;
        } else {
            ""
        }

    }

    /**
     * 可创建多个文件夹
     * dirPath 文件路径
     */
    fun mkDir(dirPath: String) {

        val dirArray = dirPath.split("/".toRegex())
        var pathTemp = ""
        for (i in 1 until dirArray.size) {
            pathTemp = "$pathTemp/${dirArray[i]}"
            val newF = File("${dirArray[0]}$pathTemp")
            if (!newF.exists()) {
                val cheatDir: Boolean = newF.mkdir()
                println(cheatDir)
            }
        }

    }

    /**
     * 创建文件
     *
     * dirpath 文件目录
     * fileName 文件名称
     */
    fun creatFile(dirPath: String = getRootDir(), fileName: String) {
        val file = File("$dirPath/$fileName")
        if (!file.exists()) {
            file.createNewFile()
        }

    }

    /**
     * 创建文件
     * filePath 文件路径
     */
    fun creatFile(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    /**
     * 创建文件
     * filePath 文件路径
     */
    fun creatFile(filePath: File) {
        if (!filePath.exists()) {
            filePath.createNewFile()
        }
    }

    /**
     * 删除文件
     *
     * dirpath 文件目录
     * fileName 文件名称
     */
    fun delFile(dirpath: String = getRootDir(), fileName: String): Boolean {
        val file = File("$dirpath/$fileName")
        if (file.checkFile()) {
            return false
        }
        return file.delete()
    }

    /**
     *  删除文件
     *  filepath 文件路径
     */
    fun delFile(filepath: File): Boolean {
        if (filepath.checkFile()) {
            return false
        }
        return filepath.delete()
    }

    /**
     *  删除文件
     *  filepath 文件路径
     */
    fun delFile(filepath: String): Boolean {
        val file = File(filepath)
        if (file.checkFile()) {
            return false
        }
        return file.delete()
    }


    /**
     * 删除文件夹
     * dirPath 文件路径
     */
    fun delDir(dirpath: String) {
        val dir = File(dirpath)
        deleteDirWihtFile(dir)
    }

    fun deleteDirWihtFile(dir: File?) {
        if (dir!!.checkFile())
            return
        for (file in dir.listFiles()) {
            if (file.isFile)
                file.delete() // 删除所有文件
            else if (file.isDirectory)
                deleteDirWihtFile(file) // 递规的方式删除文件夹
        }
        dir.delete()// 删除目录本身
    }

    private fun File.checkFile(): Boolean {
        return this == null || !this.exists() || !this.isDirectory
    }

    /**
     * 修改SD卡上的文件或目录名
     * oldFilePath 旧文件或文件夹路径
     * newFilePath 新文件或文件夹路径
     */
    fun renameFile(oldFilePath: String, newFilePath: String): Boolean {
        val oldFile = File(oldFilePath)
        val newFile = File(newFilePath)
        return oldFile.renameTo(newFile)
    }

    /**
     * 文件读取
     * filePath 文件路径
     */
    fun readFile(filePath: File): String? {
        if (!filePath.isFile) {
            return null
        } else {
            return filePath.readText()
        }
    }

    /**
     * 文件读取
     * strPath 文件路径
     */
    fun readFile(strPath: String): String? {
        return readFile(File(strPath))
    }

    /**
     * InputStream 转字符串
     */
    fun readInp(inp: InputStream): String? {
        val bytes: ByteArray = inp.readBytes()
        return String(bytes)
    }

    /**
     * BufferedReader 转字符串
     */
    fun readBuff(buff: BufferedReader): String? {
        return buff.readText()
    }

    /**
     * 写入数据
     */
    fun writeText(filePath: File, content: String) {
        creatFile(filePath)
        filePath.writeText(content)
    }

    /**
     * 追加数据
     */
    fun appendText(filePath: File, content: String) {
        creatFile(filePath)
        filePath.appendText(content)
    }

    /**
     * 追加数据
     */
    fun appendBytes(filePath: File, array: ByteArray) {
        creatFile(filePath)
        filePath.appendBytes(array)
    }

    /**
     * 获取文件大小
     */
    fun getLeng(filePath: File): Long {
        return if (!filePath.exists()) {
            -1
        } else {
            filePath.length()
        }
    }

    /**
     * 按时间排序
     */
    fun sortByTime(filePath: File): Array<File>? {
        if (!filePath.exists()) {
            return null
        }
        val files: Array<File> = filePath.listFiles()
        if (files.isEmpty()) {
            return null
        }
        files.sortBy { it.lastModified() }
        files.reverse()
        return files
    }
/*===================此工具类专门 用来 产生 保存 文件 到sd卡上的 文件名称 ==*/
    /***
     * 获取要保存的文件路径，详细地址
     * @param formatNmae 传入一个文件后缀名  如 全小写  mp4 ，h264， jpg，3gp ，aac
     * @return File 创建一个当前时间的视频文件名，并包含了详细地址
     */
    fun getFilePath(formatNmae: String): File? {
        //获得当前时间
        val current = System.currentTimeMillis();
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date(current));
        var filePath: File? = null;
        when (formatNmae) {
            "mp4", "h264", "3gp" -> {//保存为视频文件
                filePath = File("${DialogSettings.ROOT_DIR}/${DialogSettings.APP_NAME}/videos/${time}.${formatNmae}");
            }
            "jpg", "JPEG", "png", "PNG" -> {//保存为图片文件
                filePath = File("${DialogSettings.ROOT_DIR}/${DialogSettings.APP_NAME}/images/${time}.${formatNmae}");
            }
            "aac" -> {//保存为声音文件
                filePath = File("${DialogSettings.ROOT_DIR}/${DialogSettings.APP_NAME}/voices/${time}.${formatNmae}");
            }
            else -> {//保存为其他文件
                filePath = File("${DialogSettings.ROOT_DIR}/${DialogSettings.APP_NAME}/elses/${time}.${formatNmae}");
            }
        }
        creatFile(filePath)
        return filePath;
    }

}