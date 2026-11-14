package com.example.ventasapp.ui.ventas

import com.example.ventasapp.data.model.EstadoVenta
import com.example.ventasapp.data.model.TipoConvenio

/**
 * Estado del formulario de venta. Los campos de texto se guardan como String porque es lo que
 * escribe el usuario; la conversión a Int/Long se hace recién al construir la entidad de Room.
 *
 * Importante: este NO es el objeto que se guarda en la base de datos (ese es VentaEntity).
 * Aquí vive lo que se está escribiendo más los errores de validación.
 */
data class VentaFormUiState(
    val ventaId: Long = 0L,
    val estadoOriginal: EstadoVenta = EstadoVenta.BORRADOR,
    /** Observación que dejó el encargado cuando solicitó la corrección (se muestra como aviso). */
    val observacionPrevia: String? = null,

    val tipoConvenio: TipoConvenio? = null,
    val prestador: String = "",
    val fechaOperacion: Long? = null,
    val monto: String = "",
    val codigo: String = "",

    val clienteNombre: String = "",
    val clienteRut: String = "",
    val clienteCorreo: String = "",
    val clienteTelefono: String = "",
    val mascotaNombre: String = "",

    val respaldoPath: String? = null,

    val cargando: Boolean = false,
    val procesandoImagen: Boolean = false,
    val guardando: Boolean = false,
    /** true si la venta pedida no existe o pertenece a otro vendedor. */
    val noAutorizado: Boolean = false,
    val errores: VentaFormErrores = VentaFormErrores()
) {
    val esEdicion: Boolean get() = ventaId != 0L
}

data class VentaFormErrores(
    val tipoConvenio: String? = null,
    val prestador: String? = null,
    val fecha: String? = null,
    val monto: String? = null,
    val codigo: String? = null,
    val clienteNombre: String? = null,
    val clienteRut: String? = null,
    val clienteCorreo: String? = null,
    val clienteTelefono: String? = null,
    val mascota: String? = null,
    val respaldo: String? = null
) {
    /** true si hay al menos un error en el formulario. */
    val hayErrores: Boolean
        get() = listOfNotNull(
            tipoConvenio, prestador, fecha, monto, codigo, clienteNombre, clienteRut,
            clienteCorreo, clienteTelefono, mascota, respaldo
        ).isNotEmpty()
}
