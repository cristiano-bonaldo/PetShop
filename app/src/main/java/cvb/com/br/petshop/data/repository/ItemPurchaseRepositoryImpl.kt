package cvb.com.br.petshop.data.repository

import cvb.com.br.petshop.data.datasource.local.LocalItemPurchaseDataSource
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.repository.ItemPurchaseRepository
import javax.inject.Inject

class ItemPurchaseRepositoryImpl @Inject constructor(private val localItemPurchaseDataSource: LocalItemPurchaseDataSource) :
    ItemPurchaseRepository {

    override suspend fun insert(itemPurchase: ItemPurchase) {
        localItemPurchaseDataSource.insert(itemPurchase)
    }

    override suspend fun delete(itemPurchase: ItemPurchase) {
        localItemPurchaseDataSource.delete(itemPurchase)
    }
    override suspend fun update(itemPurchase: ItemPurchase) {
        localItemPurchaseDataSource.update(itemPurchase)
    }

    override suspend fun getItemsByPurchase(purchaseId: Long): List<ItemPurchase> {
        return localItemPurchaseDataSource.getItemsByPurchase(purchaseId)
    }

    override suspend fun getItemByPurchaseAndProduct(purchaseId: Long, prodId: Long): ItemPurchase? {
        return localItemPurchaseDataSource.getItemByPurchaseAndProduct(purchaseId, prodId)
    }
}