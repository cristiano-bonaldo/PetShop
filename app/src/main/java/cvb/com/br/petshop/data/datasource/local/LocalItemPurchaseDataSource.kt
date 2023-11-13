package cvb.com.br.petshop.data.datasource.local

import cvb.com.br.petshop.data.db.dao.ItemPurchaseDao
import cvb.com.br.petshop.domain.datasource.ItemPurchaseDataSource
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.util.extension.ItemPurchaseConverter.toEntityItemPurchase
import cvb.com.br.petshop.util.extension.ItemPurchaseConverter.toItemPurchase
import javax.inject.Inject

class LocalItemPurchaseDataSource @Inject constructor(private val itemPurchaseDao: ItemPurchaseDao) :
    ItemPurchaseDataSource {

    override suspend fun insert(itemPurchase: ItemPurchase) {
        val entityItemPurchase = itemPurchase.toEntityItemPurchase()
        itemPurchaseDao.insert(entityItemPurchase)
    }

    override suspend fun delete(itemPurchase: ItemPurchase) {
        val entityItemPurchase = itemPurchase.toEntityItemPurchase()
        itemPurchaseDao.delete(entityItemPurchase)
    }

    override suspend fun update(itemPurchase: ItemPurchase) {
        val entityItemPurchase = itemPurchase.toEntityItemPurchase()
        itemPurchaseDao.update(entityItemPurchase)
    }

    override suspend fun getItemsByPurchase(purchaseId: Long): List<ItemPurchase> {
        val listEntityItemPurchase = itemPurchaseDao.getItemsByPurchase(purchaseId)
        return listEntityItemPurchase.map { entityItemPurchase -> entityItemPurchase.toItemPurchase() }
    }

    override suspend fun getItemByPurchaseAndProduct(purchaseId: Long, prodId: Long): ItemPurchase? {
        val entityItemPurchase = itemPurchaseDao.getItemByPurchaseAndProduct(purchaseId, prodId)
        return entityItemPurchase?.toItemPurchase()
    }
}