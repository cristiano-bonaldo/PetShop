package cvb.com.br.petshop.viewmodel.status

import cvb.com.br.petshop.data.model.Purchase

sealed class LoadPurchaseStatus {
    data object Loading : LoadPurchaseStatus()
    class Error(val error: Throwable) : LoadPurchaseStatus()
    class Success(val listPurchase: List<Purchase?>) : LoadPurchaseStatus()
}
