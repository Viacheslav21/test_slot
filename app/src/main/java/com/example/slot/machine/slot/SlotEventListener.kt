package com.example.slot.machine.slot

interface SlotEventListener {
    fun end(slot: SlotView, score: Int)
}