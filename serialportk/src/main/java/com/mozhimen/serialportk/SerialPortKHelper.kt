package com.mozhimen.pidk.serialport

import android.serialport.SerialPort
import android.util.Log
import com.mozhimen.pidk.serialport.commons.IDataReceiveListener
import java.io.InputStream
import java.io.OutputStream


/**
 * @ClassName UtilKSerialPortK
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/18 18:33
 * @Version 1.0
 */
class SerialPortKHelper {
    companion object {
        private const val TAG = "SerialPortKHelper>>>>>"
    }

    private var _serialPort: SerialPort? = null

    private var _isSerialPortOpen = false //是否打开串口标志
    private var _isThreadNeedInterrupt = false//线程状态，为了安全终止线程

    private var _inputStream: InputStream? = null
    private var _outputStream: OutputStream? = null
    private var _buffer: ByteArray? = null
    private var _bufferSize = 0
    private var _dataChainSend: String = ""
    private var _dataReceiveListener: IDataReceiveListener? = null

    fun init(devicePath: String, baudrate: Int): SerialPort? {
        try {
            _serialPort = SerialPort
                .newBuilder(devicePath, baudrate) // 串口地址地址，波特率
                .build()
            _isSerialPortOpen = true
            _isThreadNeedInterrupt = false
            _inputStream = _serialPort!!.getInputStream()            //获取打开的串口中的输入输出流，以便于串口数据的收发
            _outputStream = _serialPort!!.getOutputStream()
            Log.d(TAG, "init: success")
            return _serialPort
        } catch (e: Exception) {
            e.printStackTrace()
            _isSerialPortOpen = false
            _isThreadNeedInterrupt = true
            Log.e(TAG, "init: fail ${e.message}")
            return null
        }
    }

    fun isInitSuccess(): Boolean =
        _isSerialPortOpen

    fun open() {
        if (isInitSuccess()) {
            ReceiveThread().start() //开启线程监控是否有数据的收发
            Log.d(TAG, "open: success")
        } else {
            Log.d(TAG, "open: fail")
        }
    }

    fun close() {
        try {
            _inputStream?.close()
            _outputStream?.flush()
            _outputStream?.close()
            _serialPort?.tryClose()
            _isThreadNeedInterrupt = true
            _isSerialPortOpen = false
            Log.d(TAG, "close: success")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "close: fail ${e.message}")
        }
    }

    fun setDataReceiveListener(listener: IDataReceiveListener) {
        _dataReceiveListener = listener
    }

    fun sendData(data: String) {
        Log.d(TAG, "sendData: $data")
        try {
            val sendData = data.toByteArray().also { _dataChainSend = String(it) }
            if (sendData.isNotEmpty()) {
                _outputStream?.apply {
                    write(sendData)
                    write('\n'.code)
                    flush()
                }
                Log.d(TAG, "sendData: success")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "sendData: fail ${e.message}")
        }
    }

    private inner class ReceiveThread : Thread() {
        override fun run() {
            super.run()
            while (!_isThreadNeedInterrupt) {            //判断线程是否安全进行，更安全的结束线程
                _buffer = ByteArray(64)
                try {
                    _bufferSize = _inputStream?.read(_buffer!!) ?: kotlin.run {
                        Log.e(TAG, "run: _inputStream is null")
                        return
                    }
                    if (_bufferSize > 0) {
                        _dataReceiveListener?.onReceiveData(_buffer, _bufferSize)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                sleep(100)
            }
        }
    }
}