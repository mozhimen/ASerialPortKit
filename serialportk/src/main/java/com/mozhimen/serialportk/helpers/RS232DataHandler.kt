package com.mozhimen.serialportk.helpers

import com.mozhimen.serialportk.commons.IDataFinishListener


/**
 * @ClassName RS232DataHandler
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/1/31 18:59
 * @Version 1.0
 */
class RS232DataHandler {
    companion object {
        private const val TAG = "RS232DataGroup"
        private const val HEAD = 0x02
        private const val END = 0x03
    }

    private var _dataFinishListener: IDataFinishListener? = null
    private val _buffer = ByteArray(1024)
    private var _cIndex = 0

    fun setDataFinishListener(listener: IDataFinishListener) {
        _dataFinishListener = listener
    }

    fun addData(data: ByteArray) {
        if (data.isEmpty()) return
        add(data, data[0].toInt() == HEAD)
        if (data[data.size - 1].toInt() == END && _cIndex > 0) {
            val result = ByteArray(_cIndex)
            System.arraycopy(_buffer, 0, result, 0, result.size)
            _cIndex = 0
            if (checkValid(result) && _dataFinishListener != null) {
                try {
                    _dataFinishListener!!.onFinish(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return
                }
            }
        }
    }

    private fun add(data: ByteArray, isHead: Boolean) {
        if (data.size > _buffer.size) {
            return
        }
        if (_cIndex + data.size > _buffer.size) {
            _cIndex = 0
            if (isHead) {
                add(data, true)
            }
            return
        }
        System.arraycopy(data, 0, _buffer, _cIndex, data.size)
        _cIndex += data.size
    }

    private fun checkValid(data: ByteArray): Boolean {
        return DataFormatUtil.isValidCRC16(data)
    }
}
