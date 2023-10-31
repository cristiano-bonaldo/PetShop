package cvb.com.br.petshop.domain.usecase

import cvb.com.br.petshop.di.ItemPurchaseRepositoryImplementation
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.domain.repository.ItemPurchaseRepository
import javax.inject.Inject

class ItemPurchaseUseCase @Inject constructor(
    @ItemPurchaseRepositoryImplementation private val itemPurchaseRepository: ItemPurchaseRepository
) {

    suspend fun getItemsByPurchaseId(purchaseId: Long): List<ItemPurchase> {
        return itemPurchaseRepository.getItemsByPurchase(purchaseId)
    }

    suspend fun updateOrDeleteItemBasedOnQuantity(itemPurchase: ItemPurchase) {
        val isDelete = itemPurchase.quantity == 0

        if (isDelete) {
            itemPurchaseRepository.delete(itemPurchase)
        } else {
            itemPurchaseRepository.update(itemPurchase)
        }
    }

    suspend fun delete(itemPurchase: ItemPurchase) {
        itemPurchaseRepository.delete(itemPurchase)
    }

    suspend fun updateQuantity(itemPurchase: ItemPurchase, requestedQuantity: Int) {
        val itemPurchaseUpdate = itemPurchase.copy(quantity = requestedQuantity)
        itemPurchaseRepository.update(itemPurchaseUpdate)
    }

    suspend fun insert(purchaseId: Long, product: Product, requestedQuantity: Int) {
        val itemPurchase = createItemPurchase(purchaseId, product, requestedQuantity)
        itemPurchaseRepository.insert(itemPurchase)
    }

    suspend fun getItemByPurchaseAndProduct(purchaseId: Long, prodId: Long): ItemPurchase? {
        return itemPurchaseRepository.getItemByPurchaseAndProduct(purchaseId, prodId)
    }

    private fun createItemPurchase(purchaseId: Long, product: Product, requestedQuantity: Int) =
        ItemPurchase(
            purchaseId = purchaseId,
            prodId = product.id,
            prodDesc = product.description,
            prodPrice = product.amount,
            prodUrl = product.imageUrl,
            quantity = requestedQuantity
        )
}

