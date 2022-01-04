package mufanc.tools.myandroidtools.utils

import android.app.IActivityManager
import android.graphics.drawable.Drawable
import android.os.ProcessRef
import android.os.ServiceManager
import mufanc.tools.myandroidtools.MyApplication
import rikka.shizuku.ShizukuBinderWrapper

object ProcessInfoHelper {
    data class ProcessInfo(
        val name: String,
        val pid: Int,
        val uid: Int,
        val isolated: Boolean,
        val packageName: String,
        val memory: Long,
        val icon: Drawable
    )

    private lateinit var iam: IActivityManager

    private fun init(): Boolean {
        if (::iam.isInitialized) return true
        if (!MyApplication.checkPermission()) return false
        iam = IActivityManager.Stub.asInterface(
            ShizukuBinderWrapper(ServiceManager.getService("activity"))
        )
        return true
    }

    fun getRunningAppProcesses(): List<ProcessInfo> {
        if (init()) {
            return iam.runningAppProcesses.map {
                val realUid = MyApplication.companionService.getProcessUid(it.pid)
                ProcessInfo(
                    it.processName,
                    it.pid,
                    realUid,
                    ProcessRef.isIsolated(realUid),
                    it.pkgList[0],
                    iam.getProcessPss(intArrayOf(it.pid))[0],
                    AppInfoHelper.getAppInfo(it.pkgList[0]).icon
                )
            }
        }
        return emptyList()
    }
}