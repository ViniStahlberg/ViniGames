package com.example.vilaxavier.ui

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.vilaxavier.R
import com.example.vilaxavier.adapter.JogoAdapter
import com.example.vilaxavier.model.Jogo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        setupApp()
    }

    private fun setupApp() {
        val jogos = listOf(
            Jogo(
                R.drawable.cs2,
                "Valve",
                "Counter-Strike 2",
                "Tiro Tático", // Este é o gênero que será mostrado
                "O clássico jogo de tiro em equipe da Valve, com gráficos renovados e mecânicas aprimoradas.",
                "https://store.steampowered.com/app/730/",
                "https://en.wikipedia.org/wiki/Counter-Strike_2"
            ),
            Jogo(
                R.drawable.r6,
                "Ubisoft",
                "Rainbow Six Siege",
                "Tiro Estratégico",
                "FPS tático focado em trabalho em equipe e destruição de ambientes em combates intensos.",
                "https://store.steampowered.com/app/359550/",
                "https://en.wikipedia.org/wiki/Tom_Clancy%27s_Rainbow_Six_Siege"
            ),
            Jogo(
                R.drawable.fortnite,
                "Epic Games",
                "Fortnite",
                "Battle Royale",
                "Jogo online de sobrevivência com construção e estilo cartunesco, sucesso global entre todas as idades.",
                "https://www.epicgames.com/fortnite",
                "https://en.wikipedia.org/wiki/Fortnite"
            ),
            Jogo(
                R.drawable.valorant,
                "Riot Games",
                "Valorant",
                "Tiro Tático",
                "FPS competitivo que mistura estratégia, habilidades únicas e jogabilidade precisa.",
                "https://playvalorant.com/",
                "https://en.wikipedia.org/wiki/Valorant"
            ),
            Jogo(
                R.drawable.rocketleague,
                "Psyonix",
                "Rocket League",
                "Esporte",
                "Jogo dinâmico que combina futebol com carros movidos a foguete, perfeito para partidas rápidas.",
                "https://store.steampowered.com/app/252950/",
                "https://en.wikipedia.org/wiki/Rocket_League"
            ),
            Jogo(
                R.drawable.minecraft,
                "Mojang Studios",
                "Minecraft",
                "Sandbox",
                "Um universo de blocos onde criatividade e exploração são infinitas possibilidades para o jogador.",
                "https://www.minecraft.net/",
                "https://en.wikipedia.org/wiki/Minecraft"
            )
        ).sortedBy { it.nome }

        val listView = findViewById<ListView>(R.id.listViewJogos)
        listView.adapter = JogoAdapter(this, jogos)

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val jogo = jogos[position]
            Intent(this, DetalheActivity::class.java).apply {
                putExtra("imagem", jogo.foto)
                putExtra("nome", jogo.nome)
                putExtra("desenvolvedora", jogo.desenvolvedora)
                putExtra("genero", jogo.genero)
                putExtra("descricao", jogo.descricao)
                putExtra("steamUrl", jogo.steamUrl)
                putExtra("wikiUrl", jogo.wikipediaUrl)
                startActivity(this)
            }
        }
    }
}