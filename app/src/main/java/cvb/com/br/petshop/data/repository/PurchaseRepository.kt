package cvb.com.br.petshop.data.repository

import cvb.com.br.petshop.data.model.Purchase

interface PurchaseRepository {
    suspend fun insert(purchase: Purchase)

    suspend fun update(purchase: Purchase)

    suspend fun getPurchaseInProgress(): Purchase?
}