package com.example.techaudit20.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.databinding.ActivityAuditItemBinding
import com.example.techaudit20.model.AuditStatus
import com.example.techaudit20.model.AuditItem

class AuditAdapter(
    private val onItemSelected: (AuditItem) -> Unit
) : ListAdapter<AuditItem, AuditAdapter.AuditViewHolder>(AuditDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditViewHolder {
        val binding = ActivityAuditItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AuditViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuditViewHolder, position: Int) {
        holder.bind(getItem(position), onItemSelected)
    }

    class AuditViewHolder(private val binding: ActivityAuditItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AuditItem, onItemSelected: (AuditItem) -> Unit) {
            binding.tvNombreEquipo.text = item.nombre
            binding.tvUbicacion.text = item.ubicacion
            binding.tvEstadoLabel.text = item.estado.name

            val colorEstado = when (item.estado) {
                AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50") // Verde
                AuditStatus.PENDIENTE -> Color.parseColor("#FFC107") // Amarillo
                AuditStatus.DANIADO -> Color.parseColor("#F44336")   // Rojo
                AuditStatus.NO_ENCONTRA -> Color.BLACK
            }

            binding.viewStatusColor.setBackgroundColor(colorEstado)
            binding.tvEstadoLabel.setTextColor(colorEstado)

            binding.root.setOnClickListener {
                onItemSelected(item)
            }
        }
    }

    class AuditDiffCallback : DiffUtil.ItemCallback<AuditItem>() {
        override fun areItemsTheSame(oldItem: AuditItem, newItem: AuditItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AuditItem, newItem: AuditItem): Boolean {
            return oldItem == newItem
        }
    }
}
