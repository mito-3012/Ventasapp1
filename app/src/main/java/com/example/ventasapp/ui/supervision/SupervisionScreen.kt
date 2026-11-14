package com.example.ventasapp.ui.supervision

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ventasapp.ui.components.BannerMensaje
import com.example.ventasapp.ui.components.EstadoVacio
import com.example.ventasapp.ui.components.PanelFiltros
import com.example.ventasapp.ui.components.PantallaBase
import com.example.ventasapp.ui.components.TarjetaVenta

/**
 * Pantalla del SUPERVISOR: ve todas las ventas del sistema, con filtros, y puede abrir cada una
 * para revisar sus datos, su respaldo y su historial. No tiene botones para modificar nada.
 */
@Composable
fun SupervisionScreen(
    viewModel: SupervisionViewModel,
    onVerVenta: (Long) -> Unit,
    onCerrarSesion: () -> Unit,
    barraInferior: @Composable () -> Unit
) {
    val filtros by viewModel.filtros.collectAsState()
    val ventas by viewModel.filtradas.collectAsState()
    val vendedores by viewModel.vendedores.collectAsState()

    PantallaBase(
        titulo = "Supervisión de ventas",
        onCerrarSesion = onCerrarSesion,
        barraInferior = barraInferior
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            BannerMensaje(
                texto = "Perfil de consulta: puedes ver todas las ventas y su historial, sin modificarlas.",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            PanelFiltros(
                filtros = filtros,
                onFiltrosChange = viewModel::onFiltrosChange,
                vendedores = vendedores
            )

            if (ventas.isEmpty()) {
                EstadoVacio(
                    icono = Icons.Default.Inbox,
                    titulo = if (filtros.hayFiltrosActivos) "Sin resultados" else "No hay ventas registradas",
                    mensaje = if (filtros.hayFiltrosActivos) "Prueba cambiando o limpiando los filtros."
                    else "Cuando los vendedores registren ventas, aparecerán aquí."
                )
            } else {
                Text(
                    "${ventas.size} venta(s)",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = ventas, key = { it.id }) { venta ->
                        TarjetaVenta(
                            venta = venta,
                            onClick = { onVerVenta(venta.id) },
                            mostrarVendedor = true,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}
