package cvb.com.br.petshop.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvb.com.br.petshop.R
import cvb.com.br.petshop.data.enum.PurchaseStatusEnum
import cvb.com.br.petshop.data.model.ItemPurchase
import cvb.com.br.petshop.data.model.Product
import cvb.com.br.petshop.data.model.Purchase
import cvb.com.br.petshop.data.repository.ItemPurchaseRepository
import cvb.com.br.petshop.data.repository.ProductRepository
import cvb.com.br.petshop.data.repository.PurchaseRepository
import cvb.com.br.petshop.di.ItemPurchaseLocalRepository
import cvb.com.br.petshop.di.MainDispatcher
import cvb.com.br.petshop.di.ProductLocalRepository
import cvb.com.br.petshop.di.PurchaseLocalRepository
import cvb.com.br.petshop.util.PurchaseUtil
import cvb.com.br.petshop.util.StringUtil
import cvb.com.br.petshop.viewmodel.status.CrudStatus
import cvb.com.br.petshop.viewmodel.status.LoadPurchaseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragListPurchaseInProgressViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    @ProductLocalRepository private val productRepository: ProductRepository,
    @PurchaseLocalRepository private val localPurchaseRepository: PurchaseRepository,
    @ItemPurchaseLocalRepository private val localItemPurchaseRepository: ItemPurchaseRepository,
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
    private var currentListItemPurchase: MutableList<ItemPurchase>? = null

    private val mLoadPurchaseStatus = MutableLiveData<LoadPurchaseStatus>()
    val loadPurchaseStatus: LiveData<LoadPurchaseStatus>
        get() = mLoadPurchaseStatus

    private val mUpdateItemPurchaseStatus = MutableLiveData<CrudStatus>()
    val updateItemPurchaseStatus: LiveData<CrudStatus>
        get() = mUpdateItemPurchaseStatus

    private val mDeleteItemPurchaseStatus = MutableLiveData<CrudStatus>()
    val deleteItemPurchaseStatus: LiveData<CrudStatus>
        get() = mDeleteItemPurchaseStatus

    private val mFinishPurchaseStatus = MutableLiveData<CrudStatus>()
    val finishPurchaseStatus: LiveData<CrudStatus>
        get() = mFinishPurchaseStatus

    private val mUpdateTotalPurchase = MutableLiveData<String>()
    val updateTotalPurchase: LiveData<String>
        get() = mUpdateTotalPurchase

    fun loadPurchase() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                mLoadPurchaseStatus.value = LoadPurchaseStatus.Loading

                listProduct = productRepository.getAll()

                val listPurchase = getListPurchase()

                getTotalPurchase()

                mLoadPurchaseStatus.value = LoadPurchaseStatus.Success(listPurchase)
            } catch (error: Throwable) {
                mLoadPurchaseStatus.value = LoadPurchaseStatus.Error(error)
            }
        }
    }

    private suspend fun getListPurchase(): List<Purchase> {
        val listPurchase = mutableListOf<Purchase>()

        purchaseInProgress = localPurchaseRepository.getPurchaseInProgress()

        purchaseInProgress?.let { purchase ->
            val listItemPurchase = localItemPurchaseRepository.getByPurchase(purchase.id)
            purchase.listItemPurchase = listItemPurchase

            currentListItemPurchase = listItemPurchase.toMutableList()

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
        viewModelScope.launch(coroutineDispatcher) {
            try {
                mFinishPurchaseStatus.value = CrudStatus.Loading

                crudEventType = C_CRUD_EVENT_PURCHASE_UPDATE

                purchaseInProgress?.let { purchase ->

                    val finishedPurchase = purchase.copy(
                        status = PurchaseStatusEnum.PURCHASE_STATUS_CLOSED.status,
                        convertedAt = System.currentTimeMillis()
                    )

                    localPurchaseRepository.update(finishedPurchase)
                    purchaseInProgress = finishedPurchase

                    mFinishPurchaseStatus.value = CrudStatus.Success
                } ?: run {
                    val msg = context.getString(R.string.frag_purchase_in_progress_invalid_purchase)
                    mFinishPurchaseStatus.value = CrudStatus.Error(Exception(msg))
                }

            } catch (error: Throwable) {
                mFinishPurchaseStatus.value = CrudStatus.Error(error)
            }
        }
    }

    fun updateItemPurchase(itemPurchase: ItemPurchase) {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                mUpdateItemPurchaseStatus.value = CrudStatus.Loading

                handleUpdateOrDeleteItemPurchase(itemPurchase)

                mUpdateItemPurchaseStatus.value = CrudStatus.Success
            } catch (error: Throwable) {
                mUpdateItemPurchaseStatus.value = CrudStatus.Error(error)
            }
        }
    }

    fun deleteItemPurchase(itemPurchase: ItemPurchase) {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                mDeleteItemPurchaseStatus.value = CrudStatus.Loading

                handleDeleteItemPurchase(itemPurchase)

                mDeleteItemPurchaseStatus.value = CrudStatus.Success
            } catch (error: Throwable) {
                mDeleteItemPurchaseStatus.value = CrudStatus.Error(error)
            }
        }
    }

    private suspend fun handleUpdateOrDeleteItemPurchase(itemPurchaseUpdate: ItemPurchase) {
        crudEventType = C_CRUD_EVENT_ITEM_UPDATE
        crudItemPurchase = itemPurchaseUpdate

        val isDelete = itemPurchaseUpdate.quantity == 0

        if (isDelete) {
            localItemPurchaseRepository.delete(itemPurchaseUpdate)
        } else {
            localItemPurchaseRepository.update(itemPurchaseUpdate)
        }

        modifyListItemPurchase(isDelete, itemPurchaseUpdate)
    }

    private suspend fun handleDeleteItemPurchase(itemPurchaseDelete: ItemPurchase) {
        crudEventType = C_CRUD_EVENT_ITEM_DELETE
        crudItemPurchase = itemPurchaseDelete

        localItemPurchaseRepository.delete(itemPurchaseDelete)
        modifyListItemPurchase(isDelete = true, itemPurchaseDelete)
    }

    private fun modifyListItemPurchase(isDelete: Boolean, newItemPurchase: ItemPurchase) {
        val oldItemPurchase = currentListItemPurchase?.firstOrNull { itemPurchase ->
            itemPurchase.purchaseId == newItemPurchase.purchaseId &&
            itemPurchase.prodId == newItemPurchase.prodId
        }

        oldItemPurchase?.let { itemPurchase ->
            val index = currentListItemPurchase?.indexOf(itemPurchase)

            index?.let { idx ->
                if (isDelete) {
                    currentListItemPurchase?.removeAt(idx)
                } else {
                    currentListItemPurchase?.set(idx, newItemPurchase)
                }
            }
        }

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