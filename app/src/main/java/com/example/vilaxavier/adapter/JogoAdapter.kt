package com.example.vilaxavier.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.vilaxavier.R
import com.example.vilaxavier.model.Jogo

class JogoAdapter(
    context: Context,
    private val jogos: List<Jogo>
) : ArrayAdapter<Jogo>(context, 0, jogos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_jogo, parent, false)

        val jogo = jogos[position]

        val imgCapa = view.findViewById<ImageView>(R.id.imgCapa)
        val tvNome = view.findViewById<TextView>(R.id.tvNome)
        val tvDesenvolvedora = view.findViewById<TextView>(R.id.tvDesenvolvedora)
        val tvGenero = view.findViewById<TextView>(R.id.tvGenero)

        imgCapa.setImageResource(jogo.foto)
        tvNome.text = jogo.nome
        tvDesenvolvedora.text = jogo.desenvolvedora
        tvGenero.text = jogo.genero

        return view
    }
}