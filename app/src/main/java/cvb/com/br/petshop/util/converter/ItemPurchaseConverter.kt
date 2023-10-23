package cvb.com.br.petshop.util.converter

import cvb.com.br.petshop.data.model.ItemPurchase
import cvb.com.br.petshop.db.entity.EntityItemPurchase

object ItemPurchaseConverter {

    fun EntityItemPurchase.toItemPurchase(): ItemPurchase {
        return ItemPurchase(
            this.purchaseId,
            this.prodId,
            this.prodDesc,
            this.prodPrice,
            this.prodUrl,
            this.quantity
        )
    }

    fun ItemPurchase.toEntityItemPurchase(): EntityItemPurchase {
        return EntityItemPurchase(
            this.purchaseId,
            this.prodId,
            this.prodDesc,
            this.prodPrice,
            this.prodUrl,
            this.quantity
        )
    }
}