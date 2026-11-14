package com.example.ventasapp.ui.login

/** Estado del formulario de creación de cuenta ficticia de vendedor (extiende la guía 11). */
data class RegistroUsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val zona: String = "",
    val aceptaTerminos: Boolean = false,
    val mostrarClave: Boolean = false,
    val cargando: Boolean = false,
    val errores: RegistroUsuarioErrores = RegistroUsuarioErrores()
)

data class RegistroUsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val confirmarClave: String? = null,
    val zona: String? = null,
    val terminos: String? = null
)
