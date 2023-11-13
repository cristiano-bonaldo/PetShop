package cvb.com.br.petshop.data.util

import cvb.com.br.petshop.domain.enum.PurchaseStatusEnum
import cvb.com.br.petshop.domain.model.Purchase

object PurchaseFactory {

    fun createPurchase(id: Long) = Purchase(
        id = id,
        status = PurchaseStatusEnum.PURCHASE_STATUS_OPEN.status,
        createAt = System.currentTimeMillis(),
        convertedAt = 0
    )
}