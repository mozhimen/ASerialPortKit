package com.mozhimen.serialportk.helpers

import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException
import kotlin.math.pow


/**
 * @ClassName SerialPortKUtil
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/1/31 19:32
 * @Version 1.0
 */
object SerialPortKUtil {
    /**
     * byte[]数组转换为16进制的字符串
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    @JvmStatic
    fun bytesToHexString(bytes: ByteArray, size: Int): String {
        val sb = StringBuilder()
        for (i in 0 until size) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    /**
     *
     * @param content String
     * @return Long
     */
    @JvmStatic
    fun covert(content: String): Long {
        var number = 0
        val highLetter = arrayOf("A", "B", "C", "D", "E", "F")
        //将十六进制代表的数据存入map
        val map: MutableMap<String?, Int> = HashMap()
        for (i in 0..9) {
            map[i.toString() + ""] = i
        }
        for (j in 10 until highLetter.size + 10) {
            map[highLetter[j - 10]] = j
        }
        //将字符串转为数组
        val str = arrayOfNulls<String>(content.length)
        for (i in str.indices) {
            str[i] = content.substring(i, i + 1)
        }
        //开始就算
        for (i in str.indices) {
            //代表的数值  *  16的  位数-1  的次方
            number += (map[str[i]]!! * 16.0.pow((str.size - 1 - i).toDouble())).toInt()
        }
        return number.toLong()
    }

    /**
     *
     * @param arr IntArray
     */
    @JvmStatic
    fun sort(arr: IntArray) {
        if (arr.size >= 2) {
            for (i in 1 until arr.size) {
                //挖出一个要用来插入的值,同时位置上留下一个可以存新的值的坑
                val x = arr[i]
                var j = i - 1
                //在前面有一个或连续多个值比x大的时候,一直循环往前面找,将x插入到这串值前面
                while (j >= 0 && arr[j] > x) {
                    //当arr[j]比x大的时候,将j向后移一位,正好填到坑中
                    arr[j + 1] = arr[j]
                    j--
                }
                //将x插入到最前面
                arr[j + 1] = x
            }
        }
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    @JvmStatic
    fun differentDaysByMillisecond(date1: Long, date2: Long): Int {
        return (date2 - date1).toInt()
    }

    /**
     * 转化
     * @param data ByteArray?
     * @return Double
     */
    @JvmStatic
    fun convertByteToDouble(data: ByteArray): Double {
        val dis = DataInputStream(ByteArrayInputStream(data))
        var num = 0.0
        try {
            num = dis.readDouble()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return num
    }

    /**
     * 从byte[]中抽取新的byte[]
     * @param data - 元数据
     * @param start - 开始位置
     * @param end - 结束位置
     * @return 新byte[]
     */
    @JvmStatic
    fun getByteArr(data: ByteArray, start: Int, end: Int): ByteArray {
        val ret = ByteArray(end - start)
        var i = 0
        while (start + i < end) {
            ret[i] = data[start + i]
            i++
        }
        return ret
    }
}