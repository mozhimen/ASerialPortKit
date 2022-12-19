package com.mozhimen.serialportk

import android.view.View
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.utilk.exts.start
import com.mozhimen.serialportk.databinding.ActivityMainBinding


/**
 * @ClassName MainActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/18 18:22
 * @Version 1.0
 */
class MainActivity : BaseActivityVB<ActivityMainBinding>() {

    fun goServiceReceive(view: View) {
        start<ServiceReceiveActivity>()
    }

    fun goSendReceive(view: View) {
        start<SendReceiveActivity>()
    }
}