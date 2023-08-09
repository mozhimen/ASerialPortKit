package com.mozhimen.pidk_serialport_test

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.mozhimen.basick.elemk.android.app.ServiceProxy
import com.mozhimen.basick.elemk.android.app.bases.BaseServiceResCallback
import com.mozhimen.basick.elemk.androidx.appcompat.bases.BaseActivityVB
import com.mozhimen.basick.lintk.optin.OptInApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optin.OptInApiInit_ByLazy
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.pidk_serialport.SerialPortKSearcher
import com.mozhimen.pidk_serialport_test.databinding.ActivityReceiveServiceBinding

@AManifestKRequire(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
class ServiceReceiveActivity : BaseActivityVB<ActivityReceiveServiceBinding>() {

    @OptIn(OptInApiCall_BindLifecycle::class, OptInApiInit_ByLazy::class)
    private val _serviceProxy: ServiceProxy<ServiceReceiveActivity> by lazy { ServiceProxy(this, SerialPortService::class.java, _serialPortResListener) }

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

    @OptIn(OptInApiCall_BindLifecycle::class, OptInApiInit_ByLazy::class)
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