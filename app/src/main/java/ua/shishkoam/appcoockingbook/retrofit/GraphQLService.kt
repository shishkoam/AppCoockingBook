package ua.shishkoam.appcoockingbook.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GraphQLService {
//    @Headers("Content-Type: application/json")
    @POST("graphql")
    suspend fun postDynamicQuery(@Body body: String): Response<String>
}