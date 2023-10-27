package cvb.com.br.petshop.data.datasource.remote

import cvb.com.br.petshop.data.model.Product
import cvb.com.br.petshop.data.repository.ProductRepository
import cvb.com.br.petshop.network.service.ApiService
import cvb.com.br.petshop.network.util.ApiHandleDataResult
import cvb.com.br.petshop.util.extension.ProductConverter.toProduct
import javax.inject.Inject

class RemoteProductDataSource @Inject constructor(private val apiService: ApiService) : ProductRepository {

    override suspend fun insert(product: Product) { }

    override suspend fun getAll(): List<Product> {
        /*
        Finalidade de Teste
        No inicio da implmentação, o servidor estava apresentando 2 problemas:
        1º) Retornava um JSON com problema.
        2º) Estava muito instável. Muitas requisiões estavam falhando

        Para não bloquear o desenvolvimento, implementei uma base de dados Fake
        ------------
        val apiProductResult =
            try {
                ApiHandleDataResult.handleData { apiService.getProducts() }
            } catch (malformedJsonException: com.google.gson.stream.MalformedJsonException) {
                Log.i(Constants.TAG, "RemoteProductDataSource::getAll() Erro: Problema na formatação do JSON")
                FakeApiProductResult.apiProductResult
            } catch (exception: Exception) {
                Log.i(Constants.TAG, "RemoteProductDataSource::getAll() Erro: Problema genérico")
                FakeApiProductResult.apiProductResult
            }

        return apiProductResult.productList.map { apiProduct -> apiProduct.toProduct() }
        */

        val apiProductResult = ApiHandleDataResult.handleData { apiService.getProducts() }
        return apiProductResult.productList.map { apiProduct -> apiProduct.toProduct() }
    }
}