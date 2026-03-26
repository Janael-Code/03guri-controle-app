package com.guricontrole.controle03

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.guricontrole.controle03.databinding.ActivitySalesBinding

class SalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesBinding
    private val cartLines = mutableListOf<LinhaCarrinho>()
    private lateinit var cartAdapter: CartItemAdapter
    private var filteredProducts: List<Produto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.recyclerCart.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartItemAdapter(cartLines) { updateTotalAndEmpty() }
        binding.recyclerCart.adapter = cartAdapter

        binding.editSearchProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                refreshSpinner(s?.toString().orEmpty())
            }
        })

        binding.spinnerProduct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSelectedStock()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.textSelectedStock.text = ""
            }
        }

        binding.buttonAddToCart.setOnClickListener { addFromSelection() }
        binding.buttonRegisterSale.setOnClickListener { registerSale() }

        refreshSpinner("")
    }

    private fun refreshSpinner(filter: String) {
        filteredProducts = if (filter.isBlank()) {
            FakeDatabase.produtos.toList()
        } else {
            FakeDatabase.produtos.filter { it.nome.contains(filter, ignoreCase = true) }
        }

        if (filteredProducts.isEmpty()) {
            val placeholder = ArrayAdapter(this, R.layout.spinner_item, listOf(getString(R.string.none_found)))
            placeholder.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.spinnerProduct.adapter = placeholder
            binding.textSelectedStock.text = ""
            return
        }

        val names = filteredProducts.map { it.nome }
        val adapter = ArrayAdapter(this, R.layout.spinner_item, names)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerProduct.adapter = adapter
        binding.spinnerProduct.setSelection(0)
        updateSelectedStock()
    }

    private fun updateSelectedStock() {
        val pos = binding.spinnerProduct.selectedItemPosition
        if (pos < 0 || pos >= filteredProducts.size) {
            binding.textSelectedStock.text = ""
            return
        }
        val p = filteredProducts[pos]
        binding.textSelectedStock.text = getString(R.string.stock_current, p.estoque)
    }

    private fun addFromSelection() {
        if (filteredProducts.isEmpty()) {
            Toast.makeText(this, R.string.none_found, Toast.LENGTH_SHORT).show()
            return
        }
        val pos = binding.spinnerProduct.selectedItemPosition
        if (pos < 0 || pos >= filteredProducts.size) return

        val produto = filteredProducts[pos]
        val qty = binding.editQtyNew.text.toString().toIntOrNull() ?: 0
        if (qty <= 0) {
            Toast.makeText(this, R.string.toast_invalid_qty, Toast.LENGTH_SHORT).show()
            return
        }

        val existente = cartLines.find { it.produtoId == produto.id }
        if (existente != null) {
            existente.quantidade += qty
        } else {
            cartLines.add(LinhaCarrinho(produtoId = produto.id, quantidade = qty))
        }
        cartAdapter.notifyDataSetChanged()
        updateTotalAndEmpty()
    }

    private fun registerSale() {
        if (cartLines.isEmpty()) {
            Toast.makeText(this, R.string.toast_empty_cart, Toast.LENGTH_SHORT).show()
            return
        }
        for (line in cartLines) {
            val p = FakeDatabase.findById(line.produtoId) ?: continue
            if (line.quantidade > p.estoque) {
                Toast.makeText(this, R.string.toast_insufficient, Toast.LENGTH_SHORT).show()
                return
            }
        }
        for (line in cartLines) {
            val p = FakeDatabase.findById(line.produtoId) ?: continue
            p.estoque -= line.quantidade
        }
        cartLines.clear()
        cartAdapter.notifyDataSetChanged()
        updateTotalAndEmpty()
        refreshSpinner(binding.editSearchProduct.text.toString())
        Toast.makeText(this, R.string.toast_sale_ok, Toast.LENGTH_SHORT).show()
    }

    private fun updateTotalAndEmpty() {
        val empty = cartLines.isEmpty()
        binding.textCartEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        binding.recyclerCart.visibility = if (empty) View.GONE else View.VISIBLE

        val total = cartLines.sumOf { line ->
            val p = FakeDatabase.findById(line.produtoId)
            (p?.preco ?: 0.0) * line.quantidade
        }

        val label = "Valor Total: "
        val valueStr = BrFormat.money(total)
        val full = label + valueStr
        val ss = SpannableString(full)
        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_primary)),
            0,
            label.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary_blue)),
            label.length,
            full.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            StyleSpan(Typeface.BOLD),
            label.length,
            full.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textTotal.text = ss
    }
}
