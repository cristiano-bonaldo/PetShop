package cvb.com.br.petshop.data.repository

import cvb.com.br.petshop.data.datasource.local.LocalPurchaseDataSource
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.domain.repository.PurchaseRepository
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(private val localPurchaseDataSource: LocalPurchaseDataSource) :
    PurchaseRepository {
    override suspend fun insert(purchase: Purchase) {
        localPurchaseDataSource.insert(purchase)
    }

    override suspend fun update(purchase: Purchase) {
        localPurchaseDataSource.update(purchase)
    }

    override suspend fun getPurchaseInProgress(): Purchase? {
        return localPurchaseDataSource.getPurchaseInProgress()
    }
}