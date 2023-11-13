package cvb.com.br.petshop.domain.datasource

import cvb.com.br.petshop.domain.model.Purchase

interface PurchaseDataSource {

    suspend fun insert(purchase: Purchase)

    suspend fun update(purchase: Purchase)

    suspend fun getPurchaseInProgress(): Purchase?

}