package cvb.com.br.petshop.network.service

import cvb.com.br.petshop.network.model.ApiProductResult
import retrofit2.Response
import retrofit2.http.GET

interface PetShopService {

    @GET("v3/")
    suspend fun getProducts(): Response<ApiProductResult>

}