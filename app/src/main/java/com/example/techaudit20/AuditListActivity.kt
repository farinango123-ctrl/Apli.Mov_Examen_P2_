package com.example.techaudit20

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.adapter.AuditAdapter
import com.example.techaudit20.databinding.ActivityAuditListBinding
import com.example.techaudit20.uil.AuditViewModel

class AuditListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuditListBinding
    private lateinit var adapter: AuditAdapter
    private val viewModel: AuditViewModel by viewModels()
    private var labId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuditListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        labId = intent.getStringExtra("EXTRA_LAB_ID")

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupSwipeToDelete()

        // Filtrar equipos por Laboratorio
        viewModel.allItems.observe(this) { listaCompleta ->
            val listaFiltrada = listaCompleta.filter { it.laboratorioId == labId }
            adapter.submitList(listaFiltrada)
        }

        binding.fabAddEquipo.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("EXTRA_LAB_ID", labId)
            startActivity(intent)
        }
        
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = AuditAdapter { itemSeleccionado ->
            // Al dar clic, vamos a AddEditActivity para EDITAR el equipo
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("EXTRA_ITEM", itemSeleccionado)
            startActivity(intent)
        }

        binding.rvEquipos.adapter = adapter
        binding.rvEquipos.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val item = adapter.currentList[position]
                viewModel.delete(item)
                Toast.makeText(this@AuditListActivity, "${item.nombre} eliminado", Toast.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvEquipos)
    }
}
