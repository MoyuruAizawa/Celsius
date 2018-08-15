package com.moyuruaizawa.celsius

import java.io.BufferedReader
import java.io.InputStreamReader

class Celsius {
  val temperatures: List<Temperature>
    get() = exec("ls sys/class/thermal/")
        .sortedBy { it.removePrefix("thermal_zone").toIntOrNull() }
        .mapNotNull(this::loadTemperature)

  private fun loadTemperature(dir: String): Temperature? {
    val type = exec("cat sys/class/thermal/$dir/type").firstOrNull() ?: return null
    val temp = exec("cat sys/class/thermal/$dir/temp").firstOrNull()?.toFloatOrNull() ?: return null

    return when {
      temp <= 0 -> null
      temp < 200 -> Temperature(type, temp)
      else -> Temperature(type, temp / 1000f)
    }
  }

  private fun exec(command: String): List<String> =
      Runtime.getRuntime()
          .exec(command)
          .use {
            waitFor()
            BufferedReader(InputStreamReader(inputStream)).readLines()
          } ?: emptyList()

  private inline fun <R> Process.use(block: Process.() -> R): R? = try {
    block()
  } catch (t: Throwable) {
    null
  } finally {
    destroy()
  }
}