package com.guricontrole.controle03

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guricontrole.controle03.databinding.ItemCarrinhoBinding

class CartItemAdapter(
    private val linhas: MutableList<LinhaCarrinho>,
    private val onChanged: () -> Unit
) : RecyclerView.Adapter<CartItemAdapter.VH>() {

    class VH(val binding: ItemCarrinhoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCarrinhoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val linha = linhas[position]
        val produto = FakeDatabase.findById(linha.produtoId) ?: return
        val b = holder.binding

        b.textName.text = produto.nome
        b.textPrice.text = BrFormat.money(produto.preco)
        b.textStock.text = b.root.context.getString(R.string.stock_label, produto.estoque)
        b.imageCategory.setImageResource(iconFor(produto.categoria))

        val oldWatcher = b.editQtyInline.tag as? TextWatcher
        if (oldWatcher != null) b.editQtyInline.removeTextChangedListener(oldWatcher)

        b.editQtyInline.setText(linha.quantidade.toString())
        b.editQtyAdd.setText("1")

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val pos = holder.bindingAdapterPosition
                if (pos == RecyclerView.NO_POSITION || pos >= linhas.size) return
                val current = linhas[pos]
                val q = s?.toString()?.toIntOrNull()
                if (q != null && q > 0) {
                    current.quantidade = q
                    onChanged()
                }
            }
        }
        b.editQtyInline.tag = watcher
        b.editQtyInline.addTextChangedListener(watcher)

        b.buttonRemove.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                linhas.removeAt(pos)
                notifyDataSetChanged()
                onChanged()
            }
        }

        b.buttonAddMore.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION || pos >= linhas.size) return@setOnClickListener
            val line = linhas[pos]
            val extra = b.editQtyAdd.text.toString().toIntOrNull() ?: 0
            if (extra <= 0) return@setOnClickListener
            line.quantidade += extra
            b.editQtyInline.removeTextChangedListener(watcher)
            b.editQtyInline.setText(line.quantidade.toString())
            b.editQtyInline.addTextChangedListener(watcher)
            onChanged()
        }
    }

    override fun getItemCount(): Int = linhas.size

    override fun onViewRecycled(holder: VH) {
        val w = holder.binding.editQtyInline.tag as? TextWatcher
        if (w != null) holder.binding.editQtyInline.removeTextChangedListener(w)
        holder.binding.editQtyInline.tag = null
        super.onViewRecycled(holder)
    }

    private fun iconFor(c: CategoriaProduto): Int = when (c) {
        CategoriaProduto.MOTOR -> R.drawable.ic_cat_motor
        CategoriaProduto.FERRAMENTA -> R.drawable.ic_cat_drill
        CategoriaProduto.CABO -> R.drawable.ic_cat_cable
        CategoriaProduto.CAIXA -> R.drawable.ic_cat_toolbox
    }
}
