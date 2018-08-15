package com.moyuruaizawa.celsiussample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.moyuruaizawa.celsius.Celsius
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

  var timer: Timer? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val celsius = Celsius()
    Timer().also { timer = it }.scheduleAtFixedRate(object : TimerTask() {
      override fun run() {
        celsius.temperatures
            .map { (type, temp) -> "$type: $temp" }
            .reduce { acc, s -> "$acc\n$s" }
            .let { Log.i("TEMP", "\n$it") }
      }
    }, 0, 10000)
  }

  override fun onDestroy() {
    timer?.cancel()
    super.onDestroy()
  }
}
