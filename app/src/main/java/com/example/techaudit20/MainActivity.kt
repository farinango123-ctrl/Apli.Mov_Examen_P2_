package com.example.techaudit20

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techaudit20.adapter.LaboratorioAdapter
import com.example.techaudit20.databinding.ActivityMainBinding
import com.example.techaudit20.model.Laboratorio
import com.example.techaudit20.uil.AuditViewModel
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LaboratorioAdapter
    private val viewModel: AuditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()

        // Observar laboratorios
        viewModel.allLabs.observe(this) { listaLabs ->
            adapter.submitList(listaLabs)
        }

        // Botón para sincronizar con la nube
        binding.btnSincronizar.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.sincronizarConNube()
        }

        // Observar estado de sincronización
        viewModel.syncStatus.observe(this) { exito ->
            binding.progressBar.visibility = View.GONE
            if (exito == true) {
                Toast.makeText(this, "¡Sincronización Exitosa con MockAPI!", Toast.LENGTH_LONG).show()
            } else if (exito == false) {
                Toast.makeText(this, "Error de conexión con la nube", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para agregar laboratorio (Entidad 1)
        binding.fabAddLab.setOnClickListener {
            mostrarDialogoNuevoLab()
        }
    }

    private fun mostrarDialogoNuevoLab() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_lab, null)
        val etNombre = view.findViewById<EditText>(R.id.etNombreLab)
        val etEdificio = view.findViewById<EditText>(R.id.etEdificioLab)

        AlertDialog.Builder(this)
            .setTitle("Nuevo Laboratorio")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val edificio = etEdificio.text.toString()
                if (nombre.isNotBlank() && edificio.isNotBlank()) {
                    val nuevoLab = Laboratorio(
                        id = UUID.randomUUID().toString(),
                        nombre = nombre,
                        edificio = edificio
                    )
                    viewModel.insertLab(nuevoLab)
                } else {
                    Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun setupRecyclerView() {
        adapter = LaboratorioAdapter { labSeleccionado ->
            val intent = Intent(this, AuditListActivity::class.java)
            intent.putExtra("EXTRA_LAB_ID", labSeleccionado.id)
            startActivity(intent)
        }

        binding.rvLaboratorios.adapter = adapter
        binding.rvLaboratorios.layoutManager = LinearLayoutManager(this)
    }
}
