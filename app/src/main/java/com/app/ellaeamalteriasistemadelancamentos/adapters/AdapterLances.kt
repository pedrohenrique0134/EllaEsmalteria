package com.app.ellaeamalteriasistemadelancamentos.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ellaeamalteriasistemadelancamentos.R
import com.app.ellaeamalteriasistemadelancamentos.databinding.ItemMovimentacoesBinding
import com.app.ellaeamalteriasistemadelancamentos.models.Lancamentos
import com.app.ellaeamalteriasistemadelancamentos.utils.numberCurrency
import com.app.ellaeamalteriasistemadelancamentos.utils.toPrettyDate

class AdapterLances(
    var context: Context,
    private var arrayList: ArrayList<Lancamentos>
) : RecyclerView.Adapter<AdapterLances.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemMovimentacoesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(lancamentos: Lancamentos) {
            binding.client.text = lancamentos.client
            if (lancamentos.entryOrExit == "Entradas"){
                binding.tipo.text = "Entrada recebida (${lancamentos.tipo})"
                binding.icon.setImageResource(R.drawable.item)
            }else{
                binding.icon.setImageResource(R.drawable.red)
                binding.tipo.text = "Saída (${lancamentos.tipo})"
                binding.valor.setTextColor(R.color.red)
            }
            binding.time.text = lancamentos.time?.toLong()?.toPrettyDate()
            binding.valor.text = "R$" + numberCurrency(lancamentos.valor.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterLances.MyViewHolder {
        val v = ItemMovimentacoesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: AdapterLances.MyViewHolder, position: Int) {
        val currentItem = arrayList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}