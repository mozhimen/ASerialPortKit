package com.mozhimen.pidk.serialport.test


/**
 * @ClassName ISerialPortListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
interface ISerialPortListener {
    fun init(): Boolean
    fun open()
    fun close()
}