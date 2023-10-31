package cvb.com.br.petshop.presentation.viewmodel.status

import cvb.com.br.petshop.domain.model.ItemPurchase

sealed class LoadItemPurchaseStatus {
    data object Loading : LoadItemPurchaseStatus()
    class Error(val error: Throwable) : LoadItemPurchaseStatus()
    class Success(val itemPurchase: ItemPurchase?) : LoadItemPurchaseStatus()
}
