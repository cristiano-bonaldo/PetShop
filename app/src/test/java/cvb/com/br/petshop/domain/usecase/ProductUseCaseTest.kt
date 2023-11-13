package cvb.com.br.petshop.domain.usecase

import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.data.util.ProductFactory
import cvb.com.br.petshop.domain.repository.ProductRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

//This instruction has is used for enable the mocks annotation
//For this test class, we stated the mocks annotation with MockitoAnnotations.openMocks(this)
//@RunWith(MockitoJUnitRunner::class)
class ProductUseCaseTest {

    @Mock
    lateinit var productRepository: ProductRepository

    @InjectMocks
    lateinit var productUseCase: ProductUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getProducts_Success() = runTest {
        val prod1 = ProductFactory.createProduct(1)
        val prod2 = ProductFactory.createProduct(2)
        val prod3 = ProductFactory.createProduct(3)

        val listProd = mutableListOf(prod1, prod2, prod3)

        Mockito.`when`(productRepository.getProducts()).thenReturn(listProd)

        val listResult = productUseCase.getLocalProducts()

        assertThat(listResult.size).isEqualTo(listProd.size)
        assertThat(listResult).contains(prod1)
        assertThat(listResult).contains(prod2)
        assertThat(listResult).contains(prod3)
    }

    @Test
    fun loadProducts_Success() = runTest {
        val prod1 = ProductFactory.createProduct(1)
        val prod2 = ProductFactory.createProduct(2)
        val prod3 = ProductFactory.createProduct(3)

        val listProd = mutableListOf(prod1, prod2, prod3)

        Mockito.`when`(productRepository.loadProducts()).thenReturn(listProd)

        val listResult = productUseCase.loadProducts()

        assertThat(listResult.size).isEqualTo(listProd.size)
        assertThat(listResult).contains(prod1)
        assertThat(listResult).contains(prod2)
        assertThat(listResult).contains(prod3)
    }
}