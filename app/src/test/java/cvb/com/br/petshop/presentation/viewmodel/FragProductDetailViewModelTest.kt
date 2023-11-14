package cvb.com.br.petshop.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.data.repository.fake.ItemPurchaseRepositoryFake
import cvb.com.br.petshop.data.repository.fake.PurchaseRepositoryFake
import cvb.com.br.petshop.data.util.ProductFactory
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.usecase.ItemPurchaseUseCase
import cvb.com.br.petshop.domain.usecase.PurchaseUseCase
import cvb.com.br.petshop.presentation.viewmodel.status.UIStatus
import cvb.com.br.petshop.util.MainDispatcherRule
import cvb.com.br.petshop.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FragProductDetailViewModelTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: FragProductDetailViewModel

    private lateinit var itemPurchaseRepositoryFake: ItemPurchaseRepositoryFake
    private lateinit var purchaseRepositoryFake: PurchaseRepositoryFake

    private lateinit var purchaseUseCase: PurchaseUseCase
    private lateinit var itemPurchaseUseCase: ItemPurchaseUseCase

    @Before
    fun setup() {
        purchaseRepositoryFake = PurchaseRepositoryFake()
        purchaseUseCase = PurchaseUseCase(
            purchaseRepositoryFake
        )

        itemPurchaseRepositoryFake = ItemPurchaseRepositoryFake()
        itemPurchaseUseCase = ItemPurchaseUseCase(
            itemPurchaseRepositoryFake
        )

        viewModel = FragProductDetailViewModel(
            Dispatchers.Default,
            purchaseUseCase,
            itemPurchaseUseCase
        )
    }

    @Test
    fun loadItemPurchaseStatus_Success() = runTest {
        val product = ProductFactory.createProduct(1)

        viewModel.loadItemPurchase(product)

        val status = viewModel.loadItemPurchaseStatus.getOrAwaitValue()

        assertThat(status).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun loadItemPurchaseState_Loading_Success() = runTest {
        val listStatus = mutableListOf<UIStatus<ItemPurchase>>()
        viewModel.loadItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val product = ProductFactory.createProduct(1)

        viewModel.loadItemPurchase(product)

        assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun loadItemPurchaseState_Loading_Error() = runTest {
        purchaseRepositoryFake.enableError()

        val listStatus = mutableListOf<UIStatus<ItemPurchase>>()
        viewModel.loadItemPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val product = ProductFactory.createProduct(1)

        viewModel.loadItemPurchase(product)

        assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun addPurchaseStatus_Success() = runTest {
        val product = ProductFactory.createProduct(1)
        val qtd = 1

        viewModel.addItemPurchase(product, qtd)

        val status = viewModel.addPurchaseStatus.getOrAwaitValue()

        assertThat(status).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun addPurchaseStatusState_Loading_Success() = runTest {
        val listStatus = mutableListOf<UIStatus<ItemPurchase>>()
        viewModel.addPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val product = ProductFactory.createProduct(1)
        val qtd = 1

        viewModel.addItemPurchase(product, qtd)

        assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun addPurchaseStatusState_Loading_Error() = runTest {
        purchaseRepositoryFake.enableError()

        val listStatus = mutableListOf<UIStatus<ItemPurchase>>()
        viewModel.addPurchaseStatus.observeForever { status ->
            listStatus.add(status)
        }

        val product = ProductFactory.createProduct(1)
        val qtd = 1

        viewModel.addItemPurchase(product, qtd)

        assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }
}
