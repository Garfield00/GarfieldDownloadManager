package com.garfield.download

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.garfield.download.base.BaseActivity
import com.garfield.download.dialog.TipDialog
import com.garfield.download.permission.PermissionActivity
import com.garfield.download.permission.goToOpenPermissions
import com.garfield.download.utils.showTipDialog
import com.garfield.gallery.utils.ToastUtils
import com.garfield.gallery.utils.Utils
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Author chunliangwang
 * Date 2019-06-27
 * Description 封面Activity
 */


class CoverActivity : BaseActivity() {

    //是否已经检测过权限
    private val mCheckedPermissions = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
    }

    private fun requestPermissions() {
        if (PermissionActivity.isNeedRequestPermission(this, PermissionActivity.NECESSARY_PERMISSIONS)) {
            PermissionActivity.startObtainPermissions(this, PermissionActivity.NECESSARY_PERMISSIONS)
        } else {
            doWhenGotPermissions()
        }
    }

    private fun doWhenGotPermissions() {
        startMainActivity(this)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PermissionActivity.PERMISSION_REQUESTCODE -> {
                if (resultCode == PermissionActivity.PERMISSION_RESULTCODE_OK) {
                    doWhenGotPermissions()
                } else {
                    showRequestPermissionDialog(this)
                }
                mCheckedPermissions.set(true)
            }
            else -> {
            }
        }

    }

    private fun showRequestPermissionDialog(activity: Activity) {
        val permissionList = ArrayList<String>()
        for (permission in PermissionActivity.NECESSARY_PERMISSIONS) {
            if (!PermissionActivity.isPermissionReallyGranted(this, permission)) {
                permissionList.add(permission)
            }
        }

        if (Utils.isListEmpty(permissionList)) {
            activity.finish()
            ToastUtils.showToast(activity, "权限已全部申请成功")
            return
        }

        var permissionsStr = StringBuilder()
        for (permission in permissionList) {
            if (TextUtils.equals(Manifest.permission.RECORD_AUDIO, permission)) {
                permissionsStr.append("麦克风权限,")
                continue
            }

            if (TextUtils.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE, permission)) {
                permissionsStr.append("存储权限,")
                continue
            }

            if (TextUtils.equals(Manifest.permission.READ_PHONE_STATE, permission)) {
                permissionsStr.append("电话权限,")
            }
        }
        if (permissionsStr.isNotEmpty()) {
            permissionsStr = StringBuilder(permissionsStr.substring(0, permissionsStr.length - 1))
        }

        val dialog = showTipDialog(activity,
            activity.resources.getString(
                R.string.permissions_refused_tips,
                activity.resources.getString(R.string.app_name),
                permissionsStr
            ),
            object : TipDialog.TipDialogListener {
                override fun onConfirm(dialog: TipDialog) {
                    goToOpenPermissions(activity)
                }

                override fun onCancel(dialog: TipDialog) {
                    activity.finish()
                    mCheckedPermissions.set(false)
                }
            })
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun onResume() {
        super.onResume()
        if (mCheckedPermissions.get()) {//已经检测过了
            if (PermissionActivity.isNeedRequestPermission(this, PermissionActivity.NECESSARY_PERMISSIONS)) {
                showRequestPermissionDialog(this)
            } else {
                doWhenGotPermissions()
            }
        }

    }

}