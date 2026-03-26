package com.guricontrole.controle03

import java.text.NumberFormat
import java.util.Locale

object BrFormat {
    private val currency: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    fun money(value: Double): String = currency.format(value)
}
