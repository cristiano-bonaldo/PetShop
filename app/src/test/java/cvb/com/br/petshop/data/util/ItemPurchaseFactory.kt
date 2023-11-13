package cvb.com.br.petshop.data.util

import cvb.com.br.petshop.domain.model.ItemPurchase

object ItemPurchaseFactory {

    fun createItemPurchase(purchaseId: Long, productId: Long, quantity: Int = 1) = ItemPurchase(
        purchaseId = purchaseId,
        prodId = productId,
        prodDesc = "Produto-Teste",
        prodPrice = 15.35,
        prodUrl = "https://petshop/imagem_url_teste",
        quantity = quantity
    )
}