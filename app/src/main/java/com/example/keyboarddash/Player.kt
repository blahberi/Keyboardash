package com.example.keyboarddash

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import kotlinx.coroutines.Job
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class Player(
    private val service: AccessibilityService,
) {
    private val isHolding = AtomicBoolean(false)
    private val isPaused: AtomicBoolean = AtomicBoolean(false)

    private val screenWidth: Int
        get() = service.resources.displayMetrics.widthPixels

    private val screenHeight: Int
        get() = service.resources.displayMetrics.heightPixels

    private val centerX: Float
        get() = screenWidth / 2f

    private val centerY: Float
        get() = screenHeight / 2f


    fun startJumping() {
        if (!isHolding.compareAndSet(false, true)) {
            return
        }

        dispatchHold()
    }

    fun stopJumping() {
        if (!isHolding.get()) {
            return
        }
        dispatchRelease()
        isHolding.set(false)
    }

    fun pauseGame() {
        val wasPaused = isPaused.getAndSet(!isPaused.get())
        if (wasPaused) {
            dispatchPause()
        } else {
            dispatchResume()
        }
    }

    private fun createPath(x: Float, y: Float) = Path().apply {
        moveTo(x, y)
    }

    private fun dispatchGesture(x: Float, y: Float, duration: Long, willContinue: Boolean = false) {
        val path = createPath(x, y)
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, duration, willContinue))
            .build()
        service.dispatchGesture(gesture, null, null)
    }

    private fun dispatchHold() {
        dispatchGesture(centerX, centerY, 1, willContinue = true)
    }

    private fun dispatchRelease() {
        dispatchGesture(0f, 0f, 1, willContinue = false)
    }

    private fun dispatchPause() {
        dispatchGesture(screenWidth - 50f, 50f, 50)
    }

    private fun dispatchResume() {
        dispatchGesture(centerX, centerY, 32)
    }
}