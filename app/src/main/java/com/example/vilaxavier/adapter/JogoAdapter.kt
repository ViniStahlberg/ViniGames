package com.example.vilaxavier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vilaxavier.R
import com.example.vilaxavier.model.Jogo

class JogoAdapter(
    private var jogos: List<Jogo> = emptyList(),
    private val onItemClick: (Jogo) -> Unit
) : RecyclerView.Adapter<JogoAdapter.JogoViewHolder>() {

    inner class JogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCapa: ImageView = itemView.findViewById(R.id.imgCapa)
        val tvNome: TextView = itemView.findViewById(R.id.tvNome)
        val tvDesenvolvedora: TextView = itemView.findViewById(R.id.tvDesenvolvedora)
        val tvGenero: TextView = itemView.findViewById(R.id.tvGenero)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JogoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jogo, parent, false)
        return JogoViewHolder(view)
    }

    override fun onBindViewHolder(holder: JogoViewHolder, position: Int) {
        val jogo = jogos[position]

        holder.imgCapa.setImageResource(jogo.foto)
        holder.tvNome.text = jogo.nome
        holder.tvDesenvolvedora.text = jogo.desenvolvedora
        holder.tvGenero.text = jogo.genero

        holder.itemView.setOnClickListener {
            onItemClick(jogo)
        }
    }

    override fun getItemCount(): Int = jogos.size

    fun atualizarLista(novaLista: List<Jogo>) {
        jogos = novaLista
        notifyDataSetChanged()
    }
}