package com.guricontrole.controle03

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.guricontrole.controle03.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonEnter.setOnClickListener {
            startActivity(Intent(this, InventoryActivity::class.java))
        }

        binding.textForgot.setOnClickListener {
            Toast.makeText(this, R.string.nav_placeholder, Toast.LENGTH_SHORT).show()
        }
    }
}
