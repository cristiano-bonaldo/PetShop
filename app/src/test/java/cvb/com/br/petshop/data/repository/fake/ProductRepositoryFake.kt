package cvb.com.br.petshop.data.repository.fake

import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.domain.repository.ProductRepository

class ProductRepositoryFake : ProductRepository {
    private var isError: Boolean = false

    fun enableError() {
        isError = true
    }

    override suspend fun getProducts(): List<Product> {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
        return listOf()
    }

    override suspend fun loadProducts(): List<Product> {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
        return listOf()
    }
}