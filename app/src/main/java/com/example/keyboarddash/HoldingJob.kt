package com.example.keyboarddash

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class HoldingJob (
    private val service: AccessibilityService,
    path: Path
) {
    private val isHolding = AtomicBoolean(false)
    private val job = AtomicReference<Job?>(null)

    private val gesture = GestureDescription.Builder()
        .addStroke(GestureDescription.StrokeDescription(path, 0, 16))
        .build()


    fun start() {
        if (!isHolding.compareAndSet(false,true)) {
            return
        }

        val holdingJob = CoroutineScope(Dispatchers.Main).launch {
            while (isHolding.get()) {
                dispatchTap()
                delay(100)
            }
        }

        job.getAndSet(holdingJob)?.cancel()
    }

    fun stop() {
        job.getAndSet(null)?.cancel()
        isHolding.set(false)
    }

    private fun dispatchTap() {
        service.dispatchGesture(gesture, null, null)
    }
}