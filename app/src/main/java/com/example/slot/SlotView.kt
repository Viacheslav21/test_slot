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
    fun end(result: SlotView)
}


class SlotView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var text: AppCompatTextView
    private var nextText: AppCompatTextView

    var finishValue: Int = 0
    var previousWasFinished = false
    private var circle = 1

    private var number: Int

    init {
        val view = View.inflate(context, R.layout.text_view_scrolling, this)
        text = view.text_lbl
        nextText = view.next_text_lbl
        number = Random().nextInt(9)
        text.text = number.toString()
    }

    companion object {
        private const val ANIM_DURATION = 500L
        private const val MIN_CIRCLES = 1
    }


    private var eventListener: EventListener? = null

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    fun startLot() {
        if (number >= 9) {
            number = 0
            circle ++
        } else number++


        text.animate().translationY(-height.toFloat()).setDuration(ANIM_DURATION).start()

        nextText.translationY = nextText.height.toFloat()

        nextText.animate().translationY(0F).setDuration(ANIM_DURATION).setListener(object :
            Animator.AnimatorListener {
            override fun onAnimationRepeat(animator: Animator) {
            }

            override fun onAnimationEnd(animator: Animator) {
                setText(text, number)
                text.translationY = 0F
                if (circle > MIN_CIRCLES && previousWasFinished && finishValue == number) finishLot()
                else startLot()

            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationStart(animator: Animator) {
            }

        })
    }

    /*  Log.d("TAG_MY", "previousWasFinished = $previousWasFinished circle = $circle")
                Log.d("TAG_MY", "finish value = $finishValue number = $number")*/


    private fun finishLot() {
        circle = 0
        setText(text, number)
        eventListener?.end(this@SlotView)
    }

    private fun setText(text: AppCompatTextView, value: Int) {
        text.text = value.toString()
        text.tag = value
    }


}