package com.mozhimen.pidk_serialport_test

import com.mozhimen.basick.elemk.android.app.bases.BaseLifecycleService2


/**
 * @ClassName SerialPortService
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/7 14:27
 * @Version 1.0
 */
class SerialPortService : BaseLifecycleService2(), ISerialPortListener {

    private val _serialPortDataListener = object : ISerialPortDataListener {
        override fun onGetData(data: String) {
            invoke(data)
        }
    }
    private val _serialPortManager: SerialPortManager by lazy { SerialPortManager(this, _serialPortDataListener) }

    override fun onCreate() {
        super.onCreate()
        if (init())
            open()
    }

    override fun onDestroy() {
        close()
        super.onDestroy()
    }

    override fun init(): Boolean =
        _serialPortManager.init()

    override fun open() {
        _serialPortManager.open()
    }

    override fun close() {
        _serialPortManager.close()
    }
}