package cvb.com.br.petshop.util.extension

import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.data.db.entity.EntityPurchase

object PurchaseConverter {

    fun EntityPurchase.toPurchase(): Purchase {
        return Purchase(
            this.id,
            this.status,
            this.createdAt,
            this.convertedAt
        )
    }

    fun Purchase.toEntityPurchase(): EntityPurchase {
        return EntityPurchase(
            this.id,
            this.status,
            this.createAt,
            this.convertedAt
        )
    }
}