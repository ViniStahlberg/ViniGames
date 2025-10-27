package com.example.vilaxavier.model

data class Jogo(
    val foto: Int,
    val desenvolvedora: String,
    val nome: String,
    val genero: String,
    val descricao: String,
    val steamUrl: String = "",
    val wikipediaUrl: String = ""
)