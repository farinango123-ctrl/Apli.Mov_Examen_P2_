package com.example.techaudit20

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.techaudit20.databinding.ActivityDetailBinding
import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.AuditStatus

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar la barra de retroceso (ActionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Recuperamos el objeto enviado
        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_ITEM", AuditItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_ITEM")
        }

        // Mostrar datos si el objeto existe
        item?.let {
            mostrarDetalle(it)
        }

        // Configurar botón volver
        binding.btnVolver.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun mostrarDetalle(item: AuditItem) {
        binding.tvDetalleNombre.text = item.nombre
        binding.tvDetalleId.text = "ID: #${item.id.take(8)}"
        binding.tvDetalleUbicacion.text = item.ubicacion
        binding.tvDetalleFecha.text = item.fechaRegistro
        binding.tvDetalleNotas.text = item.notas.ifBlank { "Sin notas registradas" }

        // Lógica visual según el estado (pintar la cabecera)
        val color = when (item.estado) {
            AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50") // Verde
            AuditStatus.PENDIENTE -> Color.parseColor("#FFC107") // Amarillo
            AuditStatus.DANIADO -> Color.parseColor("#F44336")   // Rojo
            AuditStatus.NO_ENCONTRA -> Color.BLACK
        }
        binding.viewHeaderStatus.setBackgroundColor(color)

        title = "Detalle del Equipo"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
