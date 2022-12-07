package com.mozhimen.serialportk

import com.mozhimen.basick.utilk.UtilKString
import kotlin.math.pow

object SerialPortUtil {
    /**
     * byte[]数组转换为16进制的字符串
     *
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

    @JvmStatic
    fun covert(content: String): Long {
        var number = 0L
        val highLetter = arrayOf("A", "B", "C", "D", "E", "F")
        //将十六进制代表的数据存入map
        val map: MutableMap<String?, Int> = HashMap()
        for (i in 0..9) {
            map[i.toString()] = i
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
        return number
    }

    //5a1fbe000000000000f5
    @JvmStatic
    fun getCommByte(byteStr: String, startStr: String, length: Int): String =
        UtilKString.substring(byteStr, UtilKString.findFirst(byteStr, startStr), length)
}