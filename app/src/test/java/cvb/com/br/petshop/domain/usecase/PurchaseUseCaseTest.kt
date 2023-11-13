package cvb.com.br.petshop.domain.usecase

import android.annotation.SuppressLint
import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.data.util.PurchaseFactory
import cvb.com.br.petshop.domain.enum.PurchaseStatusEnum
import cvb.com.br.petshop.domain.model.Purchase
import cvb.com.br.petshop.domain.repository.PurchaseRepository
import cvb.com.br.petshop.util.MockitoUtil
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.MockitoJUnit


class PurchaseUseCaseTest {

    @Mock
    lateinit var purchaseRepository: PurchaseRepository

    @InjectMocks
    lateinit var purchaseUseCase: PurchaseUseCase

    @JvmField
    @Rule
    val mockitoRule = MockitoJUnit.rule()

    //Alternativa -> Neste exemplo, inicializamos o Mockito atraves da Rule
    /*
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }
    */

    @SuppressLint("CheckResult")
    @Test
    fun openPurchase_Success() = runTest {
        val newPurchase = PurchaseFactory.createPurchase(1)

        Mockito.`when`(purchaseRepository.insert(newPurchase)).thenReturn(Unit)
        Mockito.`when`(purchaseRepository.getPurchaseInProgress()).thenReturn(newPurchase)

        val purchaseCreated = purchaseUseCase.openPurchase()

        val inOrder = Mockito.inOrder(purchaseRepository)

        inOrder.verify(purchaseRepository, Mockito.times(1)).insert(MockitoUtil.anyMockitoInstanceOf(Purchase::class.java))
        inOrder.verify(purchaseRepository, Mockito.times(1)).getPurchaseInProgress()

        verifyNoMoreInteractions(purchaseRepository)

        assertThat(purchaseCreated).isNotNull()
    }

    @Test
    fun openPurchaseWithCaptor_Success() = runTest {
        val captor: ArgumentCaptor<Purchase> = ArgumentCaptor.forClass(Purchase::class.java)

        purchaseUseCase.openPurchase()

        verify(purchaseRepository).insert(MockitoUtil.captureArgument(captor))
        verify(purchaseRepository, Mockito.times(1)).getPurchaseInProgress()
        verifyNoMoreInteractions(purchaseRepository)

        val purchase = captor.value

        assertThat(purchase).isNotNull()
    }

    @Test
    fun getPurchaseInProgress_Return_Purchase() = runTest {
        val purchase = PurchaseFactory.createPurchase(1)

        Mockito.`when`(purchaseRepository.getPurchaseInProgress()).thenReturn(purchase)

        val result = purchaseUseCase.getPurchaseInProgress()

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(purchase)
    }

    @Test
    fun getPurchaseInProgress_Return_Null() = runTest {
        val purchase = null

        Mockito.`when`(purchaseRepository.getPurchaseInProgress()).thenReturn(purchase)

        val result = purchaseUseCase.getPurchaseInProgress()

        assertThat(result).isNull()
    }

    @Test
    fun finishPurchase_Success() = runTest {
        val captor: ArgumentCaptor<Purchase> = ArgumentCaptor.forClass(Purchase::class.java)

        val purchaseInProgress = PurchaseFactory.createPurchase(500)

        val purchaseFinished = purchaseUseCase.finishPurchase(purchaseInProgress)

        verify(purchaseRepository).update(MockitoUtil.captureArgument(captor))

        val purchaseCaptured = captor.value

        assertThat(purchaseCaptured.status).isEqualTo(PurchaseStatusEnum.PURCHASE_STATUS_CLOSED.status)
        assertThat(purchaseCaptured.convertedAt).isAtLeast(purchaseCaptured.createAt)
        assertThat(purchaseFinished).isEqualTo(purchaseCaptured)
    }
}