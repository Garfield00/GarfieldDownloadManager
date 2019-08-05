package com.garfield.download.env

import android.annotation.SuppressLint
import android.content.Context
import com.garfield.download.BuildConfig

/**
 * Author chunliangwang
 * Date 2019-06-28
 * Description 环境状态
 */
@SuppressLint("StaticFieldLeak")
object AppEnvLite {
    val DEBUG = BuildConfig.DEBUG

    var context: Context? = null

}