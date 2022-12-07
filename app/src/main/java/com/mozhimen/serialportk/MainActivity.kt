package com.mozhimen.serialportk

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.elemk.service.LifecycleServiceDelegate
import com.mozhimen.basick.elemk.service.bases.BaseServiceResCallback
import com.mozhimen.basick.permissionk.annors.APermissionK
import com.mozhimen.serialportk.databinding.ActivityMainBinding

@APermissionK(permissions = [Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE])
class MainActivity : BaseActivityVB<ActivityMainBinding>() {
    private lateinit var _servicePortServiceDelegate: LifecycleServiceDelegate<MainActivity>
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

    override fun initData(savedInstanceState: Bundle?) {
        _servicePortServiceDelegate = LifecycleServiceDelegate(this, SerialPortService::class.java, _serialPortResListener)
        super.initData(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        Log.d(TAG, "initView: drivers ${_serialPortFinder.getDrivers()}")
        Log.d(TAG, "initView: allDevices ${_serialPortFinder.getAllDevices().joinToString()}")
        Log.d(TAG, "initView: allDevicesPath ${_serialPortFinder.getAllDevicesPath().joinToString()}")
    }
}