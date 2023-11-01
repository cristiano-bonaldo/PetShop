package cvb.com.br.petshop.data.db.dao.util

import cvb.com.br.petshop.domain.enum.PurchaseStatusEnum
import cvb.com.br.petshop.data.db.entity.EntityPurchase

object EntityPurchaseFactory {

    fun createPurchaseWithStatusOpen(id: Long) = EntityPurchase(
        id = id,
        status = PurchaseStatusEnum.PURCHASE_STATUS_OPEN.status,
        createdAt = 1698002831000,
        convertedAt = 0
    )

    fun createPurchaseWithStatusClosed(id: Long) = EntityPurchase(
        id = id,
        status = PurchaseStatusEnum.PURCHASE_STATUS_CLOSED.status,
        createdAt = 1698002831000,
        convertedAt = 1698003031000
    )




}