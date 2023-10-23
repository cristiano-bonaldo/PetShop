package cvb.com.br.petshop.data.repository

import cvb.com.br.petshop.data.model.ItemPurchase

interface ItemPurchaseRepository {
    suspend fun insert(itemPurchase: ItemPurchase)

    suspend fun delete(itemPurchase: ItemPurchase)

    suspend fun update(itemPurchase: ItemPurchase)

    suspend fun getByPurchase(purchaseId: Long): List<ItemPurchase>

    suspend fun getByPurchaseAndProduct(purchaseId: Long, prodId: Long): ItemPurchase?
}
