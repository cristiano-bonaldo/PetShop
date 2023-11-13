package cvb.com.br.petshop.data.datasource.local

import cvb.com.br.petshop.data.db.dao.PurchaseDao
import cvb.com.br.petshop.domain.datasource.PurchaseDataSource
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.util.extension.PurchaseConverter.toEntityPurchase
import cvb.com.br.petshop.util.extension.PurchaseConverter.toPurchase
import javax.inject.Inject

class LocalPurchaseDataSource @Inject constructor(private val purchaseDao: PurchaseDao) :
    PurchaseDataSource {

    override suspend fun insert(purchase: Purchase) {
        val entityPurchase = purchase.toEntityPurchase()
        purchaseDao.insert(entityPurchase)
    }

    override suspend fun update(purchase: Purchase) {
        val entityPurchase = purchase.toEntityPurchase()
        purchaseDao.update(entityPurchase)
    }

    override suspend fun getPurchaseInProgress(): Purchase? {
        val entityPurchase = purchaseDao.getPurchaseInProgress()
        return entityPurchase?.toPurchase()
    }
}