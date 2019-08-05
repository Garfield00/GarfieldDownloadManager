package com.garfield.download

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.garfield.download.base.BaseActivity
import com.garfield.download.simpledownload.SimpleDownloadManager
import java.io.File


class MainActivity : BaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private var probar_download: ProgressBar? = null
    private var tv_progress: TextView? = null
    private var img_download: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        probar_download = findViewById(R.id.probar_download)
        tv_progress = findViewById(R.id.tv_progress)
        img_download = findViewById(R.id.img_download)

        findViewById<Button>(R.id.btn_download).setOnClickListener {
            val url =
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565008253383&di=4809bc8d8037df29caecb1add3aded23&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201210%2F24%2F20121024114802_sVwSR.jpeg"
            val outputFile =
                File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "garfielddownload.jpg")
            SimpleDownloadManager.instance.downloadFile(url, outputFile, object :
                SimpleDownloadManager.SimpleDownloadListener {
                override fun onProgress(bytesRead: Long, contentLength: Long, done: Boolean) {
                    Log.i(TAG, "onProgress:bytesRead:$bytesRead:contentLength:$contentLength")
                    runOnUiThread {
                        tv_progress?.text = "$bytesRead/$contentLength"
                        probar_download?.max = contentLength.toInt()
                        probar_download?.progress = bytesRead.toInt()
                    }
                }

                override fun onFailure() {
                    Log.i(TAG, "onFailure")
                }

                override fun onSuccess() {
                    Log.i(TAG, "onSuccess")
                }
            })
        }

    }

}

fun startMainActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}

