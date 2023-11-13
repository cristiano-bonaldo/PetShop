package cvb.com.br.petshop.data.repository.fake

import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.domain.repository.ItemPurchaseRepository
import cvb.com.br.petshop.domain.repository.PurchaseRepository

class ItemPurchaseRepositoryFake : ItemPurchaseRepository {
    private var isError: Boolean = false

    fun enableError() {
        isError = true
    }

    override suspend fun insert(itemPurchase: ItemPurchase) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
    }

    override suspend fun delete(itemPurchase: ItemPurchase) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
    }

    override suspend fun update(itemPurchase: ItemPurchase) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
    }

    override suspend fun getItemsByPurchase(purchaseId: Long): List<ItemPurchase> {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
        return listOf()
    }

    override suspend fun getItemByPurchaseAndProduct(
        purchaseId: Long,
        prodId: Long
    ): ItemPurchase? {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
        return null
    }
}