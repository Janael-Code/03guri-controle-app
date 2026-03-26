package com.guricontrole.controle03

data class Produto(
    val id: Long,
    val nome: String,
    var estoque: Int,
    val preco: Double,
    val categoria: CategoriaProduto
)

enum class CategoriaProduto {
    MOTOR,
    FERRAMENTA,
    CABO,
    CAIXA
}
