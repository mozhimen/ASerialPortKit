package com.mozhimen.pidk_serialport_test

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.BaseActivityVB
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.pidk_serialport_test.databinding.ActivityMainBinding


/**
 * @ClassName MainActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/18 18:22
 * @Version 1.0
 */
class MainActivity : BaseActivityVB<ActivityMainBinding>() {

    fun goServiceReceive(view: View) {
        startContext<ServiceReceiveActivity>()
    }

    fun goSendReceive(view: View) {
        startContext<SendReceiveActivity>()
    }
}