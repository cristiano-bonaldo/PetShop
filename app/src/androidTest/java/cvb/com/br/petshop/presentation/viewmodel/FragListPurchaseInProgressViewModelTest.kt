package cvb.com.br.petshop.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import cvb.com.br.petshop.data.model.util.ItemPurchaseFactory
import cvb.com.br.petshop.data.repository.ItemPurchaseRepositoryFake
import cvb.com.br.petshop.data.repository.ProductRepositoryFake
import cvb.com.br.petshop.data.repository.PurchaseRepositoryFake
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.domain.usecase.ItemPurchaseUseCase
import cvb.com.br.petshop.domain.usecase.ProductUseCase
import cvb.com.br.petshop.domain.usecase.PurchaseUseCase
import cvb.com.br.petshop.presentation.util.getOrAwaitValue
import cvb.com.br.petshop.presentation.viewmodel.status.UIStatus
import cvb.com.br.petshop.util.PurchaseUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FragListPurchaseInProgressViewModelTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FragListPurchaseInProgressViewModel

    private lateinit var productRepositoryFake: ProductRepositoryFake
    private lateinit var purchaseRepositoryFake: PurchaseRepositoryFake
    private lateinit var itemPurchaseRepositoryFake: ItemPurchaseRepositoryFake

    private lateinit var productUseCase: ProductUseCase
    private lateinit var purchaseUseCase: PurchaseUseCase
    private lateinit var itemPurchaseUseCase: ItemPurchaseUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        productRepositoryFake = ProductRepositoryFake()
        productUseCase = ProductUseCase(productRepositoryFake)

        purchaseRepositoryFake = PurchaseRepositoryFake()
        purchaseUseCase = PurchaseUseCase(purchaseRepositoryFake)

        itemPurchaseRepositoryFake = ItemPurchaseRepositoryFake()
        itemPurchaseUseCase = ItemPurchaseUseCase(itemPurchaseRepositoryFake)

        val purchaseUtil = PurchaseUtil(ApplicationProvider.getApplicationContext())

        viewModel = FragListPurchaseInProgressViewModel(
            ApplicationProvider.getApplicationContext(),
            UnconfinedTestDispatcher(),
            productUseCase,
            purchaseUseCase,
            itemPurchaseUseCase,
            purchaseUtil
        )
    }

    @Test
    fun loadPurchaseState_Success() = runTest {
        viewModel.loadPurchase()
        advanceUntilIdle()

        val status = viewModel.loadPurchaseStatus.getOrAwaitValue()

        Truth.assertThat(status).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun loadProductState_Loading_Success() = runTest {
        val listStatus = mutableListOf<UIStatus<List<Purchase?>>>()
        viewModel.loadPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        viewModel.loadPurchase()
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun loadProductState_Loading_Error() = runTest {
        purchaseRepositoryFake.enableError()

        val listStatus = mutableListOf<UIStatus<List<Purchase?>>>()
        viewModel.loadPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        viewModel.loadPurchase()
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun updateItemPurchaseStatus_Success() = runTest {
        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1, 1)

        viewModel.updateItemPurchase(itemPurchase)
        advanceUntilIdle()

        val status = viewModel.updateItemPurchaseStatus.getOrAwaitValue()

        Truth.assertThat(status).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun updateItemPurchaseState_Loading_Success() = runTest {
        val listStatus = mutableListOf<UIStatus<Nothing>>()
        viewModel.updateItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1, 1)

        viewModel.updateItemPurchase(itemPurchase)
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun updateItemPurchaseState_Loading_Error() = runTest {
        itemPurchaseRepositoryFake.enableError()

        val listStatus = mutableListOf<UIStatus<Nothing>>()
        viewModel.updateItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1, 1)

        viewModel.updateItemPurchase(itemPurchase)
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun deleteItemPurchaseStatus_Success() = runTest {
        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1, 1)

        viewModel.deleteItemPurchase(itemPurchase)
        advanceUntilIdle()

        val status = viewModel.deleteItemPurchaseStatus.getOrAwaitValue()

        Truth.assertThat(status).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun deleteItemPurchaseState_Loading_Success() = runTest {
        val listStatus = mutableListOf<UIStatus<Nothing>>()
        viewModel.deleteItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1, 1)

        viewModel.deleteItemPurchase(itemPurchase)
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun deleteItemPurchaseState_Loading_Error() = runTest {
        itemPurchaseRepositoryFake.enableError()

        val listStatus = mutableListOf<UIStatus<Nothing>>()
        viewModel.deleteItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1, 1)

        viewModel.deleteItemPurchase(itemPurchase)
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun finishPurchaseStatus_Error() = runTest {
        viewModel.finishPurchase()
        advanceUntilIdle()

        val status = viewModel.finishPurchaseStatus.getOrAwaitValue()

        Truth.assertThat(status).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun finishPurchaseState_Loading_Error() = runTest {
        val listStatus = mutableListOf<UIStatus<Nothing>>()
        viewModel.finishPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        viewModel.finishPurchase()
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }
}