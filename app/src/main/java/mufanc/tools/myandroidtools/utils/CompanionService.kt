package mufanc.tools.myandroidtools.utils

import mufanc.tools.myandroidtools.ICompanionService
import java.io.File
import kotlin.system.exitProcess

class CompanionService : ICompanionService.Stub() {
    override fun destroy() {
        exitProcess(0)
    }

    override fun getProcessUid(pid: Int): Int {
        val status = File("/proc/$pid/status").readText()
        "Uid:\\s*(\\d+)".toRegex().findAll(status).forEach {
            return it.groupValues[1].toInt()
        }
        return 0
    }
}