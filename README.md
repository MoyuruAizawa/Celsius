# Celsius
Hardware Thermometer for Android. [See also](https://www.kernel.org/doc/Documentation/thermal/sysfs-api.txt)  

# Usage
```
implementation 'com.moyuru:celcius:0.1.0'
```

```kotlin
val celsius = Celsius()
Timer().also { timer = it }.scheduleAtFixedRate(object : TimerTask() {
  override fun run() {
    celsius.temperatures
        .map { (type, temp) -> "$type: $temp" }
        .fold("") { acc, s -> "$acc\n$s" }
        .let { Log.i("TEMP", "\n$it") }
  }
}, 0, 5000)
```
