package com.mozhimen.pidk.serialport.test


/**
 * @ClassName ISerialPortDataListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
interface ISerialPortDataListener {
    fun onGetData(data: String)
}