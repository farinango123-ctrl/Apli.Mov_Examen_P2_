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
import com.example.techauditoria.adapter.AuditAdapter
import com.example.techauditoria.databinding.ActivityMainBinding
import com.example.techauditoria.uil.AuditViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AuditAdapter
    private val viewModel: AuditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        configurararDeslizarParaBorrar()

        viewModel.allItems.observe(this) { listaActualizada ->
            adapter.actualizarLista(listaActualizada)
        }

        binding.fabAgregar.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        // Al hacer clic, enviamos a AddEditActivity para EDITAR el equipo
        adapter = AuditAdapter(mutableListOf()) { itemSeleccionndo ->
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("EXTRA_ITEM", itemSeleccionndo)
            startActivity(intent)
        }

        binding.rvAuditoria.adapter = adapter
        binding.rvAuditoria.layoutManager = LinearLayoutManager(this)
    }

    private fun configurararDeslizarParaBorrar() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Ahora podemos usar .listaAuditoria porque ya no es privada en el Adapter
                    val item = adapter.listaAuditoria[position]
                    viewModel.delete(item)
                    Toast.makeText(this@MainActivity, "Equipo eliminado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvAuditoria)
    }
}
