package mufanc.tools.myandroidtools.utils

import java.text.Collator
import java.util.*

object FilterHelper {
    var queryString = ""
    var hideSystemApps = true
    var sortBy = SortBy.APP_NAME
    var reverse = false

    fun judge(info: AppInfoHelper.AppInfo): Boolean {
        if (info.isSystemApp && hideSystemApps) return false
        if (queryString == "") return true
        val query = queryString.lowercase()
        if (info.uid == query.toIntOrNull()) return true
        if (info.appName.lowercase().contains(query) ||
                info.packageName.lowercase().contains(query)) {
            return true
        }
        return false
    }

    fun sort(appList: MutableList<AppInfoHelper.AppInfo>) {
        appList.sortWith { o1, o2 ->
            when (sortBy) {
                SortBy.APP_NAME -> Collator.getInstance(Locale.getDefault()).compare(o1.appName, o2.appName)
                SortBy.UPDATE_TIME -> -compareValues(o1.updateTime, o2.updateTime)
                SortBy.UID -> compareValues(o1.uid, o2.uid)
            }
        }
        if (reverse) appList.reverse()
    }
}

enum class SortBy {
    APP_NAME, UPDATE_TIME, UID
}
