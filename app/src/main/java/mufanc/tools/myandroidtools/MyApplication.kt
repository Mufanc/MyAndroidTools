package mufanc.tools.myandroidtools

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.shizuku.Shizuku
import rikka.sui.Sui

class MyApplication : Application() {
    companion object {
        lateinit var appContext: Context

        lateinit var companionService: ICompanionService

        fun checkPermission(): Boolean {
            fun internal(): Boolean {
                if (Sui.init(BuildConfig.APPLICATION_ID)) {
                    try {
                        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                            return true
                        }
                        if (!Shizuku.shouldShowRequestPermissionRationale()) {
                            Shizuku.requestPermission(0)
                        }
                    } catch (err: Throwable) { }
                }
                return false
            }
            return internal().also {
                if (!it) {
                    Toast.makeText(appContext, R.string.sui_unavailable, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        HiddenApiBypass.addHiddenApiExemptions("")
        appContext = applicationContext
    }
}