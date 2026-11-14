package com.example.ventasapp.ui.login

/** Estado completo de la pantalla de inicio de sesión (mismo patrón de la guía 11). */
data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val mostrarClave: Boolean = false,
    val cargando: Boolean = false,
    val errores: LoginErrores = LoginErrores()
)

/** Un String? por campo: null significa "campo válido". */
data class LoginErrores(
    val correo: String? = null,
    val clave: String? = null,
    /** Error que no pertenece a un campo puntual (credenciales incorrectas, cuenta desactivada). */
    val general: String? = null
)
