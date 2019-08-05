package com.garfield.gallery.utils

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.garfield.download.R
import com.garfield.download.base.BaseApplication
import java.lang.ref.SoftReference

/**
 * Author chunliangwang
 * Date 2019-06-27
 * Description Toast工具类
 */

object ToastUtils {

    private var sToastRef: SoftReference<Toast>? = null

    fun hideToast() {
        if (sToastRef != null) {
            val previousToast = sToastRef!!.get()
            previousToast?.cancel()
        }
    }

    fun showToast(context: Context?, res: Int) {
        if (context == null) {
            return
        }
        showToast(context, context.getString(res))
    }


    fun showToast(context: Context?, s: String) {
        if (context == null || TextUtils.isEmpty(s)) {
            return
        }
        //默认使用自定义view的toast,因为使用沉浸模式后个别手机的默认的toast，文字会不居中
        showCustomToast(context, s, false)
    }

    fun showToastInCenter(context: Context?, s: String, timeLong: Boolean) {
        if (context == null || TextUtils.isEmpty(s)) {
            return
        }
        //默认使用自定义view的toast,因为使用沉浸模式后个别手机的默认的toast，文字会不居中
        showCustomToast(context, s, timeLong, true, true)
    }

    fun showToast(context: Context?, s: String, timeLong: Boolean) {
        if (context == null || TextUtils.isEmpty(s)) {
            return
        }
        //默认使用自定义view的toast,因为使用沉浸模式后个别手机的默认的toast，文字会不居中
        showCustomToast(context, s, timeLong)
    }

    fun showToast(context: Context?, s: String, timeLong: Boolean, showCenter: Boolean) {
        if (context == null || TextUtils.isEmpty(s)) {
            return
        }
        showCustomToast(context, s, timeLong, true, showCenter)
    }

    @JvmOverloads
    fun showCustomToast(
        context: Context?,
        s: String?,
        timeLong: Boolean,
        cancelPreToast: Boolean = true,
        showCenter: Boolean = false
    ) {
        if (context == null || s == null) {
            return
        }

        if (Looper.getMainLooper() == Looper.myLooper()) {
            val toast = initToast(context, s, timeLong)
            if (cancelPreToast) {
                hideToast()
            }
            if (showCenter) {
                toast.setGravity(Gravity.CENTER, 0, 0)
            }
            toast.show()
            sToastRef = SoftReference(toast)
        } else {
            if (context is Activity) {
                context.runOnUiThread {
                    val toast = initToast(context, s, timeLong)
                    if (cancelPreToast) {
                        hideToast()
                    }
                    if (showCenter) {
                        toast.setGravity(Gravity.CENTER, 0, 0)
                    }
                    toast.show()
                    sToastRef = SoftReference(toast)
                }
            }
        }
    }

    fun initToast(context: Context?, s: String?, timeLong: Boolean): Toast {
        val toastView = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.toast, null)
        val message = toastView.findViewById(R.id.message) as TextView

        message.text = s
        val toast = Toast(context)
        toast.duration = if (timeLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        toast.view = toastView

        return toast
    }

}
