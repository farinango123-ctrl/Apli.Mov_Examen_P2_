package com.example.techaudit20
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.techaudit20.databinding.ActivityAddEditBinding
import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.AuditStatus
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private var itemAEditar: AuditItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSpinner()

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

            // Seleccionar el estado en el spinner
            val posicion = AuditStatus.entries.indexOf(item.estado)
            binding.spEstado.setSelection(posicion)

            binding.btnGuardar.text = "Actualizar Registro"
        }

        binding.btnGuardar.setOnClickListener {
            guardarRegistro()
        }
    }

    private fun setupSpinner() {
        val estados = AuditStatus.entries.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstado.adapter = adapter
    }

    private fun guardarRegistro() {
        val nombre = binding.etNombre.text.toString()
        val ubicacion = binding.etUbicacion.text.toString()
        val notas = binding.etNotas.text.toString()

        if (nombre.isBlank() || ubicacion.isBlank()) {
            Toast.makeText(this, "Nombre y Ubicación son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val estadoSeleccionado = binding.spEstado.selectedItem as AuditStatus

        // Si estamos editando, usamos el mismo ID. Si es nuevo, generamos uno.
        val id = itemAEditar?.id ?: UUID.randomUUID().toString()
        val fecha = itemAEditar?.fechaRegistro ?: Date().toString()

        val itemAGuardar = AuditItem(
            id = id,
            nombre = nombre,
            ubicacion = ubicacion,
            fechaRegistro = fecha,
            estado = estadoSeleccionado,
            notas = notas
        )

        // Corregido el cast a TechAuditApp
        val app = application as? TechAuditApp
        val database = app?.database

        if (database != null) {
            lifecycleScope.launch {
                database.auditDao().insert(itemAGuardar)
                Toast.makeText(this@AddEditActivity, "¡Guardado!", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Error: No se pudo acceder a la base de datos", Toast.LENGTH_SHORT).show()
        }
    }
}
