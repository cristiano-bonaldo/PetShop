package cvb.com.br.petshop.data.repository

import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.data.util.PurchaseFactory
import cvb.com.br.petshop.domain.datasource.PurchaseDataSource
import cvb.com.br.petshop.domain.model.Purchase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PurchaseRepositoryImplTest {

    @Mock
    lateinit var purchaseDataSource: PurchaseDataSource

    @InjectMocks
    lateinit var purchaseRepositoryImpl: PurchaseRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun update_Success() = runTest {
        val purchase = PurchaseFactory.createPurchase(1)

        purchaseRepositoryImpl.update(purchase)

        Mockito.verify(purchaseDataSource, Mockito.times(1)).update(purchase)
        Mockito.verifyNoMoreInteractions(purchaseDataSource)
    }

    @Test
    fun insert_Success() = runTest {
        val purchase = PurchaseFactory.createPurchase(1)

        purchaseRepositoryImpl.insert(purchase)

        Mockito.verify(purchaseDataSource, Mockito.times(1)).insert(purchase)
        Mockito.verifyNoMoreInteractions(purchaseDataSource)
    }

    @Test
    fun getPurchaseInProgress_WithData_Success() = runTest {
        val purchase = PurchaseFactory.createPurchase(1)

        Mockito.`when`(purchaseDataSource.getPurchaseInProgress()).thenReturn(purchase)

        val purchaseResult = purchaseRepositoryImpl.getPurchaseInProgress()

        Mockito.verify(purchaseDataSource, Mockito.times(1)).getPurchaseInProgress()
        Mockito.verifyNoMoreInteractions(purchaseDataSource)

        assertThat(purchaseResult).isNotNull()
        assertThat(purchaseResult).isEqualTo(purchase)
    }

    @Test
    fun getPurchaseInProgress_WithNoData_Success() = runTest {
        val purchase: Purchase? = null

        Mockito.`when`(purchaseDataSource.getPurchaseInProgress()).thenReturn(purchase)

        val purchaseResult = purchaseRepositoryImpl.getPurchaseInProgress()

        Mockito.verify(purchaseDataSource, Mockito.times(1)).getPurchaseInProgress()
        Mockito.verifyNoMoreInteractions(purchaseDataSource)

        assertThat(purchaseResult).isNull()
    }
}
