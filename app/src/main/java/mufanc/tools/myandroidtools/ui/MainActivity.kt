package mufanc.tools.myandroidtools.ui

import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import mufanc.tools.myandroidtools.BuildConfig
import mufanc.tools.myandroidtools.ICompanionService
import mufanc.tools.myandroidtools.MyApplication
import mufanc.tools.myandroidtools.R
import mufanc.tools.myandroidtools.databinding.ActivityMainBinding
import mufanc.tools.myandroidtools.utils.CompanionService
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private fun init() {
        bindCompanionService()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with (binding) {
            val navController = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_activity_main)
                .let { (it as NavHostFragment).navController }
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_process, R.id.navigation_about
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        try {
            if (MyApplication.checkPermission()) init()
            else {
                Shizuku.addRequestPermissionResultListener { _, result ->
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        init()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("测试")
                            .show()
                    }
                }
            }
        } catch (err: Throwable) {
            Log.e("TAG", "", err)
        }

    }

    private fun bindCompanionService() {
        Shizuku.bindUserService(
            Shizuku.UserServiceArgs(ComponentName(BuildConfig.APPLICATION_ID, CompanionService::class.java.name))
                .daemon(false)
                .processNameSuffix("helper")
                .debuggable(BuildConfig.DEBUG)
                .version(BuildConfig.VERSION_CODE),
            object : ServiceConnection {
                override fun onServiceConnected(comp: ComponentName, binder: IBinder?) {
                    MyApplication.companionService = ICompanionService.Stub.asInterface(binder)
                }

                override fun onServiceDisconnected(comp: ComponentName?) = Unit
            }
        )
    }
}
