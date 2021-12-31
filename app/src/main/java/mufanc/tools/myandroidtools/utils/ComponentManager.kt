package mufanc.tools.myandroidtools.utils

import android.content.ComponentName
import android.content.pm.IPackageManager
import android.content.pm.PackageManager
import android.os.Process
import android.os.ServiceManager
import android.widget.Toast
import mufanc.tools.myandroidtools.BuildConfig
import mufanc.tools.myandroidtools.MyApplication
import mufanc.tools.myandroidtools.R
import rikka.shizuku.ShizukuBinderWrapper

object ComponentManager {
    private lateinit var ipm: IPackageManager

    private fun init(): Boolean {
        if (ComponentManager::ipm.isInitialized) return true
        if (!MyApplication.checkPermission()) {
            Toast.makeText(MyApplication.appContext, R.string.sui_unavailable, Toast.LENGTH_SHORT).show()
            return false
        }
        ipm = IPackageManager.Stub.asInterface(
            ShizukuBinderWrapper(ServiceManager.getService("package"))
        )
        return true
    }

    fun setApplicationEnabledSetting(packageName: String, newState: Int) {
        if (init()) {
            ipm.setApplicationEnabledSetting(
                packageName,
                newState,
                0,
                Process.myUserHandle().hashCode(),
                BuildConfig.APPLICATION_ID
            )
        }
    }

    fun setComponentEnabledSetting(componentName: ComponentName, newState: Int) {
        if (init()) {
            ipm.setComponentEnabledSetting(
                componentName,
                newState,
                0,
                Process.myUserHandle().hashCode()
            )
        }
    }
}