package app.joelauncher.helper

import app.joelauncher.data.AppModel

interface AppFilterHelper {
    fun onAppFiltered(items:List<AppModel>)
}