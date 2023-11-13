package cvb.com.br.petshop.data.datasource.local

import cvb.com.br.petshop.data.db.dao.ProductDao
import cvb.com.br.petshop.domain.datasource.ProductDataSource
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.util.extension.ProductConverter.toEntityProduct
import cvb.com.br.petshop.util.extension.ProductConverter.toProduct
import javax.inject.Inject

class LocalProductDataSource @Inject constructor(private val productDao: ProductDao) : ProductDataSource {
    override suspend fun insert(product: Product) {
        val entityProduct = product.toEntityProduct()
        productDao.insert(entityProduct)
    }

    override suspend fun getAll(): List<Product> {
        val listEntityProduct = productDao.getAll()
        return listEntityProduct.map { entityProduct -> entityProduct.toProduct() }
    }
}