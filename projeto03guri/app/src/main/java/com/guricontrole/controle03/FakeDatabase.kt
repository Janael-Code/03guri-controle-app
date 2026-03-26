package com.guricontrole.controle03

object FakeDatabase {

    val produtos: MutableList<Produto> = mutableListOf(
        Produto(1L, "Motor Trifásico WEG", 45, 1850.0, CategoriaProduto.MOTOR),
        Produto(2L, "Furadeira Profissional", 12, 420.0, CategoriaProduto.FERRAMENTA),
        Produto(3L, "Cabo PP 4x2,5mm", 200, 8.5, CategoriaProduto.CABO),
        Produto(4L, "Caixa de Ferramentas", 7, 156.0, CategoriaProduto.CAIXA)
    )

    fun findById(id: Long): Produto? = produtos.find { it.id == id }
}
