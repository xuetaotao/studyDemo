package com.example.studydemo.thirdLibrary

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX

/**
 * 官方文档：
 * https://developer.android.com/guide/topics/permissions/overview#normal_permissions
 * https://developer.android.com/reference/android/Manifest.permission
 * Android 将权限分为不同的类型，包括安装时权限（包括一般权限和签名权限）、运行时权限和特殊权限。
 */
@SuppressWarnings("unused")
object PermissionXStudy {

    fun permissionXTest(fragment: Fragment) {
        PermissionX.init(fragment)
            //Protection level: dangerous
            .permissions(android.Manifest.permission.READ_CONTACTS)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(
                        fragment.context,
                        "All permissions are granted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        fragment.context,
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun permissionXTest(fragmentActivity: FragmentActivity) {
        PermissionX.init(fragmentActivity)
            .permissions(android.Manifest.permission.READ_CONTACTS)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(
                        fragmentActivity,
                        "All permissions are granted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        fragmentActivity,
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}