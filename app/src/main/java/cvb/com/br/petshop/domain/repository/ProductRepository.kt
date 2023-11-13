package cvb.com.br.petshop.domain.repository

import cvb.com.br.petshop.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>

    suspend fun loadProducts(): List<Product>
}