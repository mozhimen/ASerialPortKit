package com.mozhimen.pidk.serialport.test

import android.os.Bundle
import android.util.Log
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.servicek.ServiceKProxy
import com.mozhimen.servicek.bases.BaseServiceResCallback
import com.mozhimen.pidk.serialport.SerialPortKSearcher
import com.mozhimen.pidk.serialport.test.databinding.ActivityReceiveServiceBinding

class ServiceReceiveActivity : BaseActivityVDB<ActivityReceiveServiceBinding>() {

    @OptIn(OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    private val _serviceProxy: ServiceKProxy<ServiceReceiveActivity> by lazy { ServiceKProxy(this, SerialPortService::class.java, _serialPortResListener) }

    private val _serialPortFinder by lazy { SerialPortKSearcher() }

    private var _serialPortResListener: BaseServiceResCallback = object : BaseServiceResCallback() {
        override fun onResString(resString: String?) {
            resString?.let {
                try {
                    Log.d(TAG, "onResString: $it")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @OptIn(OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    override fun initData(savedInstanceState: Bundle?) {
        _serviceProxy.bindLifecycle(this)
        super.initData(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        Log.d(TAG, "initView: drivers ${_serialPortFinder.getDrivers()}")
        Log.d(TAG, "initView: allDevices ${_serialPortFinder.getAllDevices().joinToString()}")
        Log.d(TAG, "initView: allDevicesPath ${_serialPortFinder.getAllDevicesPath().joinToString()}")
    }
}