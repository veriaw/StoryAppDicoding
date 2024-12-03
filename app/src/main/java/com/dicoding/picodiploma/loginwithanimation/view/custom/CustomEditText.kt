package com.dicoding.picodiploma.loginwithanimation.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length < 8) {
                    showError("Password harus memiliki minimal 8 karakter")
                } else {
                    hideError()
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun showError(message: String) {
        val parent = parent
        if (parent is TextInputLayout) {
            parent.error = message
        }
    }

    private fun hideError() {
        val parent = parent
        if (parent is TextInputLayout) {
            parent.error = null
        }
    }
}