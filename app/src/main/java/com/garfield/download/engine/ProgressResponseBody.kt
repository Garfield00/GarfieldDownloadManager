package com.garfield.download.engine

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 * Author chunliangwang
 * Date 2019-08-05
 * Description 下载进度处理
 */

class ProgressResponseBody(
    private val responseBody: ResponseBody?,
    private val progressListener: ProgressListener
) : ResponseBody() {

    private lateinit var bufferedSource: BufferedSource//读取响应体时的缓冲区

    override fun contentLength(): Long {
        return responseBody!!.contentLength()
    }

    override fun contentType(): MediaType? {
        return responseBody!!.contentType()
    }

    override fun source(): BufferedSource {
        if (!this::bufferedSource.isInitialized) {
            bufferedSource = Okio.buffer(source(responseBody!!.source()))
        }
        return bufferedSource
    }

    /*构建ForwardSource*/
    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            private var progress = 0L
            override fun read(sink: Buffer, byteCount: Long): Long {
                //读缓冲区的数据，得到读了多少字节
                val bytesRead = super.read(sink, byteCount)
                if (bytesRead != -1L) this.progress += bytesRead
                progressListener.onProgress(this.progress, responseBody!!.contentLength(), bytesRead == -1L)
                return bytesRead
            }
        }
    }

    interface ProgressListener {
        fun onProgress(bytesRead: Long, contentLength: Long, done: Boolean)
    }
}

