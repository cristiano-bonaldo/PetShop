package cvb.com.br.petshop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import cvb.com.br.petshop.data.model.Product
import cvb.com.br.petshop.data.repository.ProductRepository
import cvb.com.br.petshop.di.MainDispatcher
import cvb.com.br.petshop.di.ProductLocalRepository
import cvb.com.br.petshop.di.ProductRemoteRepository
import cvb.com.br.petshop.viewmodel.status.LoadProductStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragListProductViewModel @Inject constructor(
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    @ProductRemoteRepository private val remoteProductRepository: ProductRepository,
    @ProductLocalRepository private val localProductRepository: ProductRepository
) : ViewModel() {

    private val mLoadProductStatus = MutableLiveData<LoadProductStatus>()
    val loadProductStatus: LiveData<LoadProductStatus>
        get() = mLoadProductStatus

    private val mIsLoading = MediatorLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = mIsLoading.distinctUntilChanged()

    init {
        mIsLoading.addSource(mLoadProductStatus) {
            updateMediatorIsLoading()
        }
    }

    fun loadProducts() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                mLoadProductStatus.value = LoadProductStatus.Loading

                val productList = getProducts()

                mLoadProductStatus.value = LoadProductStatus.Success(productList)
            } catch (error: Throwable) {
                mLoadProductStatus.value = LoadProductStatus.Error(error)
            }
        }
    }

    private suspend fun getProducts(): List<Product> {
        var productList = localProductRepository.getAll()

        if (productList.isEmpty()) {
            productList = remoteProductRepository.getAll()

            productList.forEach { product -> localProductRepository.insert(product) }
        }

        return localProductRepository.getAll()
    }

    private fun updateMediatorIsLoading() {
        mIsLoading.value = isLoading()
    }

    private fun isLoading(): Boolean {
        return mLoadProductStatus.value is LoadProductStatus.Loading
    }
}
