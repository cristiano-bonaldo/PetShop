package cvb.com.br.petshop.data.datasource.local

import cvb.com.br.petshop.data.model.ItemPurchase
import cvb.com.br.petshop.data.repository.ItemPurchaseRepository
import cvb.com.br.petshop.db.dao.ItemPurchaseDao
import cvb.com.br.petshop.util.extension.ItemPurchaseConverter.toEntityItemPurchase
import cvb.com.br.petshop.util.extension.ItemPurchaseConverter.toItemPurchase
import javax.inject.Inject

class LocalItemPurchaseDataSource @Inject constructor(private val itemPurchaseDao: ItemPurchaseDao) :
    ItemPurchaseRepository {

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

    override suspend fun getByPurchase(purchaseId: Long): List<ItemPurchase> {
        val listEntityItemPurchase = itemPurchaseDao.getByPurchase(purchaseId)
        return listEntityItemPurchase.map { entityItemPurchase -> entityItemPurchase.toItemPurchase() }
    }

    override suspend fun getByPurchaseAndProduct(purchaseId: Long, prodId: Long): ItemPurchase? {
        val entityItemPurchase = itemPurchaseDao.getByPurchaseAndProduct(purchaseId, prodId)
        return entityItemPurchase?.toItemPurchase()
    }
}