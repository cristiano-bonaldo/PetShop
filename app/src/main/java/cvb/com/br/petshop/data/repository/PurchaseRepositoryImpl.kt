package cvb.com.br.petshop.data.repository

import cvb.com.br.petshop.domain.datasource.PurchaseDataSource
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.domain.repository.PurchaseRepository
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(private val purchaseDataSource: PurchaseDataSource) :
    PurchaseRepository {
    override suspend fun insert(purchase: Purchase) {
        purchaseDataSource.insert(purchase)
    }

    override suspend fun update(purchase: Purchase) {
        purchaseDataSource.update(purchase)
    }

    override suspend fun getPurchaseInProgress(): Purchase? {
        return purchaseDataSource.getPurchaseInProgress()
    }
}