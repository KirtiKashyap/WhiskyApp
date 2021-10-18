package com.aadya.whiskyapp.utils

import android.content.Context
import android.net.ConnectivityManager

class Connection {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    companion object {
        private var connection: Connection? = null
        val instance: Connection?
            get() {
                if (connection == null) connection = Connection()
                return connection
            }
    }
}