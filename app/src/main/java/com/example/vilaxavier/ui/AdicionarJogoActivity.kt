package com.example.vilaxavier.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.vilaxavier.R
import com.example.vilaxavier.model.Jogo
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AdicionarJogoActivity : AppCompatActivity() {

    private var imagemUri: Uri? = null
    private var imagemCaminho: String = ""

    private val IMAGEM_PADRAO = R.drawable.cs2

    private val galeriaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                imagemUri = uri
                imagemCaminho = salvarImagemTemporaria(uri)

                val imageView = findViewById<ImageView>(R.id.ivImagemSelecionada)
                imageView.setImageURI(uri)

                Toast.makeText(
                    this,
                    getString(R.string.image_selected_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val permissaoLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            abrirGaleria()
        } else {
            Toast.makeText(
                this,
                "Permission required to access gallery",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_jogo)

        setupViews()
    }

    private fun setupViews() {
        val etNome = findViewById<EditText>(R.id.etNome)
        val etDesenvolvedora = findViewById<EditText>(R.id.etDesenvolvedora)
        val etGenero = findViewById<EditText>(R.id.etGenero)
        val etDescricao = findViewById<EditText>(R.id.etDescricao)
        val etSteamUrl = findViewById<EditText>(R.id.etSteamUrl)
        val etWikipediaUrl = findViewById<EditText>(R.id.etWikipediaUrl)
        val btnSelecionarImagem = findViewById<Button>(R.id.btnSelecionarImagem)
        val btnRemoverImagem = findViewById<Button>(R.id.btnRemoverImagem)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        val imageView = findViewById<ImageView>(R.id.ivImagemSelecionada)

        btnSelecionarImagem.setOnClickListener {
            verificarPermissaoGaleria()
        }

        btnRemoverImagem.setOnClickListener {
            imagemUri = null
            imagemCaminho = ""
            imageView.setImageResource(R.drawable.image_placeholder)
            Toast.makeText(
                this,
                getString(R.string.image_removed),
                Toast.LENGTH_SHORT
            ).show()
        }

        btnSalvar.setOnClickListener {
            if (validarCampos()) {
                val nome = etNome.text.toString().trim()
                val desenvolvedora = etDesenvolvedora.text.toString().trim()
                val genero = etGenero.text.toString().trim()
                val descricao = etDescricao.text.toString().trim()
                val steamUrl = etSteamUrl.text.toString().trim()
                val wikipediaUrl = etWikipediaUrl.text.toString().trim()

                // Sempre usar o drawable padrão para o campo 'foto'
                val jogo = Jogo(
                    foto = IMAGEM_PADRAO,
                    nome = nome,
                    desenvolvedora = desenvolvedora,
                    genero = genero,
                    descricao = descricao,
                    steamUrl = steamUrl.ifEmpty { "" },
                    wikipediaUrl = wikipediaUrl.ifEmpty { "" }
                )

                // Se houver imagem da galeria, passar o caminho como extra
                val resultIntent = Intent().apply {
                    putExtra("novo_jogo", jogo)
                    if (imagemCaminho.isNotEmpty()) {
                        putExtra("imagem_caminho", imagemCaminho)
                    }
                }

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        btnCancelar.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun verificarPermissaoGaleria() {
        val permissao = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(
                this,
                permissao
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            abrirGaleria()
        } else {
            permissaoLauncher.launch(permissao)
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        galeriaLauncher.launch(intent)
    }

    private fun salvarImagemTemporaria(uri: Uri): String {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("jogo_imagem_", ".jpg", cacheDir)

            inputStream?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            tempFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun validarCampos(): Boolean {
        val etNome = findViewById<EditText>(R.id.etNome)
        val etDesenvolvedora = findViewById<EditText>(R.id.etDesenvolvedora)
        val etGenero = findViewById<EditText>(R.id.etGenero)
        val etDescricao = findViewById<EditText>(R.id.etDescricao)

        if (etNome.text.toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.error_name_required), Toast.LENGTH_SHORT).show()
            return false
        }

        if (etDesenvolvedora.text.toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.error_developer_required), Toast.LENGTH_SHORT).show()
            return false
        }

        if (etGenero.text.toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.error_genre_required), Toast.LENGTH_SHORT).show()
            return false
        }

        if (etDescricao.text.toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.error_description_required), Toast.LENGTH_SHORT).show()
            return false
        }

        if (imagemUri == null) {
            Toast.makeText(this, getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}