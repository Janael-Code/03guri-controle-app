package com.guricontrole.controle03

import java.util.UUID

data class LinhaCarrinho(
    val id: String = UUID.randomUUID().toString(),
    val produtoId: Long,
    var quantidade: Int
)
