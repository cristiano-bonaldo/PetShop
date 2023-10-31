package cvb.com.br.petshop.presentation.viewmodel.status

import cvb.com.br.petshop.domain.model.Product

sealed class LoadProductStatus {
    data object Loading : LoadProductStatus()
    class Error(val error: Throwable) : LoadProductStatus()
    class Success(val listProduct: List<Product>) : LoadProductStatus()
}
