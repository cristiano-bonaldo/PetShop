package cvb.com.br.petshop.viewmodel.status

sealed class CrudStatus {
    data object Loading : CrudStatus()
    class Error(val error: Throwable) : CrudStatus()
    data object Success : CrudStatus()
}
