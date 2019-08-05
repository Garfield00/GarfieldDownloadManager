package com.garfield.download.simpledownload

import android.annotation.SuppressLint
import android.util.Log
import com.garfield.download.engine.ProgressResponseBody
import okhttp3.*
import java.io.IOException

/**
 * Author chunliangwang
 * Date 2019-08-05
 * Description
 */

class OriginalDownload {
    companion object {
        const val TAG = "OriginalDownload"
    }

    fun originalDownload() {
        val listener = object : ProgressResponseBody.ProgressListener {
            @SuppressLint("SetTextI18n")
            override fun onProgress(bytesRead: Long, contentLength: Long, done: Boolean) {
                Log.i(TAG, "onProgress:bytesRead:$bytesRead:contentLength:$contentLength")
//                runOnUiThread {
//                    tv_progress?.text = "$bytesRead/$contentLength"
//                    probar_download?.max = contentLength.toInt()
//                    probar_download?.progress = bytesRead.toInt()
//                }
            }
        }

        val client = OkHttpClient.Builder()
            .addNetworkInterceptor {
                val response = it.proceed(it.request())
                response.newBuilder().body(ProgressResponseBody(response.body(), listener))
                    .build()
            }
            .build()


        val builder = Request.Builder()
        builder.url("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565008253383&di=4809bc8d8037df29caecb1add3aded23&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201210%2F24%2F20121024114802_sVwSR.jpeg")
//            builder.tag("garfield")
//            builder.cacheControl(CacheControl.FORCE_NETWORK)
//            builder.get()

        val request: Request = builder.build()

        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "onFailure")
            }

            override fun onResponse(call: Call, response: Response) {
                //从响应体读取字节流
                val data = response.body()?.bytes()
                Log.i(TAG, "onResponse")
                //由于当前处于非UI线程，所以切换到UI线程显示图片
//                    runOnUiThread {
//                        if (data != null) {
//                            img_download?.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.size))
//                        }
//                    }
            }

        })
    }
}