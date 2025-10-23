package com.example.keyboarddash

import android.accessibilityservice.AccessibilityService
import android.graphics.Path
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

class RemapService : AccessibilityService() {
    private val center = Path().apply {
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val centerX = screenWidth / 2f
        val centerY = screenHeight / 2f
        moveTo(centerX, centerY)
    }

    private val holdingJob = HoldingJob(this, center)

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        if (event?.keyCode != KeyEvent.KEYCODE_NUMPAD_0) {
            return super.onKeyEvent(event)
        }
        when (event.action) {
            KeyEvent.ACTION_DOWN -> holdingJob.start()
            KeyEvent.ACTION_UP -> holdingJob.stop()
        }
        return true
    }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) { }

    override fun onInterrupt() {
    }

}