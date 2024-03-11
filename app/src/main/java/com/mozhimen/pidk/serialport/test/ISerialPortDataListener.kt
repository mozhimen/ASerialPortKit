package com.mozhimen.pidk.serialport.test


/**
 * @ClassName ISerialPortDataListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/7 14:30
 * @Version 1.0
 */
interface ISerialPortDataListener {
    fun onGetData(data: String)
}