package com.example.vilaxavier.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.vilaxavier.databinding.ItemServicoBinding
import com.example.vilaxavier.model.Jogo

class JogoAdapter (
    context: Context,
    private val lista: List<Jogo>
    ) : ArrayAdapter<Jogo>(context, 0, lista) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val binding: ItemServicoBinding
            val itemView: View

            if (convertView == null) {
                binding = ItemServicoBinding.inflate(LayoutInflater.from(context), parent, false)
                itemView = binding.root
                itemView.tag = binding
            } else {
                itemView = convertView
                binding = itemView.tag as ItemServicoBinding
            }

            val jogo = lista[position]

            binding.tvNome.text = jogo.nome
            binding.tvDesenvolvedora.text = jogo.desenvolvedora
            binding.tvGenero.text = jogo.genero
            binding.tvDescricao.text = jogo.descricao
            binding.imgFoto.setImageResource(jogo.foto)


            return itemView
        }
}