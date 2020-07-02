package com.example.slot.machine.slot

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.example.slot.R
import kotlinx.android.synthetic.main.slot_view.view.*
import java.util.*


class SlotView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var text: AppCompatTextView
    private var nextText: AppCompatTextView

    private var finishValue: Int = 0
    var previousWasFinished = false
    private var circle = 0

    private var number: Int

    private var circleCounter = mutableSetOf<Int>()

    companion object {
        private const val MAX_ANIM_DURATION = 400L
        private const val MIN_ANIM_DURATION = 550L
        private const val MIN_CIRCLES = 1
    }

    init {
        val view = View.inflate(context, R.layout.slot_view, this)
        text = view.text_lbl
        nextText = view.next_text_lbl
        number = Random().nextInt(9)
        text.text = number.toString()
    }

    private var eventListener: SlotEventListener? = null


    fun init(slotOptions: SlotOptions, eventListener: SlotEventListener) {
        this.finishValue = slotOptions.finishValue
        this.previousWasFinished = slotOptions.isPreviousWasFinished
        this.eventListener = eventListener
        startSlot()
    }


    private fun startSlot() {

        if (!circleCounter.contains(number)) circleCounter.add(number)
        else circle++


        if (number >= 9) number = 0
        else number++


        val speed = if (circle > 0 && previousWasFinished) MIN_ANIM_DURATION else MAX_ANIM_DURATION


        text.animate().translationY(-height.toFloat()).setDuration(speed).start()
        setText(nextText, number)

        nextText.translationY = (nextText.height * 2).toFloat()
        nextText.animate()
            .translationY(0F)
            .setDuration(speed)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animator: Animator) {
                }

                override fun onAnimationEnd(animator: Animator) {
                    setText(text, number)
                    text.translationY = 0F
                    if (circle > MIN_CIRCLES && previousWasFinished && finishValue == number) finishSlot()
                    else startSlot()

                }

                override fun onAnimationCancel(animator: Animator) {
                }

                override fun onAnimationStart(animator: Animator) {
                }

            })
    }

    private fun finishSlot() {
        clearCircle()
        setText(text, number)
        eventListener?.end(this@SlotView, number)
    }

    private fun setText(text: AppCompatTextView, value: Int) {
        text.text = value.toString()
        text.tag = value
    }

    fun previousFinished() {
        clearCircle()
        previousWasFinished = true
    }

    private fun clearCircle() {
        circle = 0
        circleCounter.clear()
    }


}