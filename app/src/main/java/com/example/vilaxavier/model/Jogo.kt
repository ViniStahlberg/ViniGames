package com.example.vilaxavier.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore
import java.io.Serializable

@Entity(tableName = "jogos")
data class Jogo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foto: Int,
    val nome: String,
    val desenvolvedora: String,
    val genero: String,
    val descricao: String,
    val steamUrl: String = "",
    val wikipediaUrl: String = ""
) : Serializable {

    @Ignore
    var imagemCaminho: String = ""
}