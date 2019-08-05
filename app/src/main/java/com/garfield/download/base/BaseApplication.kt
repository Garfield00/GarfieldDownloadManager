package com.garfield.download.base

import android.app.Application
import com.garfield.download.env.AppEnvLite

/**
 * Author chunliangwang
 * Date 2019-06-27
 * Description
 */

class BaseApplication : Application() {


    companion object {
        private var mInstance: Application? = null

        fun getInstance(): Application? {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        AppEnvLite.context = this
    }

    fun getInstance(): Application? {
        return mInstance
    }
}