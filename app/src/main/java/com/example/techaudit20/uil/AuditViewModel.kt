package com.example.techaudit20.uil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.techaudit20.TechAuditApp
import com.example.techaudit20.data.AuditRepository
import com.example.techaudit20.model.AuditItem
import kotlinx.coroutines.launch

class AuditViewModel (application: Application): AndroidViewModel(application) {


    private val repository: AuditRepository

    val allItems: LiveData<List<AuditItem>>

    init {
        val dao = (application as TechAuditApp).database.auditDao()
        repository = AuditRepository(dao)

        allItems = repository.allItem.asLiveData()


    }
    fun delete (item: AuditItem) = viewModelScope.launch {
        repository.delete(item)
    }


}