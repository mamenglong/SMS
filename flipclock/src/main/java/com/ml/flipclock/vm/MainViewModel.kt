package com.ml.flipclock.vm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CancellationException

class MainViewModel : ViewModel() {
    val screenState = mutableStateOf(0)
    val hour = mutableStateOf(0)
    val minute = mutableStateOf(0)
    val second = mutableStateOf(0)
    var localDateTime = LocalDateTime.now()
    var dateStr = mutableStateOf("")
    var dayOfWeek = mutableStateOf("")
    var job: Job? = null
    fun onResume() {
        job = viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("MainViewModel", "start:$throwable")
        }) {
            while (true) {
                ensureActive()
                localDateTime = LocalDateTime.now()
                hour.value = localDateTime.hour
                minute.value = localDateTime.minute
                second.value = localDateTime.second
                dateStr.value = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)
                dayOfWeek.value = localDateTime.dayOfWeek.name
                delay(1000)
            }
        }
    }

    fun onStop() {
        job?.cancel(CancellationException("finish"))
        job = null
    }
}