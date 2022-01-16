package mufanc.tools.myandroidtools.utils

import android.content.ComponentName
import android.content.pm.ApplicationInfo
import android.content.pm.ComponentInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import mufanc.tools.myandroidtools.MyApplication

object AppInfoHelper {
    data class AppInfo(
        val appName: String,
        val packageName: String,
        val icon: Drawable,
        val isSystemApp: Boolean,
        val updateTime: Long,
        val uid: Int,
        var enabled: Boolean
    ) {
        constructor(packageManager: PackageManager, info: PackageInfo) : this(
            info.applicationInfo.loadLabel(packageManager).toString(),
            info.packageName,
            info.applicationInfo.loadIcon(packageManager),
            info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0,
            info.lastUpdateTime,
            info.applicationInfo.uid,
            setOf(
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
            ).contains(packageManager.getApplicationEnabledSetting(info.packageName))
        )

        fun iconCopy(): Drawable? {
            return icon.constantState?.newDrawable()
        }
    }

    data class CompInfo(
        val name: String,
        val simpleName: String,
        val enabled: Boolean
    ) {
        constructor(packageManager: PackageManager, info: ComponentInfo) : this(
            info.name,
            info.name.split(".").let { it[it.size-1] },
            packageManager.getComponentEnabledSetting(
                ComponentName(info.packageName, info.name)
            ).let {
                it != PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }
        )
    }

    val packageManager: PackageManager = MyApplication.appContext.packageManager

    private lateinit var appInfoList: MutableList<AppInfo>

    @Synchronized
    fun getAppInfoList(reload: Boolean = false): List<AppInfo> {
        if (::appInfoList.isInitialized && !reload) return appInfoList
        appInfoList = mutableListOf()

        packageManager.getInstalledPackages(0).forEach { info ->
            AppInfo(packageManager, info).let { appInfoList.add(it) }
        }
        return appInfoList
    }

    @Synchronized
    fun getAppInfo(packageName: String): AppInfo {
        return appInfoList.find {
            it.packageName == packageName
        } ?: AppInfo(
            packageManager,
            packageManager.getPackageInfo(packageName, 0)
        ).also {
            appInfoList.add(it)
        }
    }

    @Synchronized
    fun updateAppInfo(packageName: String) {
        appInfoList.find {
            it.packageName == packageName
        }?.apply {
            enabled = setOf(
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
            ).contains(packageManager.getApplicationEnabledSetting(packageName))
        }
    }

    fun getComponentList(packageName: String, type: Int): List<CompInfo> {
        return packageManager.getPackageInfo(
            packageName,
            type or PackageManager.MATCH_DISABLED_COMPONENTS
        ).run {
            when (type) {
                PackageManager.GET_SERVICES -> services
                PackageManager.GET_RECEIVERS -> receivers
                PackageManager.GET_ACTIVITIES -> activities
                PackageManager.GET_PROVIDERS -> providers
                else -> throw RuntimeException("Unknown component type!")
            }?.map {
                CompInfo(packageManager, it)
            } ?: listOf()
        }
    }
}