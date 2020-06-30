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
    fun end(result: Int)
}


class SlotView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var text: AppCompatTextView
    private var nextText: AppCompatTextView

    var oldValue = 0

    init {
        val view = View.inflate(context, R.layout.text_view_scrolling, this)
        text = view.text_lbl
        nextText = view.next_text_lbl
    }

    /*companion object {
        private const val ANIM_DURATION = 500L
    }*/

    private var eventListener: EventListener? = null

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    fun setRandomValue(number: Int, speed: Long) {
        text.animate().translationY(-height.toFloat()).setDuration(speed).start()
        nextText.translationY = nextText.height.toFloat()
        nextText.animate().translationY(0F).setDuration(speed).setListener(object :
            Animator.AnimatorListener {
            override fun onAnimationRepeat(animator: Animator) {
            }

            override fun onAnimationEnd(animator: Animator) {
                setText(text, number)
                text.translationY = 0F

                if (oldValue != number) {
                    setRandomValue(Random().nextInt(10), speed)
                } else {
                    oldValue = 0
                    text.visibility = View.GONE
                    setText(nextText, number)
                    eventListener?.end(number)
                }
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationStart(animator: Animator) {
                text.visibility = View.VISIBLE
            }

        })
    }

    private fun setText(text: AppCompatTextView, value: Int) {
        text.text = value.toString()
        text.tag = value
    }

    fun getValue() = nextText.tag.toString().toInt()

    fun setFinishValue(finishValue: Int) {
        this.oldValue = finishValue
    }


}