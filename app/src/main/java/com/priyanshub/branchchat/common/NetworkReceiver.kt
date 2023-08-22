package com.priyanshub.branchchat.common

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.priyanshub.branchchat.R
import javax.inject.Inject

class NetworkReceiver @Inject constructor() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.DISCONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.DISCONNECTED
            ) {
                showAlertDialog(context)
            }
        } catch (t: Throwable) {
            Log.e("Network", "Network Status Failed: ", t)
        }
    }

    private fun showAlertDialog(context: Context){
        AlertDialog.Builder(context)
            .setIcon(R.drawable.ic_alert_circle)
            .setTitle("Internet Connection Alert")
            .setCancelable(false)
            .setMessage("Please Check Your Internet Connection")
            .setPositiveButton(
                "Mobile Data"
            ) { _, i ->
                try {
                    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        Intent(android.provider.Settings.ACTION_DATA_USAGE_SETTINGS)
                    } else {
                       val i = Intent()
                        i.component = ComponentName("com.android.settings", "com.android.settings.Settings\$DataUsageSummaryActivity")
                        i
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton(
                "Wifi"
            ){ _, i ->
                try {
                    val intent = Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .show()
    }
}