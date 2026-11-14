package com.example.ventasapp.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/*
 * ENTIDADES DE ROOM (tablas de la base de datos).
 *
 * Recordatorio de la guía de persistencia: una Entity NO es lo mismo que el UiState.
 *  - UiState  = lo que se está escribiendo en el formulario + sus errores (vive en memoria).
 *  - Entity   = lo que se guarda de forma permanente en el teléfono.
 *
 * Los enums (perfil, estado, tipo de convenio) se guardan como texto con el nombre del enum.
 * Es simple, legible al revisar la base y no requiere TypeConverters.
 */

@Entity(
    tableName = "usuarios",
    // Índice único: dos cuentas no pueden compartir correo.
    indices = [Index(value = ["correo"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val correo: String,
    /** Hash PBKDF2 de la contraseña. La contraseña original jamás se guarda. */
    val claveHash: String,
    /** Sal aleatoria propia de este usuario (ver util/Seguridad). */
    val sal: String,
    val perfil: String,
    val zona: String,
    val activo: Boolean = true,
    val fechaCreacion: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "ventas",
    indices = [Index("vendedorId"), Index("estado")]
)
data class VentaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val vendedorId: Long,
    /** Copia del nombre del vendedor al momento de crear la venta; evita un JOIN en cada listado. */
    val vendedorNombre: String,

    val tipoConvenio: String,
    val prestador: String,
    val fechaOperacion: Long?,
    val monto: Int,
    val codigoAutorizacion: String,

    // Datos del cliente: SIEMPRE ficticios (restricción de protección de datos del caso).
    val clienteNombre: String,
    val clienteRut: String,
    val clienteCorreo: String,
    val clienteTelefono: String,
    val mascotaNombre: String = "",

    /** Ruta del archivo de imagen en el almacenamiento privado de la app (null = sin respaldo). */
    val respaldoPath: String? = null,

    val estado: String,
    val observacion: String? = null,
    val motivoRechazo: String? = null,

    val fechaEnvio: Long? = null,
    val fechaRevision: Long? = null,
    val revisorId: Long? = null,
    val revisorNombre: String? = null,
    val fechaActualizacion: Long = System.currentTimeMillis()
)

/** Cada fila es un cambio relevante de una venta: quién, cuándo, de qué estado a cuál. */
@Entity(
    tableName = "historial",
    indices = [Index("ventaId")]
)
data class HistorialEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ventaId: Long,
    val estadoAnterior: String?,
    val estadoNuevo: String,
    val accion: String,
    val usuarioId: Long,
    val usuarioNombre: String,
    val fecha: Long = System.currentTimeMillis(),
    val observacion: String? = null
)

/**
 * Alertas internas (el caso permite notificaciones dentro de la app).
 *
 * El destino es un usuario puntual (usuarioDestinoId) o, si ese campo es null,
 * todos los usuarios del perfil indicado en perfilDestino (por ejemplo "todos los encargados").
 */
@Entity(
    tableName = "notificaciones",
    indices = [Index("usuarioDestinoId"), Index("perfilDestino")]
)
data class NotificacionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuarioDestinoId: Long?,
    val perfilDestino: String,
    val ventaId: Long,
    val mensaje: String,
    val fecha: Long = System.currentTimeMillis(),
    val leida: Boolean = false
)

/**
 * Porcentaje de comisión (ficticio) por tipo de convenio. Lo edita el administrador.
 * Se guarda como fracción: 0.10 equivale a 10%.
 */
@Entity(tableName = "comisiones")
data class ComisionEntity(
    @PrimaryKey val tipoConvenio: String,
    val porcentaje: Double
)
