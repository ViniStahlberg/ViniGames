package com.example.vilaxavier.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vilaxavier.model.Jogo

@Dao
interface JogoDao {

    @Insert
    suspend fun inserir(jogo: Jogo): Long

    @Update
    suspend fun atualizar(jogo: Jogo)

    @Delete
    suspend fun deletar(jogo: Jogo)

    @Query("SELECT * FROM jogos ORDER BY nome ASC")
    fun obterTodos(): LiveData<List<Jogo>>

    @Query("SELECT * FROM jogos WHERE id = :id")
    suspend fun obterPorId(id: Long): Jogo?
}