package com.mozhimen.pidk.serialport.helpers

/**
 * @ClassName CSUtil
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/10 15:09
 * @Version 1.0
 */
object CSUtil {
    //cs校验
    @JvmStatic
    fun getCS(bytes: ByteArray): Byte {
        try {
            var num = 0
            for (i in bytes.indices)
                num = (num + bytes[i]) % 256
            return num.toByte()
        } catch (e: Exception) {
        }
        return 0
    }
}