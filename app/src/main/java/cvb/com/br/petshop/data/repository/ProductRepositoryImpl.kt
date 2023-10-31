package cvb.com.br.petshop.data.repository

import cvb.com.br.petshop.data.datasource.local.LocalProductDataSource
import cvb.com.br.petshop.data.datasource.remote.RemoteProductDataSource
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteProductDataSource: RemoteProductDataSource,
    private val localProductDataSource: LocalProductDataSource
) : ProductRepository {

    override suspend fun insert(product: Product) {}

    override suspend fun getProducts(): List<Product> {
        return remoteProductDataSource.getAll()
    }

    override suspend fun loadProducts(): List<Product> {
        var productList = localProductDataSource.getAll()

        if (productList.isEmpty()) {
            productList = remoteProductDataSource.getAll()

            productList.forEach { product -> localProductDataSource.insert(product) }
        }

        return localProductDataSource.getAll()
    }
}