package com.mozhimen.serialportk

import android.annotation.SuppressLint
import android.util.Log
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.LineNumberReader
import java.util.*


/**
 * @ClassName SerialPortKSearcher
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/7 16:01
 * @Version 1.0
 */
class SerialPortKSearcher {
    companion object{
        private const val TAG = "SerialPortKSearcher>>>>>"
    }

    private var _serialPortDrivers: Vector<SerialPortKDriver>? = null

    @SuppressLint("LongLogTag")
    @Throws(IOException::class)
    fun getDrivers(): Vector<SerialPortKDriver> {
        if (_serialPortDrivers == null) {
            _serialPortDrivers = Vector()
            val r = LineNumberReader(FileReader("/proc/tty/drivers"))
            var l: String?
            while (r.readLine().also { l = it } != null && l != null) {
                // Issue 3:
                // Since driver name may contain spaces, we do not extract driver name with split()
                val driverName = l!!.substring(0, 0x15).trim()
                val w = l!!.split(" +".toRegex()).toTypedArray()
                if (w.size >= 5 && w[w.size - 1] == "serial") {
                    Log.d(TAG, "Found new driver " + driverName + " on " + w[w.size - 4])
                    _serialPortDrivers!!.add(SerialPortKDriver(driverName, w[w.size - 4]))
                }
            }
            r.close()
        }
        return _serialPortDrivers!!
    }

    fun getAllDevices(): Array<String> {
        val devices = Vector<String>()
        // Parse each driver
        val iteratorDriver: Iterator<SerialPortKDriver>
        try {
            iteratorDriver = getDrivers().iterator()
            while (iteratorDriver.hasNext()) {
                val serialPortDriver = iteratorDriver.next()
                val iteratorDevices: Iterator<File> = serialPortDriver.getDevices().iterator()
                while (iteratorDevices.hasNext()) {
                    val device = iteratorDevices.next().name
                    val value = String.format("%s (%s)", device, serialPortDriver.name)
                    devices.add(value)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return devices.toTypedArray()
    }

    fun getAllDevicesPath(): Array<String> {
        val devices = Vector<String>()
        // Parse each driver
        val iteratorDriver: Iterator<SerialPortKDriver>
        try {
            iteratorDriver = getDrivers().iterator()
            while (iteratorDriver.hasNext()) {
                val serialPortDriver = iteratorDriver.next()
                val iteratorDevice: Iterator<File> = serialPortDriver.getDevices().iterator()
                while (iteratorDevice.hasNext()) {
                    val device = iteratorDevice.next().absolutePath
                    devices.add(device)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return devices.toTypedArray()
    }

    class SerialPortKDriver(val name: String, private val _deviceRoot: String) {
        private val TAG = "SerialPortKDriver>>>>>"
        private var _devices: Vector<File>? = null

        fun getDevices(): Vector<File> {
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

        override fun toString(): String {
            return "SerialPortKDriver(name='$name', _deviceRoot='$_deviceRoot', _devices=$_devices)"
        }
    }
}