package com.example.slot.machine

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.slot.R
import com.example.slot.machine.slot.SlotEventListener
import com.example.slot.machine.slot.SlotOptions
import com.example.slot.machine.slot.SlotView
import kotlinx.android.synthetic.main.slot_machine_view.view.*
import java.util.*

class SlotMachine @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), SlotEventListener {


    private var score = 0
    private val slots = mutableListOf<SlotView>()
    private val slotsNumbers = mutableListOf<Int>()

    companion object {
        const val WIN_NUMBER = 7
    }


    init {
        View.inflate(context, R.layout.slot_machine_view, this)
        slots.add(firstLot)
        slots.add(secondLot)
        slots.add(thirdLot)
    }


    fun start() {
        val slotsOptions = generateSlotData()
        slots.forEachIndexed { index, slot -> generateSlot(slot, slotsOptions[index]) }
    }

    private fun generateSlotData(): List<SlotOptions> {
        val random = Random().nextInt(900) + 100

        val first = random / 100 % 10
        val second = random / 10 % 10
        val third = random % 10

        val list = mutableListOf<SlotOptions>()
        list.add(SlotOptions(finishValue = first, isPreviousWasFinished = true))
        list.add(SlotOptions(finishValue = second))
        list.add(SlotOptions(finishValue = third))

        return list
    }


    private fun generateSlot(slotView: SlotView, slotOptions: SlotOptions) {
        slotView.init(slotOptions, this)
    }


    override fun end(slot: SlotView, score: Int) {
        slotsNumbers.add(score)
        val index = slots.indexOfFirst { it == slot }
        if (index < slots.size.minus(1) && index >= 0) {
            val slotView = slots[index.plus(1)]
            slotView.previousFinished()
        } else {
            calculateScore()
            finishCycle()
        }


    }

    private fun calculateScore() {
        when (slotsNumbers.count { it == WIN_NUMBER }) {
            1 -> setScore(200)
            2 -> setScore(500)
            3 -> setScore(1000)
        }
    }

    private fun setScore(score: Int) {
        this.score = this.score + score
        machine_score.text = context.getString(R.string.score, this.score)
    }

    private fun finishCycle() {
        thirdLot.previousWasFinished = false
        secondLot.previousWasFinished = false
        slotsNumbers.clear()
    }


}