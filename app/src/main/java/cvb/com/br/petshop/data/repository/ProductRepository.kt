package cvb.com.br.petshop.data.repository

import cvb.com.br.petshop.data.model.Product

interface ProductRepository {
    suspend fun insert(product: Product)

    suspend fun getAll(): List<Product>
}