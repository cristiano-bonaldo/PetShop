package cvb.com.br.petshop.network.util

import retrofit2.Response

object ApiHandleDataResult {

    suspend fun <T : Any> handleData(execute: suspend () -> Response<T>): T {
        val response = execute()

        val body = response.body()

        if (response.isSuccessful && body != null) {
            return body
        } else {
            throw Throwable("handleData::Error Code: ${response.code()} - ${response.message()}")
        }
    }
}