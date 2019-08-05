package com.garfield.download.simpledownload

import android.util.Log
import com.garfield.download.MainActivity
import com.garfield.download.engine.ProgressResponseBody
import okhttp3.*
import java.io.*
import java.util.concurrent.TimeUnit

/**
 * Author chunliangwang
 * Date 2019-08-05
 * Description 简易下载器
 */

class SimpleDownloadManager {

    companion object {
        val instance: SimpleDownloadManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SimpleDownloadManager()
        }

        const val TIME_OUT = 10 * 1000
    }

    private val okClient: OkHttpClient

    init {
        okClient = newOkClient()
    }

    private fun newOkClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
        builder.readTimeout(TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
        builder.writeTimeout(TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
        builder.retryOnConnectionFailure(false)
        return builder.build()
    }

    fun downloadFile(url: String, outputFile: File?, downloadListener: SimpleDownloadListener) {
        val builder = Request.Builder()
        builder.url(url)
//        builder.tag("garfield")
//        builder.cacheControl(CacheControl.FORCE_NETWORK)
//        builder.get()

        val request: Request = builder.build()

        val call = okClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(MainActivity.TAG, "onFailure")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    downloadListener.onFailure()
                    return
                }

                var progress = 0

                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null
                try {
                    if (outputFile != null && !outputFile.exists()) {
                        val dir = outputFile.parentFile
                        if (dir.exists() || dir.mkdirs()) {
                            outputFile.createNewFile()
                        }
                    }
                    if (outputFile == null) {
                        downloadListener.onFailure()
//                        onFailure(
//                            generateHttpError(
//                                IOException("文件创建失败"),
//                                "文件创建失败",
//                                HttpError.ErrorType.ERROR_INVALID_VALUE
//                            )
//                        )
                        return
                    }

                    val body = ProgressResponseBody(response.body(), object : ProgressResponseBody.ProgressListener {
                        override fun onProgress(bytesRead: Long, contentLength: Long, done: Boolean) {
                            downloadListener.onProgress(bytesRead, contentLength, done)
                        }
                    })

                    inputStream = body.byteStream()
                    outputStream = FileOutputStream(outputFile)
                    val buffer = ByteArray(4096)
                    var length: Int

                    do {
                        length = inputStream!!.read(buffer)
                        if (length!=-1){
                            outputStream.write(buffer, 0, length)
                            progress += length
                        } else break
                    }while (true)
//                    while ((length = inputStream!!.read(buffer)) != -1) {
////                        outputStream.write(buffer, 0, length)
////                        progress += length
////                        postDownloadProgress(progress.toLong(), body.contentLength(), false)
////                        if (!isDownloading.get()) {
////                            if (inputStream != null) {
////                                try {
////                                    inputStream.close()
////                                } catch (e: IOException) {
////                                    e.printStackTrace()
////                                }
////
////                                inputStream = null
////                            }
////                            if (outputStream != null) {
////                                try {
////                                    outputStream.close()
////                                } catch (e: IOException) {
////                                    e.printStackTrace()
////                                }
////
////                                outputStream = null
////                            }
////                            FileUtilsLite.deleteFile(file)
////                            return
////                        }
//                    }
                    outputStream.flush()
//                    postDownloadProgress(progress.toLong(), body.contentLength(), true)
//                    if (mListener is FileRequestListener) {
//                        (mListener as FileRequestListener).onAsyncResponse(file)
//                    }
//                    postOnMain(Runnable { mListener.onResponse(file) })
                    //            postSuccess(request, file);
                    downloadListener.onSuccess()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    downloadListener.onFailure()
//                    onFailure(generateHttpError(e, "文件不存在", HttpError.ErrorType.ERROR_PARSE))
                } catch (e: IOException) {
                    e.printStackTrace()
                    downloadListener.onFailure()
//                    onFailure(generateHttpError(e, "IO错误", HttpError.ErrorType.ERROR_PARSE))
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }


//                //从响应体读取字节流
//                val data = response.body()?.bytes()
//                Log.i(MainActivity.TAG, "onResponse")
//                //由于当前处于非UI线程，所以切换到UI线程显示图片
////                    runOnUiThread {
////                        if (data != null) {
////                            img_download?.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.size))
////                        }
////                    }
            }

        })
    }

    interface SimpleDownloadListener {
        fun onProgress(bytesRead: Long, contentLength: Long, done: Boolean)
        fun onFailure()
        fun onSuccess()
    }
}