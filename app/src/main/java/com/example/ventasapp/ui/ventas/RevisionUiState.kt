package com.example.ventasapp.ui.ventas

import com.example.ventasapp.data.model.AccionRevision

/** Estado del panel de revisión que usa el encargado en el detalle de la venta. */
data class RevisionUiState(
    val observacion: String = "",
    val errorObservacion: String? = null,
    /** Acción que espera confirmación en el AlertDialog (null = no hay diálogo abierto). */
    val accionPendiente: AccionRevision? = null,
    val procesando: Boolean = false
)
