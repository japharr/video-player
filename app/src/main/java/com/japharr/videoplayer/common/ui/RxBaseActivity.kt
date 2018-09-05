package com.japharr.videoplayer.common.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

/**
 * Created by Japharr on 8/5/2018.
 */
@SuppressLint("Registered")
open class RxBaseActivity: AppCompatActivity(), EasyPermissions.PermissionCallbacks{
    open val TAG = RxBaseActivity::class.java.simpleName!!

    protected var subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //defSharedPref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @AfterPermissionGranted(REQUEST_PERMISSION_EXTERNAL_STORAGE)
    fun needStoragePermission() {
        val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            //sendSMS();
        } else {
            // Request the SEND_SMS permission via a user dialog
            EasyPermissions.requestPermissions(
                    PermissionRequest.Builder(this, REQUEST_PERMISSION_EXTERNAL_STORAGE, *perms)
                            //.setRationale(R.string.rationale_ask_again)
                            //.setPositiveButtonText(android.R.string.ok)
                            //.setNegativeButtonText(android.R.string.cancel)
                            .build())
        }/**/
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size)

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Log.d(TAG, "somePermissionPermanentlyDenied:" + requestCode + ":" + perms.size)

            //AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size)

    }

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeDisposable()
    }

    override fun onPause() {
        super.onPause()
        subscriptions.clear()
    }

    companion object {
        const val REQUEST_PERMISSION_EXTERNAL_STORAGE = 10001
        const val REQUEST_PERMISSION_EXTERNAL_STORAGE_ACTION = 10002
        const val SAF_PERMISSION = 10003
    }
}