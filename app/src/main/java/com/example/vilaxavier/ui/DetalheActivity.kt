package com.seuapp.jogos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vilaxavier.R

class DetalheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe)

        val imgCapa = findViewById<ImageView>(R.id.imgCapaDetalhe)
        val tvNome = findViewById<TextView>(R.id.tvNomeDetalhe)
        val tvDesenvolvedora = findViewById<TextView>(R.id.tvDesenvolvedoraDetalhe)
        val tvGenero = findViewById<TextView>(R.id.tvGeneroDetalhe)
        val tvDescricao = findViewById<TextView>(R.id.tvDescricaoDetalhe)
        val btnSteam = findViewById<Button>(R.id.btnSteam)
        val btnWikipedia = findViewById<Button>(R.id.btnWikipedia)

        val imagem = intent.getIntExtra("imagem", 0)
        val nome = intent.getStringExtra("nome")
        val desenvolvedora = intent.getStringExtra("desenvolvedora")
        val genero = intent.getStringExtra("genero")
        val descricao = intent.getStringExtra("descricao")
        val steamUrl = intent.getStringExtra("steamUrl")
        val wikipediaUrl = intent.getStringExtra("wikiUrl")

        imgCapa.setImageResource(imagem)
        tvNome.text = nome
        tvDesenvolvedora.text = desenvolvedora
        tvGenero.text = genero
        tvDescricao.text = descricao

        btnSteam.setOnClickListener {
            steamUrl?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }

        btnWikipedia.setOnClickListener {
            wikipediaUrl?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }
    }
}
