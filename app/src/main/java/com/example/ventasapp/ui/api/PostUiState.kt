package com.example.ventasapp.ui.api

import com.example.ventasapp.data.model.Post

// Estado observable de la pantalla: publicaciones, carga, errores y operación en curso.
data class PostUiState(
    val posts: List<Post> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null,
    val cargaIntentada: Boolean = false,
    val procesandoId: Int? = null
)

// Identifica la operación que realizará el formulario sobre la publicación.
enum class ModoFormularioPost { CREAR, REEMPLAZAR, ACTUALIZAR_TITULO }

// Conserva los valores del formulario y permite que Compose refleje sus cambios.
data class PostFormUiState(
    val visible: Boolean = false,
    val modo: ModoFormularioPost = ModoFormularioPost.CREAR,
    val postId: Int? = null,
    val userId: Int = 1,
    val titulo: String = "",
    val cuerpo: String = "",
    val guardando: Boolean = false,
    val errores: PostFormErrores = PostFormErrores()
)

// Almacena los mensajes de validación asociados a cada campo del formulario.
data class PostFormErrores(
    val titulo: String? = null,
    val cuerpo: String? = null
)
