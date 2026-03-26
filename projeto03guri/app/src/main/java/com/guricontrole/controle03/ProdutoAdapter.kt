package com.guricontrole.controle03

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guricontrole.controle03.databinding.ItemProdutoBinding

class ProdutoAdapter(
    private val items: MutableList<Produto> = mutableListOf(),
    private val onItemClick: (Produto) -> Unit,
    private val onEdit: (Produto) -> Unit,
    private val onDelete: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.VH>() {

    class VH(val binding: ItemProdutoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemProdutoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = items[position]
        val b = holder.binding
        b.textName.text = p.nome
        b.textStock.text = b.root.context.getString(R.string.stock_label, p.estoque)
        b.textPrice.text = BrFormat.money(p.preco)
        b.imageCategory.setImageResource(iconFor(p.categoria))
        b.root.setOnClickListener { onItemClick(p) }
        b.buttonEdit.setOnClickListener { onEdit(p) }
        b.buttonDelete.setOnClickListener { onDelete(p) }
    }

    override fun getItemCount(): Int = items.size

    fun setProdutos(list: List<Produto>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    private fun iconFor(c: CategoriaProduto): Int = when (c) {
        CategoriaProduto.MOTOR -> R.drawable.ic_cat_motor
        CategoriaProduto.FERRAMENTA -> R.drawable.ic_cat_drill
        CategoriaProduto.CABO -> R.drawable.ic_cat_cable
        CategoriaProduto.CAIXA -> R.drawable.ic_cat_toolbox
    }
}
