package com.example.slot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), EventListener {


    var countDown = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        start_btn.setOnClickListener {

            val random = Random().nextInt(500 - 300) + 300

            val first = random / 100 % 10
            val second = random / 10 % 10
            val third = random % 10

            generateRandom(one_lot, first)
            generateRandom(second_lot, second)
            generateRandom(third_lot, third)

        }

        one_lot.setEventListener(this)
        second_lot.setEventListener(this)
        third_lot.setEventListener(this)

    }

    private fun generateRandom(slotView: SlotView, finishValue: Int) {
        slotView.setFinishValue(finishValue)
        slotView.setRandomValue(0, (Random().nextInt(500 - 300) + 300).toLong())

    }

    override fun end(result: Int) {
        if (result < 2) countDown++
        else countDown = 0

    }
}
