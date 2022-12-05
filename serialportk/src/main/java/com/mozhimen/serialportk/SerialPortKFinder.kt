package com.mozhimen.serialportk

import android.util.Log
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.LineNumberReader
import java.util.*


/**
 * @ClassName SerialPortKFinder
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/5 15:32
 * @Version 1.0
 */
class SerialPortKFinder {
    companion object {
        private const val TAG = "SerialPortKFinder>>>>>"
    }

    private var _drivers: Vector<SerialPortKDriver>? = null

    @Throws(IOException::class)
    fun getDrivers(): Vector<SerialPortKDriver> {
        if (_drivers == null) {
            _drivers = Vector()
            val r = LineNumberReader(FileReader("/proc/tty/drivers"))
            var l: String
            while (r.readLine().also { l = it } != null) {
                // Issue 3:
                // Since driver name may contain spaces, we do not extract driver name with split()
                val driverName = l.substring(0, 0x15).trim { it <= ' ' }
                val w = l.split(" +").toTypedArray()
                if (w.size >= 5 && w[w.size - 1] == "serial") {
                    Log.d(TAG, "Found new driver " + driverName + " on " + w[w.size - 4])
                    _drivers!!.add(SerialPortKDriver(driverName, w[w.size - 4]))
                }
            }
            r.close()
        }
        return _drivers!!
    }

    fun getAllDevices(): Array<String> {
        val devices = Vector<String>()
        // Parse each driver
        val iteratorDriver: Iterator<SerialPortKDriver>
        try {
            iteratorDriver = getDrivers().iterator()
            while (iteratorDriver.hasNext()) {
                val driver = iteratorDriver.next()
                val iteratorDevice: Iterator<File> = driver.devices.iterator()
                while (iteratorDevice.hasNext()) {
                    val device = iteratorDevice.next().name
                    devices.add("$device (${driver.name})")
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
                val driver = iteratorDriver.next()
                val iteratorDevice: Iterator<File> = driver.devices.iterator()
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
}