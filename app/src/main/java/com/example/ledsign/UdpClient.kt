package com.example.ledsign

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UdpClient {
    private var async_cient: AsyncTask<Void?, Void?, Void?>? = null
    var Message: String? = null
    private val port = 4210
    private val serverAddr = InetAddress.getByName("192.168.4.1")

    @SuppressLint("NewApi")
    fun sendUdp() {
        async_cient = object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                var ds: DatagramSocket? = null
                try {
                    ds = DatagramSocket()
                    val dp: DatagramPacket
                    dp = DatagramPacket(
                        Message!!.toByteArray(),
                        Message!!.length,
                        serverAddr,
                        port
                    )
                    ds.broadcast = true
                    ds.send(dp)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    ds?.close()
                }
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
            }
        }
        if (Build.VERSION.SDK_INT >= 11) async_cient?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }
}