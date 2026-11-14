package com.example.ventasapp.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/*
 * DAOs (Data Access Object): aquí se declaran las consultas SQL.
 *  - Las funciones "suspend" se ejecutan fuera del hilo principal (corrutinas).
 *  - Las que devuelven Flow se vuelven a emitir solas cada vez que cambia la tabla; así la UI
 *    se actualiza sin que nadie tenga que "refrescar" a mano.
 */

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: UsuarioEntity): Long

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun buscarPorId(id: Long): UsuarioEntity?

    @Update
    suspend fun actualizar(usuario: UsuarioEntity)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    /** Lista completa para la pantalla de administración de usuarios. */
    @Query("SELECT * FROM usuarios ORDER BY perfil, nombre")
    fun observarTodos(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE perfil = 'VENDEDOR' AND activo = 1 ORDER BY nombre")
    fun observarVendedores(): Flow<List<UsuarioEntity>>
}

@Dao
interface VentaDao {

    @Insert
    suspend fun insertar(venta: VentaEntity): Long

    @Update
    suspend fun actualizar(venta: VentaEntity)

    @Query("SELECT * FROM ventas WHERE id = :id")
    suspend fun obtenerPorId(id: Long): VentaEntity?

    @Query("SELECT * FROM ventas WHERE id = :id")
    fun observarPorId(id: Long): Flow<VentaEntity?>

    /** Control de acceso: el vendedor solo consulta las ventas cuyo vendedorId es el suyo. */
    @Query("SELECT * FROM ventas WHERE vendedorId = :vendedorId ORDER BY fechaActualizacion DESC")
    fun observarDeVendedor(vendedorId: Long): Flow<List<VentaEntity>>

    /** El encargado ve todo lo enviado, pero nunca los borradores de los vendedores. */
    @Query("SELECT * FROM ventas WHERE estado != 'BORRADOR' ORDER BY fechaActualizacion DESC")
    fun observarRecibidas(): Flow<List<VentaEntity>>

    /** El supervisor consulta todas las ventas, incluidos los borradores. */
    @Query("SELECT * FROM ventas ORDER BY fechaActualizacion DESC")
    fun observarTodas(): Flow<List<VentaEntity>>

    /** Rutas de fotos en uso: sirve para detectar y borrar fotos que quedaron sin venta. */
    @Query("SELECT respaldoPath FROM ventas WHERE respaldoPath IS NOT NULL")
    suspend fun rutasDeRespaldo(): List<String>

    /** Cuenta otras ventas con el mismo código (se excluye la propia al editar). Detecta duplicados. */
    @Query("SELECT COUNT(*) FROM ventas WHERE codigoAutorizacion = :codigo COLLATE NOCASE AND id != :excluirId")
    suspend fun contarCodigo(codigo: String, excluirId: Long): Int

    @Query("DELETE FROM ventas WHERE id = :id")
    suspend fun eliminar(id: Long)

    @Query("UPDATE ventas SET respaldoPath = NULL")
    suspend fun limpiarRutasDeRespaldo()
}

@Dao
interface HistorialDao {

    @Insert
    suspend fun insertar(registro: HistorialEntity)

    @Query("SELECT * FROM historial WHERE ventaId = :ventaId ORDER BY fecha DESC, id DESC")
    fun observarDeVenta(ventaId: Long): Flow<List<HistorialEntity>>

    @Query("DELETE FROM historial WHERE ventaId = :ventaId")
    suspend fun eliminarDeVenta(ventaId: Long)
}

@Dao
interface NotificacionDao {

    @Insert
    suspend fun insertar(notificacion: NotificacionEntity)

    @Query(
        "SELECT * FROM notificaciones " +
            "WHERE usuarioDestinoId = :usuarioId OR (usuarioDestinoId IS NULL AND perfilDestino = :perfil) " +
            "ORDER BY fecha DESC, id DESC"
    )
    fun observarPara(usuarioId: Long, perfil: String): Flow<List<NotificacionEntity>>

    @Query(
        "SELECT COUNT(*) FROM notificaciones " +
            "WHERE leida = 0 AND (usuarioDestinoId = :usuarioId OR (usuarioDestinoId IS NULL AND perfilDestino = :perfil))"
    )
    fun observarNoLeidas(usuarioId: Long, perfil: String): Flow<Int>

    @Query(
        "UPDATE notificaciones SET leida = 1 " +
            "WHERE usuarioDestinoId = :usuarioId OR (usuarioDestinoId IS NULL AND perfilDestino = :perfil)"
    )
    suspend fun marcarTodasLeidas(usuarioId: Long, perfil: String)
}

@Dao
interface ComisionDao {

    @Query("SELECT * FROM comisiones")
    fun observarTodas(): Flow<List<ComisionEntity>>

    /** Crea el valor inicial solo si todavía no existe (no pisa lo que ya configuró el administrador). */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarSiNoExiste(comision: ComisionEntity)

    /** Inserta o reemplaza (upsert) el porcentaje de un convenio. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(comision: ComisionEntity)
}
