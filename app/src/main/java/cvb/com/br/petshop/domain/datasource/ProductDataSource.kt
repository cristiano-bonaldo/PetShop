package cvb.com.br.petshop.domain.datasource

import cvb.com.br.petshop.domain.model.Product

interface ProductDataSource {

    suspend fun getAll(): List<Product>

    suspend fun insert(product: Product)

}