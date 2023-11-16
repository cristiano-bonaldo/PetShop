package cvb.com.br.petshop.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import cvb.com.br.petshop.PetShopApplication
import cvb.com.br.petshop.data.repository.fake.ItemPurchaseRepositoryFake
import cvb.com.br.petshop.data.repository.fake.ProductRepositoryFake
import cvb.com.br.petshop.data.repository.fake.PurchaseRepositoryFake
import cvb.com.br.petshop.data.util.ItemPurchaseFactory
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.domain.usecase.ItemPurchaseUseCase
import cvb.com.br.petshop.domain.usecase.ProductUseCase
import cvb.com.br.petshop.domain.usecase.PurchaseUseCase
import cvb.com.br.petshop.presentation.viewmodel.status.UIStatus
import cvb.com.br.petshop.util.MainDispatcherRule
import cvb.com.br.petshop.util.PurchaseUtil
import cvb.com.br.petshop.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragListPurchaseInProgressViewModelTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: FragListPurchaseInProgressViewModel

    private lateinit var productRepositoryFake: ProductRepositoryFake
    private lateinit var purchaseRepositoryFake: PurchaseRepositoryFake
    private lateinit var itemPurchaseRepositoryFake: ItemPurchaseRepositoryFake

    private lateinit var productUseCase: ProductUseCase
    private lateinit var purchaseUseCase: PurchaseUseCase
    private lateinit var itemPurchaseUseCase: ItemPurchaseUseCase

    @Before
    fun setup() {
        productRepositoryFake = ProductRepositoryFake()
        productUseCase = ProductUseCase(productRepositoryFake)

        purchaseRepositoryFake = PurchaseRepositoryFake()
        purchaseUseCase = PurchaseUseCase(purchaseRepositoryFake)

        itemPurchaseRepositoryFake = ItemPurchaseRepositoryFake()
        itemPurchaseUseCase = ItemPurchaseUseCase(itemPurchaseRepositoryFake)

        val app = getApplicationContext<PetShopApplication>()

        val purchaseUtil = PurchaseUtil(app)

        viewModel = FragListPurchaseInProgressViewModel(
            app,
            Dispatchers.Default,
            productUseCase,
            purchaseUseCase,
            itemPurchaseUseCase,
            purchaseUtil
        )
    }

    @Test
    fun loadPurchaseState_Success() = runTest {
        viewModel.loadPurchase()

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

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun updateItemPurchaseStatus_Success() = runTest {
        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1, 1)

        viewModel.updateItemPurchase(itemPurchase)

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

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun deleteItemPurchaseStatus_Success() = runTest {
        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1, 1)

        viewModel.deleteItemPurchase(itemPurchase)

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

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun finishPurchaseStatus_Error() = runTest {
        viewModel.finishPurchase()

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

        Truth.assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        Truth.assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }
}