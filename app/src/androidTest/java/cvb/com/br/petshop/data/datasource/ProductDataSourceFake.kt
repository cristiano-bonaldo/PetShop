package cvb.com.br.petshop.data.datasource

import cvb.com.br.petshop.data.model.Product
import cvb.com.br.petshop.data.repository.ProductRepository

class ProductDataSourceFake : ProductRepository {

    private var isError: Boolean = false

    fun enableError() {
        isError = true
    }

    override suspend fun insert(product: Product) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
    }

    override suspend fun getAll(): List<Product> {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }

        return listOf()
    }
}