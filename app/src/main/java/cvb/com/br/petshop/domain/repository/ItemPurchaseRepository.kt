package cvb.com.br.petshop.domain.repository

import cvb.com.br.petshop.domain.model.ItemPurchase

interface ItemPurchaseRepository {
    suspend fun insert(itemPurchase: ItemPurchase)

    suspend fun delete(itemPurchase: ItemPurchase)

    suspend fun update(itemPurchase: ItemPurchase)

    suspend fun getItemsByPurchase(purchaseId: Long): List<ItemPurchase>

    suspend fun getItemByPurchaseAndProduct(purchaseId: Long, prodId: Long): ItemPurchase?
}
