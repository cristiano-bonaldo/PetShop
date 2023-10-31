package cvb.com.br.petshop.data.network.interceptor

import cvb.com.br.petshop.util.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url

        val newUrl = originalHttpUrl.newBuilder()
            .addPathSegment(Constants.API_KEY)
            .build()

        /*
        Exemplo com Query Parameter
        --
        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("example-key", "example-key-value")
            .build()
        */

        val requestBuilder = original.newBuilder().url(newUrl)

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}