package cvb.com.br.petshop.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.viewmodel.data.datasource.ProductDataSourceFake
import cvb.com.br.petshop.viewmodel.status.LoadProductStatus
import cvb.com.br.petshop.viewmodel.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FragListProductViewModelTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FragListProductViewModel

    private lateinit var remoteProductRepositoryFake: ProductDataSourceFake
    private lateinit var localProductRepositoryFake: ProductDataSourceFake

    @Before
    fun setup() {
        remoteProductRepositoryFake = ProductDataSourceFake()
        localProductRepositoryFake = ProductDataSourceFake()

        viewModel = FragListProductViewModel(
            UnconfinedTestDispatcher(),
            remoteProductRepositoryFake,
            localProductRepositoryFake
        )
    }

    @Test
    fun loadProductState_Success() = runTest {
        viewModel.loadProducts()
        advanceUntilIdle()

        val loadProductStatus = viewModel.loadProductStatus.getOrAwaitValue()

        assertThat(loadProductStatus).isInstanceOf(LoadProductStatus.Success::class.java)
    }

    @Test
    fun loadProductState_Loading_Success() = runTest {
        val listProductStatus = mutableListOf<LoadProductStatus>()
        viewModel.loadProductStatus.observeForever { status ->
            listProductStatus.add(status)
        }

        viewModel.loadProducts()
        advanceUntilIdle()

        assertThat(listProductStatus[0]).isInstanceOf(LoadProductStatus.Loading::class.java)
        assertThat(listProductStatus[1]).isInstanceOf(LoadProductStatus.Success::class.java)
    }

    @Test
    fun loadProductState_Loading_Error() = runTest {
        remoteProductRepositoryFake.enableError()

        val listProductStatus = mutableListOf<LoadProductStatus>()
        viewModel.loadProductStatus.observeForever { status ->
            listProductStatus.add(status)
        }

        viewModel.loadProducts()
        advanceUntilIdle()

        assertThat(listProductStatus[0]).isInstanceOf(LoadProductStatus.Loading::class.java)
        assertThat(listProductStatus[1]).isInstanceOf(LoadProductStatus.Error::class.java)
    }

    @Test
    fun isLoadingForSuccess() = runTest {
        val isLoadingStatus = mutableListOf<Boolean>()
        viewModel.isLoading.observeForever { status ->
            isLoadingStatus.add(status)
        }

        viewModel.loadProducts()
        advanceUntilIdle()

        assertThat(isLoadingStatus[0]).isTrue()
        assertThat(isLoadingStatus[1]).isFalse()
    }

    @Test
    fun isLoadingForError() = runTest {
        remoteProductRepositoryFake.enableError()

        val isLoadingStatus = mutableListOf<Boolean>()
        viewModel.isLoading.observeForever { status ->
            isLoadingStatus.add(status)
        }

        viewModel.loadProducts()
        advanceUntilIdle()

        assertThat(isLoadingStatus[0]).isTrue()
        assertThat(isLoadingStatus[1]).isFalse()
    }
}
