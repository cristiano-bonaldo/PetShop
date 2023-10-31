package cvb.com.br.petshop.domain.usecase

import cvb.com.br.petshop.di.ProductRepositoryImplementation
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.domain.repository.ProductRepository
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    @ProductRepositoryImplementation private val productRepository: ProductRepository
) {

    suspend fun loadProducts(): List<Product> {
        return productRepository.loadProducts()
    }

    suspend fun getLocalProducts(): List<Product> {
        return productRepository.getProducts()
    }
}

