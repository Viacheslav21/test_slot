package com.example.slot

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.text_view_scrolling.view.*
import java.util.*

interface EventListener {
    fun end(slot: SlotView)
}


class SlotView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var text: AppCompatTextView
    private var nextText: AppCompatTextView

    var finishValue: Int = 0
    var previousWasFinished = false
    private var circle = 0

    private var number: Int

    private var circleCounter = mutableSetOf<Int>()

    init {
        val view = View.inflate(context, R.layout.text_view_scrolling, this)
        text = view.text_lbl
        nextText = view.next_text_lbl
        number = Random().nextInt(9)
        text.text = number.toString()
    }

    companion object {
              private const val MAX_ANIM_DURATION = 400L
              private const val MIN_ANIM_DURATION = 550L

        private const val MIN_CIRCLES = 1
    }


    private var eventListener: EventListener? = null

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    fun startSlot() {

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

    /*  Log.d("TAG_MY", "previousWasFinished = $previousWasFinished circle = $circle")
                Log.d("TAG_MY", "finish value = $finishValue number = $number")*/


    private fun finishSlot() {
        clearCircle()
        setText(text, number)
        eventListener?.end(this@SlotView)
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