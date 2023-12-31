package cvb.com.br.petshop.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.data.repository.fake.ProductRepositoryFake
import cvb.com.br.petshop.domain.model.Product
import cvb.com.br.petshop.domain.usecase.ProductUseCase
import cvb.com.br.petshop.presentation.viewmodel.status.UIStatus
import cvb.com.br.petshop.util.MainDispatcherRule
import cvb.com.br.petshop.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FragListProductViewModelTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: FragListProductViewModel

    private lateinit var productRepository: ProductRepositoryFake

    private lateinit var productUseCase: ProductUseCase

    @Before
    fun setup() {
        productRepository = ProductRepositoryFake()

        productUseCase = ProductUseCase(productRepository)

        viewModel = FragListProductViewModel(
            Dispatchers.Default,
            productUseCase
        )
    }

    @Test
    fun loadProductState_Success() = runTest {
        viewModel.loadProducts()

        val loadProductStatus = viewModel.loadProductStatus.getOrAwaitValue()

        assertThat(loadProductStatus).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun loadProductState_Loading_Success() = runTest {
        val listProductStatus = mutableListOf<UIStatus<List<Product>>>()

        viewModel.loadProductStatus.observeForever { status ->
            listProductStatus.add(status)
        }

        viewModel.loadProducts()

        assertThat(listProductStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        assertThat(listProductStatus[1]).isInstanceOf(UIStatus.Success::class.java)
    }

    @Test
    fun loadProductState_Loading_Error() = runTest {
        productRepository.enableError()

        val listProductStatus = mutableListOf<UIStatus<List<Product>>>()
        viewModel.loadProductStatus.observeForever { status ->
            listProductStatus.add(status)
        }

        viewModel.loadProducts()

        assertThat(listProductStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        assertThat(listProductStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun isLoadingForSuccess() = runTest {
        val isLoadingStatus = mutableListOf<Boolean>()
        viewModel.isLoading.observeForever { status ->
            isLoadingStatus.add(status)
        }

        viewModel.loadProducts()

        assertThat(isLoadingStatus[0]).isTrue()
        assertThat(isLoadingStatus[1]).isFalse()
    }

    @Test
    fun isLoadingForError() = runTest {
        productRepository.enableError()

        val isLoadingStatus = mutableListOf<Boolean>()
        viewModel.isLoading.observeForever { status ->
            isLoadingStatus.add(status)
        }

        viewModel.loadProducts()

        assertThat(isLoadingStatus[0]).isTrue()
        assertThat(isLoadingStatus[1]).isFalse()
    }
}