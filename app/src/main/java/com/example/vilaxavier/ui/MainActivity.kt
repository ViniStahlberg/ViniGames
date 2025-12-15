package com.example.vilaxavier.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vilaxavier.R
import com.example.vilaxavier.adapter.JogoAdapter
import com.example.vilaxavier.data.AppDatabase
import com.example.vilaxavier.model.Jogo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: android.content.SharedPreferences
    private var isDarkTheme = false
    private var isPortuguese = true
    private lateinit var jogoAdapter: JogoAdapter
    private lateinit var database: AppDatabase
    private lateinit var etSearch: EditText

    private val jogosCompletos = mutableListOf<Jogo>()

    private val jogosIniciais = listOf(
        Jogo(
            foto = R.drawable.cs2,
            nome = "Counter-Strike 2",
            desenvolvedora = "Valve",
            genero = "Tiro Tático",
            descricao = "O sucessor do CS:GO com nova engine",
            steamUrl = "https://store.steampowered.com/app/730/",
            wikipediaUrl = "https://en.wikipedia.org/wiki/Counter-Strike_2"
        ),
        Jogo(
            foto = R.drawable.r6,
            nome = "Rainbow Six Siege",
            desenvolvedora = "Ubisoft",
            genero = "Tiro Estratégico",
            descricao = "Jogo de tiro tático em equipe",
            steamUrl = "https://store.steampowered.com/app/359550/",
            wikipediaUrl = "https://en.wikipedia.org/wiki/Tom_Clancy%27s_Rainbow_Six_Siege"
        ),
        Jogo(
            foto = R.drawable.fortnite,
            nome = "Fortnite",
            desenvolvedora = "Epic Games",
            genero = "Battle Royale",
            descricao = "Battle royale gratuito com construção",
            steamUrl = "https://www.epicgames.com/fortnite",
            wikipediaUrl = "https://en.wikipedia.org/wiki/Fortnite"
        ),
        Jogo(
            foto = R.drawable.valorant,
            nome = "Valorant",
            desenvolvedora = "Riot Games",
            genero = "Tiro Tático",
            descricao = "FPS tático com personagens com habilidades",
            steamUrl = "https://playvalorant.com/",
            wikipediaUrl = "https://en.wikipedia.org/wiki/Valorant"
        ),
        Jogo(
            foto = R.drawable.rocketleague,
            nome = "Rocket League",
            desenvolvedora = "Psyonix",
            genero = "Esporte",
            descricao = "Futebol com carros movidos a foguete",
            steamUrl = "https://store.steampowered.com/app/252950/",
            wikipediaUrl = "https://en.wikipedia.org/wiki/Rocket_League"
        ),
        Jogo(
            foto = R.drawable.minecraft,
            nome = "Minecraft",
            desenvolvedora = "Mojang Studios",
            genero = "Sandbox",
            descricao = "Jogo de construção e sobrevivência",
            steamUrl = "https://www.minecraft.net/",
            wikipediaUrl = "https://en.wikipedia.org/wiki/Minecraft"
        )
    ).sortedBy { it.nome }


    private val adicionarJogoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val novoJogo = data?.getSerializableExtra("novo_jogo") as? Jogo
            val imagemCaminho = data?.getStringExtra("imagem_caminho")

            novoJogo?.let { jogo ->
                imagemCaminho?.let { caminho ->
                    jogo.imagemCaminho = caminho
                }

                lifecycleScope.launch(Dispatchers.IO) {
                    database.jogoDao().inserir(jogo)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.game_added_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        carregarJogos()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        isDarkTheme = prefs.getBoolean("is_dark_theme", false)
        isPortuguese = prefs.getBoolean("is_portuguese", true)

        applySavedTheme()
        applySavedLanguage()

        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        database = AppDatabase.getDatabase(this)

        setupViews()
        setupButtons()
        setupSearch()
        updateButtonTexts()

        lifecycleScope.launch {
            carregarJogosIniciais()
            carregarJogos()
        }
    }

    private fun setupSearch() {
        etSearch = findViewById(R.id.etSearch)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                filtrarJogos(s.toString())
            }
        })
    }

    private fun filtrarJogos(query: String) {
        val queryLower = query.toLowerCase(Locale.getDefault()).trim()

        if (queryLower.isEmpty()) {
            jogoAdapter.atualizarLista(jogosCompletos)
        } else {
            val jogosFiltrados = jogosCompletos.filter { jogo ->
                jogo.nome.toLowerCase(Locale.getDefault()).contains(queryLower) ||
                        jogo.desenvolvedora.toLowerCase(Locale.getDefault()).contains(queryLower) ||
                        jogo.genero.toLowerCase(Locale.getDefault()).contains(queryLower) ||
                        jogo.descricao.toLowerCase(Locale.getDefault()).contains(queryLower)
            }
            jogoAdapter.atualizarLista(jogosFiltrados)
        }
    }

    private fun carregarJogosIniciais() {
        lifecycleScope.launch(Dispatchers.IO) {
            val jogosExistentes = database.jogoDao().obterTodos()

            jogosExistentes.value?.takeIf { it.isEmpty() }?.let {
                jogosIniciais.forEach { jogo ->
                    database.jogoDao().inserir(jogo)
                }
            }
        }
    }

    private fun carregarJogos() {
        lifecycleScope.launch {
            database.jogoDao().obterTodos().observe(this@MainActivity) { listaJogos ->
                // Esta lambda é automaticamente chamada na Main Thread quando os dados mudam
                jogosCompletos.clear()
                jogosCompletos.addAll(listaJogos)

                val query = etSearch.text.toString()
                if (query.isNotEmpty()) {
                    filtrarJogos(query)
                } else {
                    jogoAdapter.atualizarLista(listaJogos)
                }
            }
        }
    }

    private fun setupViews() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewJogos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        jogoAdapter = JogoAdapter(emptyList()) { jogo ->
            // Navegar para detalhes
            val intent = Intent(this, DetalheActivity::class.java).apply {
                putExtra("imagem", jogo.foto)
                putExtra("nome", jogo.nome)
                putExtra("desenvolvedora", jogo.desenvolvedora)
                putExtra("genero", jogo.genero)
                putExtra("descricao", jogo.descricao)
                putExtra("steamUrl", jogo.steamUrl)
                putExtra("wikiUrl", jogo.wikipediaUrl)
            }
            startActivity(intent)
        }

        recyclerView.adapter = jogoAdapter
    }

    private fun setupButtons() {
        val btnTheme = findViewById<Button>(R.id.btnTheme)
        val btnLanguage = findViewById<Button>(R.id.btnLanguage)
        val btnAdicionar = findViewById<Button>(R.id.btnAdicionar)

        btnTheme.setOnClickListener {
            toggleTheme()
        }

        btnLanguage.setOnClickListener {
            toggleLanguage()
        }

        btnAdicionar.setOnClickListener {
            val intent = Intent(this, AdicionarJogoActivity::class.java)
            adicionarJogoLauncher.launch(intent)
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
        val btnAdicionar = findViewById<Button>(R.id.btnAdicionar)

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

        btnAdicionar.text = getString(R.string.add_new)
    }

    override fun onResume() {
        super.onResume()
        updateButtonTexts()
    }

}