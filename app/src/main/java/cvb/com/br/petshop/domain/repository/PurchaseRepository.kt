package cvb.com.br.petshop.domain.repository

import cvb.com.br.petshop.domain.model.Purchase

interface PurchaseRepository {
    suspend fun insert(purchase: Purchase)

    suspend fun update(purchase: Purchase)

    suspend fun getPurchaseInProgress(): Purchase?
}