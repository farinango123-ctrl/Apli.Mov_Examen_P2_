package com.example.techaudit20.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.databinding.ItemLaboratorioBinding
import com.example.techaudit20.model.Laboratorio

class LaboratorioAdapter(
    private val onItemSelected: (Laboratorio) -> Unit
) : ListAdapter<Laboratorio, LaboratorioAdapter.LaboratorioViewHolder>(LaboratorioDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaboratorioViewHolder {
        val binding = ItemLaboratorioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LaboratorioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LaboratorioViewHolder, position: Int) {
        holder.bind(getItem(position), onItemSelected)
    }

    class LaboratorioViewHolder(private val binding: ItemLaboratorioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(laboratorio: Laboratorio, onItemSelected: (Laboratorio) -> Unit) {
            binding.tvNombreLab.text = laboratorio.nombre
            binding.tvEdificioLab.text = "Edificio: ${laboratorio.edificio}"
            binding.root.setOnClickListener {
                onItemSelected(laboratorio)
            }
        }
    }

    class LaboratorioDiffCallback : DiffUtil.ItemCallback<Laboratorio>() {
        override fun areItemsTheSame(oldItem: Laboratorio, newItem: Laboratorio): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Laboratorio, newItem: Laboratorio): Boolean {
            return oldItem == newItem
        }
    }
}
