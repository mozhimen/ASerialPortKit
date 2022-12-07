package com.mozhimen.serialportk


/**
 * @ClassName ISerialPortListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/7 14:28
 * @Version 1.0
 */
interface ISerialPortListener {
    fun init(): Boolean
    fun open()
    fun close()
}