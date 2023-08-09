package com.mozhimen.pidk_serialport_test

import android.os.Bundle
import android.util.Log
import com.mozhimen.basick.elemk.androidx.appcompat.bases.BaseActivityVB
import com.mozhimen.basick.utilk.kotlin.bytes2str
import com.mozhimen.pidk_serialport.SerialPortKHelper
import com.mozhimen.pidk_serialport.commons.IDataReceiveListener
import com.mozhimen.pidk_serialport_test.databinding.ActivitySendReceiveBinding


/**
 * @ClassName SendReceiveActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/18 18:27
 * @Version 1.0
 */
class SendReceiveActivity : BaseActivityVB<ActivitySendReceiveBinding>() {
    private val _serialPortKHelper: SerialPortKHelper by lazy { SerialPortKHelper() }
    private val _listener = object : IDataReceiveListener {
        override fun onReceiveData(data: ByteArray?, size: Int) {
            data?.let {
                val receiveData = data.bytes2str()/*String(data).filter()*/
                runOnUiThread {
                    vb.sendReceiveTxt.text = receiveData
                }
                Log.d(TAG, "onReceiveData: data $receiveData size $size")
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        _serialPortKHelper.init("/dev/ttyS8", 9600)
        _serialPortKHelper.setDataReceiveListener(_listener)
        _serialPortKHelper.open()
        super.initData(savedInstanceState)
    }

    override fun onDestroy() {
        _serialPortKHelper.close()
        super.onDestroy()
    }
}