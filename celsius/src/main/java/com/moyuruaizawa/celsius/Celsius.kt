package com.moyuruaizawa.celsius

import java.io.BufferedReader
import java.io.InputStreamReader

class Celsius {
  val temperatures: List<Pair<String, Float>>
    get() = exec("ls sys/class/thermal/")
        .mapNotNull(this::loadTemperature)

  private fun loadTemperature(dir: String): Pair<String, Float>? {
    val type = exec("cat sys/class/thermal/$dir/type").firstOrNull() ?: return null
    val temp = exec("cat sys/class/thermal/$dir/temp").firstOrNull()?.toFloatOrNull() ?: return null

    return when {
      temp <= 0.0f -> null
      temp >= 999 -> type to temp / 1000f
      else -> type to temp
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