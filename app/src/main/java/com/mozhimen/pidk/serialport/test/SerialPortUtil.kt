package com.mozhimen.pidk.serialport.test

import com.mozhimen.basick.utilk.kotlin.UtilKString
import com.mozhimen.basick.utilk.kotlin.findFirst
import kotlin.math.pow

fun String.getStrBytesFor(startStr: String, length: Int): String =
    SerialPortUtil.getStrBytesFor(this, startStr, length)

object SerialPortUtil {

    @JvmStatic
    fun strHex2long(strContent: String): Long {
        var number = 0L
        val highLetter = arrayOf("A", "B", "C", "D", "E", "F")
        //将十六进制代表的数据存入map
        val map: MutableMap<String?, Int> = HashMap()
        for (i in 0..9)
            map[i.toString()] = i

        for (j in 10 until highLetter.size + 10)
            map[highLetter[j - 10]] = j

        //将字符串转为数组
        val str = arrayOfNulls<String>(strContent.length)
        for (i in str.indices)
            str[i] = strContent.substring(i, i + 1)

        //开始就算
        for (i in str.indices) {
            //代表的数值  *  16的  位数-1  的次方
            number += (map[str[i]]!! * 16.0.pow((str.size - 1 - i).toDouble())).toInt()
        }
        return number
    }

    //5a1fbe000000000000f5
    @JvmStatic
    fun getStrBytesFor(strBytes: String, startStr: String, length: Int): String =
        UtilKString.subStr(strBytes, strBytes.findFirst(startStr), length)
}