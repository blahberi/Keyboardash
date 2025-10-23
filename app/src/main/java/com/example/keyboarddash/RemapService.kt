package com.example.keyboarddash

import android.accessibilityservice.AccessibilityService
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

class RemapService : AccessibilityService() {
    private val keys = setOf(
        KeyEvent.KEYCODE_NUMPAD_5,
    )

    private lateinit var player: Player

    override fun onServiceConnected() {
        super.onServiceConnected()

        player = Player(this)
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        if (event?.keyCode !in keys) {
            return super.onKeyEvent(event)
        }
        when (event?.keyCode) {
            KeyEvent.KEYCODE_NUMPAD_5 -> handleJump(event)
            KeyEvent.KEYCODE_ESCAPE -> handlePause(event)
        }
        return true
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) { }

    override fun onInterrupt() {
        player.stopJumping()
    }

    private fun handleJump(event: KeyEvent) {
        when (event.action) {
            KeyEvent.ACTION_DOWN -> player.startJumping()
            KeyEvent.ACTION_UP -> player.stopJumping()
        }
    }

    private fun handlePause(event: KeyEvent) {
        when (event.action) {
            KeyEvent.ACTION_DOWN -> player.pauseGame()
        }
    }

}