package com.garfield.download.permission

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import com.garfield.download.base.BaseActivity
import com.garfield.gallery.utils.ToastUtils
import com.garfield.gallery.utils.Utils
import java.util.*

/**
 * Author chunliangwang
 * Date 2019-06-27
 * Description
 */

class PermissionActivity : BaseActivity() {

    companion object {
        val TAG = "PermissionActivity"

        val PERMISSION_REQUESTCODE = 1111
        val PERMISSION_RESULTCODE_OK = 0
        val PERMISSION_RESULTCODE_ERROR = 1

        private val MY_PERMISSIONS_REQUEST_CALL_PHONE = 1
        //private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;
        private val MY_PERMISSIONS_REQUEST_CALL_ALL = 3

        val OPSTR_READ_EXTERNAL_STORAGE = "android:read_external_storage"
        val OPSTR_WRITE_EXTERNAL_STORAGE = "android:write_external_storage"
        val OPSTR_FINE_LOCATION = "android:fine_location"
        val OPSTR_READ_PHONE_STATE = "android:read_phone_state"
        val OPSTR_PROCESS_OUTGOING_CALLS = "android:process_outgoing_calls"
        val OPSTR_RECORD_AUDIO = "android:record_audio"
        val OPSTR_CAMERA = "android:camera"
        val OPSTR_READ_CONTACTS = "android:read_contacts"

        /*必须有的权限，没有的话，不能启动的*/
        val NECESSARY_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        /*拍照所需要的权限*/
        val CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        //附加权限，不给也能启动的
        var permissionsNotNecessary = arrayOf(Manifest.permission.READ_CONTACTS)

        /*所请求的权限*/
        val REQUESTED_PERMISSIONS = "requested_permissions"

        val permissionMap: MutableMap<String, String> = HashMap()

        init {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            }
            permissionMap[Manifest.permission.READ_EXTERNAL_STORAGE] = OPSTR_READ_EXTERNAL_STORAGE
            permissionMap[Manifest.permission.WRITE_EXTERNAL_STORAGE] = OPSTR_WRITE_EXTERNAL_STORAGE
            permissionMap[Manifest.permission.ACCESS_FINE_LOCATION] = OPSTR_FINE_LOCATION
            permissionMap[Manifest.permission.READ_PHONE_STATE] = OPSTR_READ_PHONE_STATE
            permissionMap[Manifest.permission.PROCESS_OUTGOING_CALLS] = OPSTR_PROCESS_OUTGOING_CALLS
            permissionMap[Manifest.permission.RECORD_AUDIO] = OPSTR_RECORD_AUDIO
            permissionMap[Manifest.permission.CAMERA] = OPSTR_CAMERA
            permissionMap[Manifest.permission.READ_CONTACTS] = OPSTR_READ_CONTACTS
        }

        fun isNeedRequestPermission(context: Activity, permissions: Array<String>): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Utils.isStringArrayEmpty(permissions))
                return false
            for (permission in permissions) {
                if (!permission?.let { isPermissionReallyGranted(context, it) }!!) return true
            }
            return false
        }

        /*两种方式判断是否获取了权限 个别如小米手机会出现问题*/
        fun isPermissionReallyGranted(context: Context, permission: String): Boolean {
            return isPermissionGranted(context, permission) && getPermissionResult(context, permission)
        }

        private fun isPermissionGranted(context: Context, permission: String): Boolean {
            return !TextUtils.isEmpty(permission) && ActivityCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        private fun getPermissionResult(context: Context, permissionName: String?): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return true
            val permissionStr = permissionMap[permissionName]
            val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            //默认为true 否则容易引发莫名的提示
            val checkResult = appOpsManager.checkOpNoThrow(
                permissionStr, Binder.getCallingUid(), context.packageName
            )
            return checkResult == AppOpsManager.MODE_ALLOWED
        }

        fun startObtainPermissions(context: Activity, permissions: Array<String>) {
            val intent = Intent(context, PermissionActivity::class.java)
            intent.putExtra(REQUESTED_PERMISSIONS, permissions)
            context.startActivityForResult(intent, PERMISSION_REQUESTCODE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //声明一个集合，在后面的代码中用来存储用户拒绝授权的权限
        val permissionList = ArrayList<String>()
        val requestedPermissions = getRequestedPermissions(intent)

        var permissionsToCheck = NECESSARY_PERMISSIONS
        if (Utils.isStringArrayNotEmpty(requestedPermissions)) {
            permissionsToCheck = requestedPermissions
        }

        for (permission in permissionsToCheck) {
            if (!isPermissionReallyGranted(this, permission)) {
                permissionList.add(permission)
            }
        }

        if (permissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            //Toast.makeText(this,"已经授权",Toast.LENGTH_LONG).showChat();
            setResult(PERMISSION_RESULTCODE_OK)
            this.finish()
            return
        }

        ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), MY_PERMISSIONS_REQUEST_CALL_ALL)
    }

    /*获取传过来的需要请求的权限列表*/
    private fun getRequestedPermissions(intent: Intent?): Array<String> {
        val emptyPermissions = arrayOf<String>()
        if (intent == null || !intent.hasExtra(REQUESTED_PERMISSIONS)) return emptyPermissions
        val permissions = intent.getStringArrayExtra(REQUESTED_PERMISSIONS)
        return if (permissions == null || permissions.isEmpty()) emptyPermissions else permissions
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            ToastUtils.showToast(this, if (grantResults[0] == PackageManager.PERMISSION_GRANTED) "权限已申请" else "权限已拒绝")
        } else if (requestCode == MY_PERMISSIONS_REQUEST_CALL_ALL) {
            /*for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                    if (showRequestPermission) {
                        showToast("权限未申请");
                    } else {
                        showToast("申请权限被拒绝了,且勾选了禁止后不再询问"*//*+gotoDefultPermissionSetting()*//*);
                    }
                    setResult(PERMISSION_RESULTCODE_ERROR);
                    this.finish();
                    return;
                }
            }*/
            if (isNeedRequestPermission(this@PermissionActivity, permissions)) {
                //必须权限没有给，跳转到系统设置页面
                //                isStartSetting = true;
                //                ToastUtils.showToast(this, "请开启所有权限，以便使用所有功能");
                //                for (String permission : permissions) {
                //                    LivingLog.d(TAG, "权限不完整:name:", permission, "granted:", isPermissionGranted(this, permission), "PermissionResult:", getPermissionResult(this, permission));
                //                }
                //                gotoDefultPermissionSetting();
                setResult(PERMISSION_RESULTCODE_ERROR)
                finish()
                return
            }
            setResult(PERMISSION_RESULTCODE_OK)
            finish()
        }

    }

}

/*跳转到设置页面 用于打开各种权限等*/
fun goToOpenPermissions(activity: Context) {
    // 进入设置系统应用权限界面
    val localIntent = Intent()
    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
    localIntent.data = Uri.fromParts("package", activity.getPackageName(), null)
    activity.startActivity(localIntent)
}
