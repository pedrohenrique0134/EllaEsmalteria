package com.app.ellaeamalteriasistemadelancamentos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ellaeamalteriasistemadelancamentos.R
import com.app.ellaeamalteriasistemadelancamentos.databinding.ItemClientesBinding
import com.app.ellaeamalteriasistemadelancamentos.databinding.ItemUsersBinding
import com.app.ellaeamalteriasistemadelancamentos.models.Cliente
import com.app.ellaeamalteriasistemadelancamentos.models.Collaborators
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class AdapterClientes(
    var context: Context,
    private var arrayList: ArrayList<Cliente>
) : RecyclerView.Adapter<AdapterClientes.MyViewHolder>() {


    inner class MyViewHolder(val binding: ItemClientesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cliente: Cliente) {
            binding.nomeCliente.text = cliente.nome
            binding.emailCliente.text = cliente.email
            binding.telefoneCliente.text = cliente.telefone
            binding.aniversario.text = cliente.aniversario

            binding.time.setOnClickListener {
                if (binding.llMoreData.visibility == View.VISIBLE) {
                    binding.llMoreData.visibility = View.GONE
                    binding.time.setImageResource(R.drawable.arrow_drop_down)
                } else {
                    binding.llMoreData.visibility = View.VISIBLE
                    binding.time.setImageResource(R.drawable.arrow_drop_up)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = ItemClientesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = arrayList[position]
        holder.bind(currentItem)
    }

}