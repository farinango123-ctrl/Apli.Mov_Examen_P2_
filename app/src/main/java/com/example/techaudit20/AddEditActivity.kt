package com.example.techaudit20

import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.techaudit20.databinding.ActivityAddEditBinding
import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.AuditStatus
import com.example.techaudit20.uil.AuditViewModel
import java.util.Date
import java.util.UUID

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private val viewModel: AuditViewModel by viewModels()
    private var itemAEditar: AuditItem? = null
    private var currentLabId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar el ID del laboratorio desde el intent
        currentLabId = intent.getStringExtra("EXTRA_LAB_ID")

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSpinners()

        // Intentar recuperar el item si venimos de un clic para editar
        itemAEditar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_ITEM", AuditItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_ITEM")
        }

        itemAEditar?.let { item ->
            binding.etNombre.setText(item.nombre)
            binding.etUbicacion.setText(item.ubicacion)
            binding.etNotas.setText(item.notas)

            val posicion = AuditStatus.entries.indexOf(item.estado)
            binding.spEstado.setSelection(posicion)

            currentLabId = item.laboratorioId

            binding.btnGuardar.text = "Actualizar Registro"
            title = "Editar Equipo"
        } ?: run {
            title = "Nuevo Registro"
        }

        binding.btnGuardar.setOnClickListener {
            guardarRegistro()
        }
    }

    private fun setupSpinners() {
        val estados = AuditStatus.entries.toTypedArray()
        val adapterEstado = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstado.adapter = adapterEstado

        binding.tvLabLabel.visibility = android.view.View.GONE
        binding.spLaboratorio.visibility = android.view.View.GONE
    }

    private fun guardarRegistro() {
        val nombre = binding.etNombre.text.toString()
        val ubicacion = binding.etUbicacion.text.toString()
        val notas = binding.etNotas.text.toString()

        if (nombre.isBlank() || ubicacion.isBlank()) {
            Toast.makeText(this, "Nombre y Ubicación son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }
        
        // CORRECCIÓN: Si no hay laboratorio seleccionado, no dejar guardar.
        if (currentLabId == null) {
            Toast.makeText(this, "Error: Seleccione un laboratorio primero", Toast.LENGTH_SHORT).show()
            return
        }

        val estadoSeleccionado = binding.spEstado.selectedItem as AuditStatus
        val id = itemAEditar?.id ?: UUID.randomUUID().toString()
        val fecha = itemAEditar?.fechaRegistro ?: Date().toString()

        val itemAGuardar = AuditItem(
            id = id,
            nombre = nombre,
            ubicacion = ubicacion,
            fechaRegistro = fecha,
            estado = estadoSeleccionado,
            notas = notas,
            laboratorioId = currentLabId!! // Ya no usamos "LAB_GENERAL"
        )

        if (itemAEditar == null) {
            viewModel.insert(itemAGuardar)
        } else {
            viewModel.update(itemAGuardar)
        }
        
        finish()
    }
}
