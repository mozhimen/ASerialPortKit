package com.mozhimen.pidk.serialport.commons


/**
 * @ClassName IDataFinishListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/1/31 19:00
 * @Version 1.0
 */
interface IDataFinishListener {
    fun onFinish(data: ByteArray)
    fun onEmpty()
}