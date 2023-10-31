package cvb.com.br.petshop.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import cvb.com.br.petshop.data.datasource.ItemPurchaseDataSourceFake
import cvb.com.br.petshop.data.datasource.ProductDataSourceFake
import cvb.com.br.petshop.data.datasource.PurchaseDataSourceFake
import cvb.com.br.petshop.data.model.util.ItemPurchaseFactory
import cvb.com.br.petshop.presentation.viewmodel.FragListPurchaseInProgressViewModel
import cvb.com.br.petshop.util.PurchaseUtil
import cvb.com.br.petshop.util.getOrAwaitValue
import cvb.com.br.petshop.presentation.viewmodel.status.CrudStatus
import cvb.com.br.petshop.presentation.viewmodel.status.LoadProductStatus
import cvb.com.br.petshop.presentation.viewmodel.status.LoadPurchaseStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    private lateinit var productRepositoryFake: ProductDataSourceFake
    private lateinit var purchaseRepositoryFake: PurchaseDataSourceFake
    private lateinit var itemPurchaseRepositoryFake: ItemPurchaseDataSourceFake

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        productRepositoryFake = ProductDataSourceFake()
        purchaseRepositoryFake = PurchaseDataSourceFake()
        itemPurchaseRepositoryFake = ItemPurchaseDataSourceFake()

        val purchaseUtil = PurchaseUtil(ApplicationProvider.getApplicationContext())

        viewModel = FragListPurchaseInProgressViewModel(
            ApplicationProvider.getApplicationContext(),
            UnconfinedTestDispatcher(),
            productRepositoryFake,
            purchaseRepositoryFake,
            itemPurchaseRepositoryFake,
            purchaseUtil
        )
    }

    @Test
    fun loadPurchaseState_Success() = runTest {
        viewModel.loadPurchase()
        advanceUntilIdle()

        val status = viewModel.loadPurchaseStatus.getOrAwaitValue()

        Truth.assertThat(status).isInstanceOf(LoadPurchaseStatus.Success::class.java)
    }

    @Test
    fun loadProductState_Loading_Success() = runTest {
        val listStatus = mutableListOf<LoadPurchaseStatus>()
        viewModel.loadPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        viewModel.loadPurchase()
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(LoadPurchaseStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(LoadPurchaseStatus.Success::class.java)
    }

    @Test
    fun loadProductState_Loading_Error() = runTest {
        purchaseRepositoryFake.enableError()

        val listStatus = mutableListOf<LoadPurchaseStatus>()
        viewModel.loadPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        viewModel.loadPurchase()
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(LoadPurchaseStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(LoadPurchaseStatus.Error::class.java)
    }

    @Test
    fun updateItemPurchaseStatus_Success() = runTest {
        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1,1)

        viewModel.updateItemPurchase(itemPurchase)
        advanceUntilIdle()

        val status = viewModel.updateItemPurchaseStatus.getOrAwaitValue()

        Truth.assertThat(status).isInstanceOf(CrudStatus.Success::class.java)
    }

    @Test
    fun updateItemPurchaseState_Loading_Success() = runTest {
        val listStatus = mutableListOf<CrudStatus>()
        viewModel.updateItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1,1)

        viewModel.updateItemPurchase(itemPurchase)
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(CrudStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(CrudStatus.Success::class.java)
    }

    @Test
    fun updateItemPurchaseState_Loading_Error() = runTest {
        itemPurchaseRepositoryFake.enableError()

        val listStatus = mutableListOf<CrudStatus>()
        viewModel.updateItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1,1)

        viewModel.updateItemPurchase(itemPurchase)
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(CrudStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(CrudStatus.Error::class.java)
    }

    @Test
    fun deleteItemPurchaseStatus_Success() = runTest {
        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1,1)

        viewModel.deleteItemPurchase(itemPurchase)
        advanceUntilIdle()

        val status = viewModel.deleteItemPurchaseStatus.getOrAwaitValue()

        Truth.assertThat(status).isInstanceOf(CrudStatus.Success::class.java)
    }

    @Test
    fun deleteItemPurchaseState_Loading_Success() = runTest {
        val listStatus = mutableListOf<CrudStatus>()
        viewModel.deleteItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1,1)

        viewModel.deleteItemPurchase(itemPurchase)
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(CrudStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(CrudStatus.Success::class.java)
    }

    @Test
    fun deleteItemPurchaseState_Loading_Error() = runTest {
        itemPurchaseRepositoryFake.enableError()

        val listStatus = mutableListOf<CrudStatus>()
        viewModel.deleteItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1,1)

        viewModel.deleteItemPurchase(itemPurchase)
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(CrudStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(CrudStatus.Error::class.java)
    }

    @Test
    fun finishPurchaseStatus_Error() = runTest {
            viewModel.finishPurchase()
            advanceUntilIdle()

            val status = viewModel.finishPurchaseStatus.getOrAwaitValue()

            Truth.assertThat(status).isInstanceOf(CrudStatus.Error::class.java)
    }

    @Test
    fun finishPurchaseState_Loading_Error() = runTest {
        val listStatus = mutableListOf<CrudStatus>()
        viewModel.finishPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        viewModel.finishPurchase()
        advanceUntilIdle()

        Truth.assertThat(listStatus[0]).isInstanceOf(CrudStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(CrudStatus.Error::class.java)
    }
}