package com.grupo5.tickets4u

import com.grupo5.tickets4u.login.LoginRequest
import com.grupo5.tickets4u.login.LoginResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.grupo5.tickets4u.login.*
import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import okhttp3.ResponseBody
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("api/eventos")
    suspend fun getEventos(): List<Event>

    @POST("api/eventos")
    suspend fun crearEvento(@Body evento: Event): retrofit2.Response<Event>

    @PUT("api/eventos/{id}")
    suspend fun editarEvento(
        @Path("id") id: Long,
        @Body evento: Event
    ): Response<Event>
    @DELETE("api/eventos/{id}")
    suspend fun eliminarEvento(@Path("id") id: Long): Response<Unit>


    @FormUrlEncoded
    @POST("api/pedido/confirmar")
    suspend fun confirmarPedido(
        @Field("total") total: Double,
        @Field("idEvento") idEvento: Long
    ): ResponseBody

    object RetrofitClient {

        // Para emulador de Android Studio
        private const val BASE_URL = "http://10.0.2.2:9090/"

        private var retrofit: Retrofit? = null
        private lateinit var appContext: Context

        fun init(context: Context) {
            appContext = context.applicationContext
        }

        val instance: ApiService
            get() {
                if (retrofit == null) {
                    // Logging interceptor para debug
                    val loggingInterceptor = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }

                    // Auth interceptor
                    val authInterceptor = AuthInterceptor(appContext)

                    // OkHttpClient
                    val client = OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(authInterceptor)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build()

                    retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
                return retrofit!!.create(ApiService::class.java)
            }
    }
}
