package cvb.com.br.petshop.data.datasource

import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.domain.repository.PurchaseRepository

class PurchaseDataSourceFake : PurchaseRepository {

    private var isError: Boolean = false

    fun enableError() {
        isError = true
    }

    override suspend fun insert(purchase: Purchase) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
    }

    override suspend fun update(purchase: Purchase) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
    }

    override suspend fun getPurchaseInProgress(): Purchase? {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
        return null
    }
}