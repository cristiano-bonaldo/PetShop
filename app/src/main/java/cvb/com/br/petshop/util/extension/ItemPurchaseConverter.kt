package cvb.com.br.petshop.util.extension

import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.data.db.entity.EntityItemPurchase

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