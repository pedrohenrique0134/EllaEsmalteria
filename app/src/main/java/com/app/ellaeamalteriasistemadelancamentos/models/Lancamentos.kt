package com.app.ellaeamalteriasistemadelancamentos.models

data class Lancamentos(
    var client: String? = null,
    var data: String? = null,
    var valor: String? = null,
    var tipo: Any? = null,
    var id: String? = null,
    var entryOrExit: String? = null,
    var time: String? = null,
)
