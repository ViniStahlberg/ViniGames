package com.example.vilaxavier.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vilaxavier.R
import com.example.vilaxavier.databinding.ActivityMainBinding
import com.example.vilaxavier.model.Jogo
import com.example.vilaxavier.adapter.JogoAdapter
import android.content.Intent
import com.seuapp.jogos.DetalheActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var jogos: List<Jogo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        setupViews()
        setupListeners()
    }

    private fun loadData() {
        jogos = listOf(
            Jogo(R.drawable.cs2, "Valve", "Counter-Strike 2", "Tiro Tático", "O clássico jogo de tiro em equipe da Valve, com gráficos renovados e mecânicas aprimoradas."),
            Jogo(R.drawable.r6, "Ubisoft", "Rainbow Six Siege", "Tiro Estratégico", "FPS tático focado em trabalho em equipe e destruição de ambientes em combates intensos."),
            Jogo(R.drawable.fortnite, "Epic Games", "Fortnite", "Battle Royale", "Jogo online de sobrevivência com construção e estilo cartunesco, sucesso global entre todas as idades."),
            Jogo(R.drawable.valorant, "Riot Games", "Valorant", "Tiro Tático", "FPS competitivo que mistura estratégia, habilidades únicas e jogabilidade precisa."),
            Jogo(R.drawable.rocketleague, "Psyonix", "Rocket League", "Esporte/Futebol com Carros", "Jogo dinâmico que combina futebol com carros movidos a foguete, perfeito para partidas rápidas."),
            Jogo(R.drawable.minecraft, "Mojang Studios", "Minecraft", "Sandbox/Aventura", "Um universo de blocos onde criatividade e exploração são infinitas possibilidades para o jogador.")
        ).sortedBy { it.nome }
    }

    private fun setupViews() {
        val adapter = JogoAdapter(this, jogos)
        binding.listViewMusicas.adapter = adapter
    }

    private fun setupListeners() {
        binding.listViewMusicas.setOnItemClickListener { _, _, position, _ ->
            val jogo = jogos[position]
            val intent = Intent(this, DetalheActivity::class.java).apply {
                putExtra("imagem", jogo.foto)
                putExtra("nome", jogo.nome)
                putExtra("desenvolvedora", jogo.desenvolvedora)
                putExtra("genero", jogo.genero)
                putExtra("descricao", jogo.descricao)
                putExtra("steamUrl", "https://store.steampowered.com/app/730") // Exemplo CS2
                putExtra("wikiUrl", "https://pt.wikipedia.org/wiki/Counter-Strike_2")
            }
            startActivity(intent)
        }
    }
}