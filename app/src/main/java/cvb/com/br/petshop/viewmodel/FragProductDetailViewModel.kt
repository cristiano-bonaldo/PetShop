package cvb.com.br.petshop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvb.com.br.petshop.data.enum.PurchaseStatusEnum
import cvb.com.br.petshop.data.model.ItemPurchase
import cvb.com.br.petshop.data.model.Product
import cvb.com.br.petshop.data.model.Purchase
import cvb.com.br.petshop.data.repository.ItemPurchaseRepository
import cvb.com.br.petshop.data.repository.PurchaseRepository
import cvb.com.br.petshop.di.ItemPurchaseLocalRepository
import cvb.com.br.petshop.di.MainDispatcher
import cvb.com.br.petshop.di.PurchaseLocalRepository
import cvb.com.br.petshop.viewmodel.status.CrudStatus
import cvb.com.br.petshop.viewmodel.status.LoadItemPurchaseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragProductDetailViewModel @Inject constructor(
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    @PurchaseLocalRepository private val localPurchaseRepository: PurchaseRepository,
    @ItemPurchaseLocalRepository private val localItemPurchaseRepository: ItemPurchaseRepository,
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

                purchaseInProgress = localPurchaseRepository.getPurchaseInProgress()

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
            itemPurchase = localItemPurchaseRepository.getByPurchaseAndProduct(purchase.id, productId)
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
            val purchase = createPurchase()
            localPurchaseRepository.insert(purchase)
            purchaseInProgress = localPurchaseRepository.getPurchaseInProgress()
        }

        purchaseInProgress?.let { purchase ->
            currentItemPurchase?.let { itemPurchase ->
                val itemPurchaseUpdate = itemPurchase.copy(quantity = requestedQuantity)
                localItemPurchaseRepository.update(itemPurchaseUpdate)
            } ?: run {
                val itemPurchase = createItemPurchase(purchase.id, product, requestedQuantity)
                localItemPurchaseRepository.insert(itemPurchase)
            }
        }
    }

    fun getTotalPurchase(product: Product, qtd: Int) {
        mTotalPurchase.value = (qtd * product.amount)
    }

    private fun createPurchase() = Purchase(
        id = 0,
        status = PurchaseStatusEnum.PURCHASE_STATUS_OPEN.status,
        createAt = System.currentTimeMillis(),
        convertedAt = 0)

    private fun createItemPurchase(purchaseId: Long, product: Product, requestedQuantity: Int) = ItemPurchase(
        purchaseId = purchaseId,
        prodId = product.id,
        prodDesc = product.description,
        prodPrice = product.amount,
        prodUrl = product.imageUrl,
        quantity = requestedQuantity)

    fun execRetryEvent() = addItemPurchase(prodForRetry, qtdForRetry)
}