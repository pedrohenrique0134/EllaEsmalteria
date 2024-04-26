package com.app.ellaeamalteriasistemadelancamentos.utils

import android.text.Editable
import android.text.TextWatcher

class TelefoneTextWatcher : TextWatcher {
    private var isFormatting: Boolean = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Não é necessário implementar neste caso
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Não é necessário implementar neste caso
    }

    override fun afterTextChanged(s: Editable?) {
        if (!isFormatting) {
            isFormatting = true

            val phoneBuilder = StringBuilder()
            for (c in s ?: return) {
                if (c.isDigit()) {
                    phoneBuilder.append(c)
                }
            }

            val formattedPhone = formatarTelefone(phoneBuilder.toString())
            s?.replace(0, s.length, formattedPhone)

            isFormatting = false
        }
    }
}

fun formatarTelefone(telefone: String): String {
    // Sua lógica de formatação de telefone aqui
    // Exemplo simples para (62)99267-3011
    return when {
        telefone.length >= 10 -> {
            val ddd = telefone.substring(0, 2)
            val prefixo = telefone.substring(2, 7)
            val sufixo = telefone.substring(7)
            "($ddd)$prefixo-$sufixo"
        }
        else -> telefone // Retorna o número sem formatação se não atender aos requisitos
    }
}
