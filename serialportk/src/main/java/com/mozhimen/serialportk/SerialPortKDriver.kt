package com.mozhimen.serialportk

import android.util.Log
import java.io.File
import java.util.*


/**
 * @ClassName SerialPortKDriver
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/5 15:35
 * @Version 1.0
 */
class SerialPortKDriver(
    val name: String,
    private val _deviceRoot: String
) {
    companion object {
        private const val TAG = "SerialPortKDriver>>>>>"
    }

    private var _devices: Vector<File>? = null

    val devices: Vector<File>
        get() {
            if (_devices == null) {
                _devices = Vector()
                val dev = File("/dev")
                val files = dev.listFiles()
                if (files != null) {
                    var i = 0
                    while (i < files.size) {
                        if (files[i].absolutePath.startsWith(_deviceRoot)) {
                            Log.d(TAG, "Found new device: " + files[i])
                            _devices!!.add(files[i])
                        }
                        i++
                    }
                }
            }
            return _devices!!
        }
}