package com.ml.flipclock.vm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CancellationException

class MainViewModel : ViewModel() {
    val screenState = mutableStateOf(0)
    val timeStatusFlow = MutableStateFlow(TimeStatus.new())

   suspend fun loop(){
       timeStatusFlow.emit(TimeStatus.new())
       delay(1000)
   }
}

data class TimeStatus(
    val dateTime: LocalDateTime,
    val hour: Int = dateTime.hour,
    val minute: Int = dateTime.minute,
    val second: Int = dateTime.second,
    val dayOfWeek: String = dateTime.dayOfWeek.name,
) {
    companion object {
        fun new(): TimeStatus {
            return TimeStatus(LocalDateTime.now())
        }
    }

    val dateStr: String
        get() {
            return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)
        }

}