package cvb.com.br.petshop.db.dao.util

import cvb.com.br.petshop.db.entity.EntityItemPurchase

object EntityItemPurchaseFactory {

    fun createItemPurchase(purchaseId: Long, productId: Long) = EntityItemPurchase(
        purchaseId = purchaseId,
        prodId = productId,
        prodDesc = "Produto-Teste",
        prodPrice = 15.35,
        prodUrl = "https://petshop/imagem_url_teste",
        quantity = 3
    )
}