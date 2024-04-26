package com.app.ellaeamalteriasistemadelancamentos.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DateMaskTextWatcher(private val editText: EditText) : TextWatcher {

    private var isDeleting = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isDeleting) return

        val input = s.toString()

        if (input.length == 2 || input.length == 5) {
            editText.append("/")
        }

        if (input.length == 11) {
            val day = input.substring(0, 2)
            val month = input.substring(3, 5)
            val year = input.substring(6)

            val formattedDate = "$day/$month/$year"
            editText.setText(formattedDate)
            editText.setSelection(formattedDate.length)
        }
    }
}