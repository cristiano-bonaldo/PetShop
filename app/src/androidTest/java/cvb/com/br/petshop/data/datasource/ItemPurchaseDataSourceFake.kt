package cvb.com.br.petshop.data.datasource

import cvb.com.br.petshop.data.model.ItemPurchase
import cvb.com.br.petshop.data.model.Purchase
import cvb.com.br.petshop.data.repository.ItemPurchaseRepository
import cvb.com.br.petshop.data.repository.PurchaseRepository

class ItemPurchaseDataSourceFake : ItemPurchaseRepository {

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

    override suspend fun getByPurchase(purchaseId: Long): List<ItemPurchase> {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
        return listOf()
    }

    override suspend fun getByPurchaseAndProduct(purchaseId: Long, prodId: Long): ItemPurchase? {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }
        return null
    }
}