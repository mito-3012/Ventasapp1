package com.example.ventasapp.repository

import com.example.ventasapp.data.model.Post
import com.example.ventasapp.data.model.repository.PostRepository
import com.example.ventasapp.data.remote.ApiService


class TestablePostRepository(

    private val testApi: ApiService

) : PostRepository() {



    override suspend fun getPosts(): List<Post> {

        return testApi.getPosts()

    }

}



class PostRepositoryTest : StringSpec({



    "getPosts() debe retornar una lista de posts simulada" {



        // 1. Simulamos el resultado de la API.

        val fakePosts = listOf(

            Post(

                userId = 1,

                id = 1,

                title = "Titulo 1",

                body = "Cuerpo 1"

            ),

            Post(

                userId = 2,

                id = 2,

                title = "Titulo 2",

                body = "Cuerpo 2"

            )

        )



        // 2. Creamos un mock de ApiService.

        val mockApi = mockk<ApiService>()



        coEvery {

            mockApi.getPosts()

        } returns fakePosts



        // 3. Creamos el repositorio de prueba.

        val repo = TestablePostRepository(mockApi)



        // 4. Ejecutamos el test.

        runTest {

            val result = repo.getPosts()



            result shouldContainExactly fakePosts

        }

    }

})