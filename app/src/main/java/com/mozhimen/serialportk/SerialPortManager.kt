package com.mozhimen.serialportk

import android.serialport.SerialPort
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.mozhimen.basick.taskk.temps.TaskKPoll
import java.io.InputStream


/**
 * @ClassName SerialPortManager
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/7 14:31
 * @Version 1.0
 */
class SerialPortManager(owner: LifecycleOwner, listener: ISerialPortDataListener? = null) : ISerialPortListener {
    companion object {
        private const val TAG = "SerialPortManager>>>>>"
    }

    private val _taskKPolling: TaskKPoll by lazy { TaskKPoll(owner) }

    private var _serialPort: SerialPort? = null// Ads1115 TTL
    private var _inputStream: InputStream? = null
    private var _buffer: ByteArray? = null
    private var _bufferSize = 0
    private var _bufferDataChain = ""
    private var _serialPortDataListener: ISerialPortDataListener? = null

    init {
        listener?.let { _serialPortDataListener = it }
    }

    override fun init(): Boolean {
        try {
            _serialPort = SerialPort
                .newBuilder("/dev/ttyS8", 115200) // 串口地址地址，波特率
                .build()
            _inputStream = _serialPort!!.getInputStream()
        } catch (e: Exception) {
            e.printStackTrace()
            _serialPort = null
            _inputStream = null
            return false
        }
        return true
    }

    override fun open() {
        _taskKPolling.start(1000) {
            _buffer = ByteArray(64)
            _bufferSize = _inputStream?.read(_buffer) ?: kotlin.run {
                Log.e(TAG, "run: _inputStream is null")
                return@start
            }
            if (_bufferSize > 0) {
                _bufferDataChain = SerialPortUtil.getCommByte(SerialPortUtil.bytesToHexString(_buffer!!, _bufferSize), "5a", 20)
                _serialPortDataListener?.onGetData(_bufferDataChain)
            }
        }
    }

    override fun close() {
        _taskKPolling.cancel()
        _serialPort?.tryClose()
        _serialPort = null
        _inputStream = null
        _buffer = null
        _bufferSize = 0
        _bufferDataChain = ""
        _serialPortDataListener = null
    }
}