package cvb.com.br.petshop.data.repository

import cvb.com.br.petshop.domain.datasource.ItemPurchaseDataSource
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.repository.ItemPurchaseRepository
import javax.inject.Inject

class ItemPurchaseRepositoryImpl @Inject constructor(private val itemPurchaseDataSource: ItemPurchaseDataSource) :
    ItemPurchaseRepository {

    override suspend fun insert(itemPurchase: ItemPurchase) {
        itemPurchaseDataSource.insert(itemPurchase)
    }

    override suspend fun delete(itemPurchase: ItemPurchase) {
        itemPurchaseDataSource.delete(itemPurchase)
    }

    override suspend fun update(itemPurchase: ItemPurchase) {
        itemPurchaseDataSource.update(itemPurchase)
    }

    override suspend fun getItemsByPurchase(purchaseId: Long): List<ItemPurchase> {
        return itemPurchaseDataSource.getItemsByPurchase(purchaseId)
    }

    override suspend fun getItemByPurchaseAndProduct(
        purchaseId: Long,
        prodId: Long
    ): ItemPurchase? {
        return itemPurchaseDataSource.getItemByPurchaseAndProduct(purchaseId, prodId)
    }
}