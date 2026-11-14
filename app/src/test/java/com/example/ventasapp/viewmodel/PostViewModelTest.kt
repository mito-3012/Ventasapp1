package com.example.apirest.viewmodel

import com.example.ventasapp.data.model.Post
import com.example.ventasapp.viewmodel.PostViewModelimport kotlinx.coroutines.ExperimentalCoroutinesApi







@OptIn(ExperimentalCoroutinesApi::class)

class PostViewModelTest : StringSpec({



    "postList debe contener los datos esperados después de fetchPosts" {



        // Creamos una lista falsa de publicaciones.

        val fakePosts = listOf(

            Post(

                userId = 1,

                id = 1,

                title = "Titulo 1",

                body = "Contenido 1"

            ),

            Post(

                userId = 2,

                id = 2,

                title = "Titulo 2",

                body = "Contenido 2"

            )

        )



        // Creamos un ViewModel de prueba.

        val testViewModel = object : PostViewModel() {



            override fun fetchPosts() {

                _postList.value = fakePosts

            }

        }



        // Ejecutamos la prueba.

        runTest {

            testViewModel.fetchPosts()



            testViewModel.postList.value shouldContainExactly fakePosts

        }

    }

})