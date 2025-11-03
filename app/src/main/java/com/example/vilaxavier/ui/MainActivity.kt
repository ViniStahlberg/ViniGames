package com.example.vilaxavier.ui

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.vilaxavier.R
import com.example.vilaxavier.adapter.JogoAdapter
import com.example.vilaxavier.model.Jogo
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: android.content.SharedPreferences
    private var isDarkTheme = false
    private var isPortuguese = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        isDarkTheme = prefs.getBoolean("is_dark_theme", false)
        isPortuguese = prefs.getBoolean("is_portuguese", true)

        applySavedTheme()
        applySavedLanguage()

        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        setupApp()
        setupButtons()
        updateButtonTexts()
    }

    private fun setupButtons() {
        val btnTheme = findViewById<Button>(R.id.btnTheme)
        val btnLanguage = findViewById<Button>(R.id.btnLanguage)

        btnTheme.setOnClickListener {
            toggleTheme()
        }

        btnLanguage.setOnClickListener {
            toggleLanguage()
        }
    }

    private fun toggleTheme() {
        isDarkTheme = !isDarkTheme
        prefs.edit().putBoolean("is_dark_theme", isDarkTheme).apply()

        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        updateButtonTexts()
    }

    private fun toggleLanguage() {
        isPortuguese = !isPortuguese
        prefs.edit().putBoolean("is_portuguese", isPortuguese).apply()

        setAppLocale(if (isPortuguese) "pt" else "en")
        recreate()
    }

    private fun setAppLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources: Resources = resources
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        val newContext = createConfigurationContext(configuration)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun applySavedTheme() {
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun applySavedLanguage() {
        val languageCode = if (isPortuguese) "pt" else "en"
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources: Resources = resources
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun updateButtonTexts() {
        val btnTheme = findViewById<Button>(R.id.btnTheme)
        val btnLanguage = findViewById<Button>(R.id.btnLanguage)

        btnTheme.text = if (isDarkTheme) {
            getString(R.string.theme_light)
        } else {
            getString(R.string.theme_dark)
        }

        btnLanguage.text = if (isPortuguese) {
            getString(R.string.language_english)
        } else {
            getString(R.string.language_portuguese)
        }
    }

    private fun setupApp() {
        val jogos = listOf(
            Jogo(
                R.drawable.cs2,
                "Valve",
                "Counter-Strike 2",
                if (isPortuguese) "Tiro Tático" else "Tactical Shooter",
                getString(R.string.cs2_description),
                "https://store.steampowered.com/app/730/",
                "https://en.wikipedia.org/wiki/Counter-Strike_2"
            ),
            Jogo(
                R.drawable.r6,
                "Ubisoft",
                "Rainbow Six Siege",
                if (isPortuguese) "Tiro Estratégico" else "Strategic Shooter",
                getString(R.string.r6_description),
                "https://store.steampowered.com/app/359550/",
                "https://en.wikipedia.org/wiki/Tom_Clancy%27s_Rainbow_Six_Siege"
            ),
            Jogo(
                R.drawable.fortnite,
                "Epic Games",
                "Fortnite",
                "Battle Royale",
                getString(R.string.fortnite_description),
                "https://www.epicgames.com/fortnite",
                "https://en.wikipedia.org/wiki/Fortnite"
            ),
            Jogo(
                R.drawable.valorant,
                "Riot Games",
                "Valorant",
                if (isPortuguese) "Tiro Tático" else "Tactical Shooter",
                getString(R.string.valorant_description),
                "https://playvalorant.com/",
                "https://en.wikipedia.org/wiki/Valorant"
            ),
            Jogo(
                R.drawable.rocketleague,
                "Psyonix",
                "Rocket League",
                if (isPortuguese) "Esporte" else "Sports",
                getString(R.string.rocket_league_description),
                "https://store.steampowered.com/app/252950/",
                "https://en.wikipedia.org/wiki/Rocket_League"
            ),
            Jogo(
                R.drawable.minecraft,
                "Mojang Studios",
                "Minecraft",
                "Sandbox",
                getString(R.string.minecraft_description),
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

    override fun onResume() {
        super.onResume()
        updateButtonTexts()
    }
}