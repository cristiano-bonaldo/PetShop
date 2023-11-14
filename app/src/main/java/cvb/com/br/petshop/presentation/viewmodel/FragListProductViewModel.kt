package cvb.com.br.petshop.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import cvb.com.br.petshop.di.DefaultDispatcher
import cvb.com.br.petshop.di.MainDispatcher
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.domain.usecase.ProductUseCase
import cvb.com.br.petshop.presentation.viewmodel.status.UIStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragListProductViewModel @Inject constructor(
    @DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val productUseCase: ProductUseCase
) : ViewModel() {

    private val mLoadProductStatus = MutableLiveData<UIStatus<List<Product>>>()
    val loadProductStatus: LiveData<UIStatus<List<Product>>>
        get() = mLoadProductStatus

    private val mIsLoading = MediatorLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = mIsLoading.distinctUntilChanged()

    init {
        mIsLoading.addSource(mLoadProductStatus) {
            updateMediatorIsLoading()
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            try {
                mLoadProductStatus.value = UIStatus.Loading

                val productList = productUseCase.loadProducts()

                mLoadProductStatus.value = UIStatus.Success(productList)
            } catch (error: Throwable) {
                mLoadProductStatus.value = UIStatus.Error(error)
            }
        }
    }

    private fun updateMediatorIsLoading() {
        mIsLoading.value = isLoading()
    }

    private fun isLoading(): Boolean {
        return mLoadProductStatus.value is UIStatus.Loading
    }
}
