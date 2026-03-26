package com.guricontrole.controle03

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.guricontrole.controle03.databinding.ActivityInventoryBinding

class InventoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInventoryBinding
    private lateinit var adapter: ProdutoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.selectedItemId = R.id.nav_estoque

        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)
        adapter = ProdutoAdapter(
            onItemClick = { p ->
                Toast.makeText(this, p.nome, Toast.LENGTH_SHORT).show()
            },
            onEdit = {
                Toast.makeText(this, R.string.nav_placeholder, Toast.LENGTH_SHORT).show()
            },
            onDelete = {
                Toast.makeText(this, R.string.nav_placeholder, Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerProducts.adapter = adapter

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, SalesActivity::class.java))
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_estoque -> true
                else -> {
                    Toast.makeText(this, R.string.nav_placeholder, Toast.LENGTH_SHORT).show()
                    binding.bottomNav.post {
                        binding.bottomNav.selectedItemId = R.id.nav_estoque
                    }
                    false
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            Toast.makeText(this, R.string.nav_placeholder, Toast.LENGTH_SHORT).show()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search, R.id.action_profile -> {
                    Toast.makeText(this, R.string.nav_placeholder, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.setProdutos(FakeDatabase.produtos.toList())
    }
}
