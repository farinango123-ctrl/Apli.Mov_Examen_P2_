package com.example.techaudit20.uil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.techaudit20.TechAuditApp
import com.example.techaudit20.data.AuditRepository
import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.Laboratorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuditViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuditRepository
    private val database = (application as TechAuditApp).database
    private val labDao = database.labDao()
    private val auditDao = database.auditDao()
    
    val allItems: LiveData<List<AuditItem>>
    val allLabs: LiveData<List<Laboratorio>>
    
    private val _syncStatus = MutableLiveData<Boolean?>()
    val syncStatus: LiveData<Boolean?> = _syncStatus

    init {
        repository = AuditRepository(auditDao)
        allItems = repository.allItem.asLiveData()
        allLabs = labDao.getAllLabs().asLiveData()
    }

    fun insertLab(lab: Laboratorio) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            labDao.insert(lab)
        }
    }

    fun insert(item: AuditItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: AuditItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: AuditItem) = viewModelScope.launch {
        repository.delete(item)
    }

    fun sincronizarConNube() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            // Obtener datos directamente de la DB para evitar nulos de LiveData
            val labs = labDao.getAllLabsSync() 
            val equipos = auditDao.getAllEquiposSync()
            
            val resultado = repository.sincronizarTodo(labs, equipos)
            _syncStatus.postValue(resultado)
        }
    }
}
