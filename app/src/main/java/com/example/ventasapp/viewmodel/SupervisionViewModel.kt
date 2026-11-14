package com.example.ventasapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ventasapp.data.model.FiltrosVentas
import com.example.ventasapp.data.model.UsuarioEntity
import com.example.ventasapp.data.model.VentaEntity
import com.example.ventasapp.data.model.aplicarFiltros
import com.example.ventasapp.repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel del SUPERVISOR: consulta TODAS las ventas (incluidos los borradores) con los mismos
 * filtros del encargado. Es de solo lectura: no expone ninguna función que modifique datos.
 */
class SupervisionViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = repositorio

    private val _filtros = MutableStateFlow(FiltrosVentas())
    val filtros: StateFlow<FiltrosVentas> = _filtros

    val todas: StateFlow<List<VentaEntity>> = repo.ventasTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val filtradas: StateFlow<List<VentaEntity>> = combine(todas, _filtros) { lista, filtros ->
        lista.aplicarFiltros(filtros)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Opciones del filtro "vendedor". */
    val vendedores: StateFlow<List<UsuarioEntity>> = repo.vendedores()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        // Al cambiar de usuario no deben quedar filtros del anterior.
        viewModelScope.launch {
            repo.sesion.usuario.collect { _filtros.value = FiltrosVentas() }
        }
    }

    fun onFiltrosChange(nuevos: FiltrosVentas) {
        _filtros.value = nuevos
    }
}