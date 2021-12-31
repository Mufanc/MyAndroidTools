package mufanc.tools.myandroidtools

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.shizuku.Shizuku
import rikka.sui.Sui

class MyApplication : Application() {
    companion object {
        lateinit var appContext: Context

        fun checkPermission(): Boolean {
            if (Sui.init(BuildConfig.APPLICATION_ID)) {
                try {
                    if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                        return true
                    }
                    if (!Shizuku.shouldShowRequestPermissionRationale()) {
                        Shizuku.requestPermission(0)
                    }
                    return true
                } catch (err: Throwable) {
                    return false
                }
            }
            return false
        }
    }

    override fun onCreate() {
        super.onCreate()
        HiddenApiBypass.addHiddenApiExemptions("")
        appContext = applicationContext
        checkPermission()
    }
}