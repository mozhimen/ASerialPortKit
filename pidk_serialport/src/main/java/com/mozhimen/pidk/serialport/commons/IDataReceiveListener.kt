package com.mozhimen.pidk.serialport.commons


/**
 * @ClassName ISerialPortKListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/18 19:02
 * @Version 1.0
 */
interface IDataReceiveListener {
    fun onReceiveData(data: ByteArray?, size: Int)
}