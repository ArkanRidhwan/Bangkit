package com.arkan.ridwan.storyapps.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.arkan.ridwan.storyapps.R

class CustomEditText : AppCompatEditText {

    private var errorBackground: Drawable? = null
    private var defaultBackground: Drawable? = null
    private var errorCheck: Boolean = false
    private var tipeInput = this.inputType


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        defaultBackground = ContextCompat.getDrawable(context, R.drawable.bg_edittext)
        errorBackground = ContextCompat.getDrawable(context, R.drawable.bg_edittext_error)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(string: Editable?) {
                val input = string.toString()
                when (tipeInput) {
                    email -> {
                        if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                            error = "Format Email Salah"
                            errorCheck = true
                        } else {
                            errorCheck = false
                        }
                    }
                    password -> {
                        if (input.length < 6) {
                            error = "Password Kurang Dari 6"
                            errorCheck = true
                        } else {
                            errorCheck = false
                        }
                    }
                }
            }

        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = when {
            errorCheck -> errorBackground
            else -> defaultBackground
        }
    }

    companion object {
        const val email = 0x00000021
        const val password = 0x00000081
    }

}