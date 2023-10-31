package cvb.com.br.petshop.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvb.com.br.petshop.di.MainDispatcher
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.domain.usecase.ItemPurchaseUseCase
import cvb.com.br.petshop.domain.usecase.PurchaseUseCase
import cvb.com.br.petshop.presentation.viewmodel.status.CrudStatus
import cvb.com.br.petshop.presentation.viewmodel.status.LoadItemPurchaseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragProductDetailViewModel @Inject constructor(
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val purchaseUseCase: PurchaseUseCase,
    private val itemPurchaseUseCase: ItemPurchaseUseCase,
) : ViewModel() {

    private var purchaseInProgress: Purchase? = null

    private var currentItemPurchase: ItemPurchase? = null

    private lateinit var prodForRetry: Product
    private var qtdForRetry: Int = 0

    private val mLoadItemPurchaseStatus = MutableLiveData<LoadItemPurchaseStatus>()
    val loadItemPurchaseStatus: LiveData<LoadItemPurchaseStatus>
        get() = mLoadItemPurchaseStatus

    private val mAddPurchaseStatus = MutableLiveData<CrudStatus>()
    val addPurchaseStatus: LiveData<CrudStatus>
        get() = mAddPurchaseStatus

    private val mTotalPurchase = MutableLiveData<Double>()
    val totalPurchase: LiveData<Double>
        get() = mTotalPurchase


    fun loadItemPurchase(product: Product) {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                mLoadItemPurchaseStatus.value = LoadItemPurchaseStatus.Loading

                purchaseInProgress = purchaseUseCase.getPurchaseInProgress()

                currentItemPurchase = getItemPurchase(product.id)

                mLoadItemPurchaseStatus.value = LoadItemPurchaseStatus.Success(currentItemPurchase)
            } catch (error: Throwable) {
                mLoadItemPurchaseStatus.value = LoadItemPurchaseStatus.Error(error)
            }
        }
    }

    private suspend fun getItemPurchase(productId: Long): ItemPurchase? {
        var itemPurchase: ItemPurchase? = null
        purchaseInProgress?.let { purchase ->
            itemPurchase = itemPurchaseUseCase.getItemByPurchaseAndProduct(purchase.id, productId)
        }

        return itemPurchase
    }

    fun addItemPurchase(product: Product, quantityRequested: Int) {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                mAddPurchaseStatus.value = CrudStatus.Loading

                handlePurchaseProcess(product, quantityRequested)

                mAddPurchaseStatus.value = CrudStatus.Success
            } catch (error: Throwable) {
                mAddPurchaseStatus.value = CrudStatus.Error(error)
            }
        }
    }

    private suspend fun handlePurchaseProcess(product: Product, requestedQuantity: Int) {
        prodForRetry = product
        qtdForRetry = requestedQuantity

        if (purchaseInProgress == null) {
            purchaseInProgress = purchaseUseCase.openPurchase()
        }

        purchaseInProgress?.let { purchase ->
            currentItemPurchase?.let { itemPurchase ->
                itemPurchaseUseCase.updateQuantity(itemPurchase, requestedQuantity)
            } ?: run {
                itemPurchaseUseCase.insert(purchase.id, product, requestedQuantity)
            }
        }
    }

    fun getTotalPurchase(product: Product, qtd: Int) {
        mTotalPurchase.value = (qtd * product.amount)
    }

    fun execRetryEvent() = addItemPurchase(prodForRetry, qtdForRetry)
}