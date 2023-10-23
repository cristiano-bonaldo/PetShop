package cvb.com.br.petshop.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.viewmodel.data.datasource.ItemPurchaseDataSourceFake
import cvb.com.br.petshop.viewmodel.data.datasource.PurchaseDataSourceFake
import cvb.com.br.petshop.viewmodel.status.CrudStatus
import cvb.com.br.petshop.viewmodel.status.LoadItemPurchaseStatus
import cvb.com.br.petshop.viewmodel.util.ProductFactory
import cvb.com.br.petshop.viewmodel.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FragProductDetailViewModelTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FragProductDetailViewModel

    private lateinit var purchaseRepositoryFake: PurchaseDataSourceFake
    private lateinit var itemPurchaseRepositoryFake: ItemPurchaseDataSourceFake

    @Before
    fun setup() {
        purchaseRepositoryFake = PurchaseDataSourceFake()
        itemPurchaseRepositoryFake = ItemPurchaseDataSourceFake()

        viewModel = FragProductDetailViewModel(
            UnconfinedTestDispatcher(),
            purchaseRepositoryFake,
            itemPurchaseRepositoryFake
        )
    }

    @Test
    fun loadItemPurchaseStatus_Success() = runTest {
        val product = ProductFactory.createProduct(1)

        viewModel.loadItemPurchase(product)
        advanceUntilIdle()

        val status = viewModel.loadItemPurchaseStatus.getOrAwaitValue()

        assertThat(status).isInstanceOf(LoadItemPurchaseStatus.Success::class.java)
    }

    @Test
    fun loadItemPurchaseState_Loading_Success() = runTest {
        val listStatus = mutableListOf<LoadItemPurchaseStatus>()
        viewModel.loadItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val product = ProductFactory.createProduct(1)

        viewModel.loadItemPurchase(product)
        advanceUntilIdle()

        assertThat(listStatus[0]).isInstanceOf(LoadItemPurchaseStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(LoadItemPurchaseStatus.Success::class.java)
    }

    @Test
    fun loadItemPurchaseState_Loading_Error() = runTest {
        purchaseRepositoryFake.enableError()

        val listStatus = mutableListOf<LoadItemPurchaseStatus>()
        viewModel.loadItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val product = ProductFactory.createProduct(1)

        viewModel.loadItemPurchase(product)
        advanceUntilIdle()

        assertThat(listStatus[0]).isInstanceOf(LoadItemPurchaseStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(LoadItemPurchaseStatus.Error::class.java)
    }

    @Test
    fun addPurchaseStatus_Success() = runTest {
        val product = ProductFactory.createProduct(1)
        val qtd = 1

        viewModel.addItemPurchase(product, qtd)
        advanceUntilIdle()

        val status = viewModel.addPurchaseStatus.getOrAwaitValue()

        assertThat(status).isInstanceOf(CrudStatus.Success::class.java)
    }

    @Test
    fun addPurchaseStatusState_Loading_Success() = runTest {
        val listStatus = mutableListOf<CrudStatus>()
        viewModel.addPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val product = ProductFactory.createProduct(1)
        val qtd = 1

        viewModel.addItemPurchase(product, qtd)
        advanceUntilIdle()

        assertThat(listStatus[0]).isInstanceOf(CrudStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(CrudStatus.Success::class.java)
    }

    @Test
    fun addPurchaseStatusState_Loading_Error() = runTest {
        purchaseRepositoryFake.enableError()

        val listStatus = mutableListOf<CrudStatus>()
        viewModel.addPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val product = ProductFactory.createProduct(1)
        val qtd = 1

        viewModel.addItemPurchase(product, qtd)
        advanceUntilIdle()

        assertThat(listStatus[0]).isInstanceOf(CrudStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(CrudStatus.Error::class.java)
    }
}
