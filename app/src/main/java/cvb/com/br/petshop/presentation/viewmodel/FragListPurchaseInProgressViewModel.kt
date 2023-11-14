package cvb.com.br.petshop.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvb.com.br.petshop.R
import cvb.com.br.petshop.di.DefaultDispatcher
import cvb.com.br.petshop.di.MainDispatcher
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.domain.usecase.ItemPurchaseUseCase
import cvb.com.br.petshop.domain.usecase.ProductUseCase
import cvb.com.br.petshop.domain.usecase.PurchaseUseCase
import cvb.com.br.petshop.presentation.viewmodel.status.UIStatus
import cvb.com.br.petshop.util.PurchaseUtil
import cvb.com.br.petshop.util.StringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragListPurchaseInProgressViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    @DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val productUseCase: ProductUseCase,
    private val purchaseUseCase: PurchaseUseCase,
    private val itemPurchaseUseCase: ItemPurchaseUseCase,
    private val purchaseUtil: PurchaseUtil
) : ViewModel() {

    companion object {
        private const val C_CRUD_EVENT_ITEM_DELETE = 0
        private const val C_CRUD_EVENT_ITEM_UPDATE = 1
        private const val C_CRUD_EVENT_PURCHASE_UPDATE = 2
    }

    private var crudEventType: Int = 0
    private lateinit var crudItemPurchase: ItemPurchase

    private var listProduct: List<Product>? = null

    private var purchaseInProgress: Purchase? = null
    private var currentListItemPurchase: List<ItemPurchase>? = null

    private val mLoadPurchaseStatus = MutableLiveData<UIStatus<List<Purchase?>>>()
    val loadPurchaseStatus: LiveData<UIStatus<List<Purchase?>>>
        get() = mLoadPurchaseStatus

    private val mUpdateItemPurchaseStatus = MutableLiveData<UIStatus<Nothing>>()
    val updateItemPurchaseStatus: LiveData<UIStatus<Nothing>>
        get() = mUpdateItemPurchaseStatus

    private val mDeleteItemPurchaseStatus = MutableLiveData<UIStatus<Nothing>>()
    val deleteItemPurchaseStatus: LiveData<UIStatus<Nothing>>
        get() = mDeleteItemPurchaseStatus

    private val mFinishPurchaseStatus = MutableLiveData<UIStatus<Nothing>>()
    val finishPurchaseStatus: LiveData<UIStatus<Nothing>>
        get() = mFinishPurchaseStatus

    private val mUpdateTotalPurchase = MutableLiveData<String>()
    val updateTotalPurchase: LiveData<String>
        get() = mUpdateTotalPurchase

    fun loadPurchase() {
        viewModelScope.launch {
            try {
                mLoadPurchaseStatus.value = UIStatus.Loading

                listProduct = productUseCase.getLocalProducts()

                val listPurchase = getListPurchase()

                getTotalPurchase()

                mLoadPurchaseStatus.value = UIStatus.Success(listPurchase)
            } catch (error: Throwable) {
                mLoadPurchaseStatus.value = UIStatus.Error(error)
            }
        }
    }

    private suspend fun getListPurchase(): List<Purchase> {
        val listPurchase = mutableListOf<Purchase>()

        purchaseInProgress = purchaseUseCase.getPurchaseInProgress()

        purchaseInProgress?.let { purchase ->
            val listItemPurchase = itemPurchaseUseCase.getItemsByPurchaseId(purchase.id)
            purchase.listItemPurchase = listItemPurchase

            currentListItemPurchase = listItemPurchase

            listPurchase.add(purchase)
        }

        return listPurchase
    }

    fun findProductById(productId: Long): Product? {
        return listProduct?.let { list ->
            list.firstOrNull { product -> product.id == productId  }
        } ?: run {
            null
        }
    }

    fun finishPurchase() {
        viewModelScope.launch {
            try {
                mFinishPurchaseStatus.value = UIStatus.Loading

                crudEventType = C_CRUD_EVENT_PURCHASE_UPDATE

                purchaseInProgress?.let { purchase ->

                    val finishedPurchase = purchaseUseCase.finishPurchase(purchase)
                    purchaseInProgress = finishedPurchase

                    mFinishPurchaseStatus.value = UIStatus.Success(null)
                } ?: run {
                    val msg = context.getString(R.string.frag_purchase_in_progress_invalid_purchase)
                    mFinishPurchaseStatus.value = UIStatus.Error(Exception(msg))
                }

            } catch (error: Throwable) {
                mFinishPurchaseStatus.value = UIStatus.Error(error)
            }
        }
    }

    fun updateItemPurchase(itemPurchase: ItemPurchase) {
        viewModelScope.launch {
            try {
                mUpdateItemPurchaseStatus.value = UIStatus.Loading

                handleUpdateOrDeleteItemPurchase(itemPurchase)

                mUpdateItemPurchaseStatus.value = UIStatus.Success(null)
            } catch (error: Throwable) {
                mUpdateItemPurchaseStatus.value = UIStatus.Error(error)
            }
        }
    }

    fun deleteItemPurchase(itemPurchase: ItemPurchase) {
        viewModelScope.launch {
            try {
                mDeleteItemPurchaseStatus.value = UIStatus.Loading

                handleDeleteItemPurchase(itemPurchase)

                mDeleteItemPurchaseStatus.value = UIStatus.Success(null)
            } catch (error: Throwable) {
                mDeleteItemPurchaseStatus.value = UIStatus.Error(error)
            }
        }
    }

    private suspend fun handleUpdateOrDeleteItemPurchase(itemPurchaseUpdate: ItemPurchase) {
        crudEventType = C_CRUD_EVENT_ITEM_UPDATE
        crudItemPurchase = itemPurchaseUpdate

        itemPurchaseUseCase.updateOrDeleteItemBasedOnQuantity(itemPurchaseUpdate)

        val id = itemPurchaseUpdate.purchaseId
        currentListItemPurchase = itemPurchaseUseCase.getItemsByPurchaseId(id)

        getTotalPurchase()
    }

    private suspend fun handleDeleteItemPurchase(itemPurchaseDelete: ItemPurchase) {
        crudEventType = C_CRUD_EVENT_ITEM_DELETE
        crudItemPurchase = itemPurchaseDelete

        itemPurchaseUseCase.delete(itemPurchaseDelete)

        val id = itemPurchaseDelete.purchaseId
        currentListItemPurchase = itemPurchaseUseCase.getItemsByPurchaseId(id)

        getTotalPurchase()
    }

    private fun getTotalPurchase() {
        val total = purchaseUtil.getTotalPurchase(currentListItemPurchase)
        mUpdateTotalPurchase.value = StringUtil.formatValue(total)
    }

    fun execRetryEvent() {
        when (crudEventType) {
            C_CRUD_EVENT_ITEM_DELETE -> deleteItemPurchase(crudItemPurchase)
            C_CRUD_EVENT_ITEM_UPDATE -> updateItemPurchase(crudItemPurchase)
            else -> finishPurchase()
        }
    }

    fun getCurrentListItemPurchase() = currentListItemPurchase

    fun getPurchaseInformation(): String {
        purchaseInProgress?.let { purchase ->
            currentListItemPurchase?.let { listItemPurchase ->
                purchase.listItemPurchase = listItemPurchase
                return purchaseUtil.getPurchaseFormatted(purchase)
            }
        }

        return "-"
    }
}