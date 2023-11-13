package cvb.com.br.petshop.data.repository

import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.data.util.ProductFactory
import cvb.com.br.petshop.domain.datasource.ProductDataSource
import cvb.com.br.petshop.domain.model.Product
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ProductRepositoryImplTest {

    @Mock
    lateinit var remoteDataSource: ProductDataSource
    @Mock
    lateinit var localDataSource: ProductDataSource

    @InjectMocks
    lateinit var productRepositoryImpl: ProductRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }


    /*
    ##############################################
    ##############################################

    Erro Mockito -> Ploblema ao instanciar classe 'mockada' com parametros do mesmo tipo!
    No caso, Local localDataSource e remoteDataSource são implementações de ProductDataSource
    O realizar a injeção de dependencia, o Mockito esta sempre injetanto o mesmo objeto,
    com se estivesse fasendo:
    val productRepositoryImpl = ProductRepositoryImpl(localDataSource, localDataSource)

    Procurei uma solução na internet... Não encontrei resolução

    ##############################################
    ##############################################
    */

    @Ignore
    @Test
    fun getProduct_Success() = runTest {
        val prod1 = ProductFactory.createProduct(1)
        val prod2 = ProductFactory.createProduct(2)
        val prod3 = ProductFactory.createProduct(3)

        val listProd = listOf(prod1, prod2, prod3)

        Mockito.`when`(localDataSource.getAll()).thenReturn(listProd)

        val listResult = productRepositoryImpl.getProducts()

        assertThat(listResult.size).isEqualTo(listProd.size)
        assertThat(listResult).contains(prod1)
        assertThat(listResult).contains(prod2)
        assertThat(listResult).contains(prod3)
    }

    @Ignore
    @Test
    fun loadProductWithNoLocalData_Success() = runTest {
        val localEmptyProdList = listOf<Product>()
        Mockito.`when`(localDataSource.getAll()).thenReturn(localEmptyProdList)

        //---

        val prod1 = ProductFactory.createProduct(1)
        val prod2 = ProductFactory.createProduct(2)
        val prod3 = ProductFactory.createProduct(3)

        val remoteProdList = listOf(prod1, prod2, prod3)

        Mockito.`when`(remoteDataSource.getAll()).thenReturn(remoteProdList)

        productRepositoryImpl.loadProducts()

        Mockito.verify(localDataSource, Mockito.times(1)).insert(prod1)
        Mockito.verify(localDataSource, Mockito.times(1)).insert(prod2)
        Mockito.verify(localDataSource, Mockito.times(1)).insert(prod3)
    }
}
