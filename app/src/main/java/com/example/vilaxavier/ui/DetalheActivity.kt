package com.example.vilaxavier.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vilaxavier.R

class DetalheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe)

        supportActionBar?.hide()

        val imgCapa = findViewById<ImageView>(R.id.imgCapaDetalhe)
        val tvNome = findViewById<TextView>(R.id.tvNomeDetalhe)
        val tvDesenvolvedora = findViewById<TextView>(R.id.tvDesenvolvedoraDetalhe)
        val tvGenero = findViewById<TextView>(R.id.tvGeneroDetalhe)
        val tvDescricao = findViewById<TextView>(R.id.tvDescricaoDetalhe)
        val btnSteam = findViewById<Button>(R.id.btnSteam)
        val btnWikipedia = findViewById<Button>(R.id.btnWikipedia)

        val imagem = intent.getIntExtra("imagem", 0)
        val nome = intent.getStringExtra("nome") ?: getString(R.string.name_not_available)
        val desenvolvedora = intent.getStringExtra("desenvolvedora") ?: getString(R.string.developer_not_available)
        val genero = intent.getStringExtra("genero") ?: getString(R.string.genre_not_available)
        val descricao = intent.getStringExtra("descricao") ?: getString(R.string.description_not_available)
        val steamUrl = intent.getStringExtra("steamUrl")
        val wikipediaUrl = intent.getStringExtra("wikiUrl")

        if (imagem != 0) {
            imgCapa.setImageResource(imagem)
        }

        tvNome.text = nome
        tvDesenvolvedora.text = desenvolvedora
        tvGenero.text = genero
        tvDescricao.text = descricao

        btnSteam.setOnClickListener {
            steamUrl?.let { url ->
                if (url.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, getString(R.string.url_not_available), Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, getString(R.string.steam_link_not_available), Toast.LENGTH_SHORT).show()
            }
        }

        btnWikipedia.setOnClickListener {
            wikipediaUrl?.let { url ->
                if (url.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, getString(R.string.url_not_available), Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, getString(R.string.wikipedia_link_not_available), Toast.LENGTH_SHORT).show()
            }
        }
    }
}