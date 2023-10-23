package cvb.com.br.petshop.viewmodel.status

import cvb.com.br.petshop.data.model.ItemPurchase

sealed class LoadItemPurchaseStatus {
    data object Loading : LoadItemPurchaseStatus()
    class Error(val error: Throwable) : LoadItemPurchaseStatus()
    class Success(val itemPurchase: ItemPurchase?) : LoadItemPurchaseStatus()
}
