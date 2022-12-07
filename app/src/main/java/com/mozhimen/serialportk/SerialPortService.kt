package com.mozhimen.serialportk

import com.mozhimen.basick.elemk.service.bases.BaseService


/**
 * @ClassName SerialPortService
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/7 14:27
 * @Version 1.0
 */
class SerialPortService : BaseService(), ISerialPortListener {
    private val _serialPortDataListener = object : ISerialPortDataListener {
        override fun onGetData(data: String) {
            onCallback(data)
        }
    }
    private val _serialPortManager: SerialPortManager by lazy { SerialPortManager(this, _serialPortDataListener) }

    override fun onCreate() {
        super.onCreate()
        if (init()) {
            open()
        }
    }

    override fun onDestroy() {
        close()
        super.onDestroy()
    }

    override fun init(): Boolean = _serialPortManager.init()

    override fun open() {
        _serialPortManager.open()
    }

    override fun close() {
        _serialPortManager.close()
    }
}