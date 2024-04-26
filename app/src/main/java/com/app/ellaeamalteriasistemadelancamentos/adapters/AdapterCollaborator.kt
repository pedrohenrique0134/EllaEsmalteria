package com.app.ellaeamalteriasistemadelancamentos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ellaeamalteriasistemadelancamentos.databinding.ItemUsersBinding
import com.app.ellaeamalteriasistemadelancamentos.models.Collaborators
import com.squareup.picasso.Picasso

class AdapterCollaborator(var context: Context,
                          private  var arrayList: ArrayList<Collaborators>,
                          val clickUser: ClickUser)
    : RecyclerView.Adapter<AdapterCollaborator.MyViewHolder>(){

        interface ClickUser{
            fun clickUser(collaborators: Collaborators)
        }

        inner class MyViewHolder(val binding: ItemUsersBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(collaborators: Collaborators){
                    binding.userName.text = collaborators.nome
                    Picasso.get().load(collaborators.foto).into(binding.imgCollaborator)
                    binding.llBox.setOnClickListener {
                        clickUser.clickUser(collaborators)
                    }
                }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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