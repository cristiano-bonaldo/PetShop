package cvb.com.br.petshop.data.model.util

import cvb.com.br.petshop.data.model.ItemPurchase
import cvb.com.br.petshop.db.entity.EntityItemPurchase

object ItemPurchaseFactory {

    fun createItemPurchase(purchaseId: Long, productId: Long) = ItemPurchase(
        purchaseId = purchaseId,
        prodId = productId,
        prodDesc = "Produto-Teste",
        prodPrice = 15.35,
        prodUrl = "https://petshop/imagem_url_teste",
        quantity = 3
    )
}