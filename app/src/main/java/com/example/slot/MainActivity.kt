package com.example.slot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), EventListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        start_btn.setOnClickListener {

            val random = Random().nextInt(900) + 100

            val first = random / 100 % 10
            val second = random / 10 % 10
            val third = random % 10

            generateRandom(first_lot, first)
            first_lot.previousWasFinished = true
            generateRandom(second_lot, second)
            generateRandom(third_lot, third)

        }

        first_lot.setEventListener(this)
        second_lot.setEventListener(this)
        third_lot.setEventListener(this)

    }

    private fun generateRandom(slotView: SlotView, finishValue: Int) {
        slotView.finishValue = finishValue
        slotView.startSlot()
    }

    override fun end(slot: SlotView) {

        if (slot == first_lot) second_lot.previousFinished()


        if (slot == second_lot)  third_lot.previousFinished()


        if (slot == third_lot) {
            third_lot.previousWasFinished = false
            second_lot.previousWasFinished = false
        }


    }
}
