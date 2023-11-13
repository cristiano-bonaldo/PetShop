package cvb.com.br.petshop.domain.usecase

import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.data.util.ItemPurchaseFactory
import cvb.com.br.petshop.data.util.ProductFactory
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.repository.ItemPurchaseRepository
import cvb.com.br.petshop.util.MockitoUtil
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ItemPurchaseUseCaseTest {

    @Mock
    lateinit var itemPurchaseRepository: ItemPurchaseRepository

    @InjectMocks
    lateinit var itemPurchaseUseCase: ItemPurchaseUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getItemsByPurchase_WithData_Success() = runTest {
        val item1 = ItemPurchaseFactory.createItemPurchase(1, 1)
        val item2 = ItemPurchaseFactory.createItemPurchase(1, 2)
        val item3 = ItemPurchaseFactory.createItemPurchase(1, 3)

        val itemList = listOf(item1, item2, item3)

        Mockito.`when`(itemPurchaseRepository.getItemsByPurchase(1)).thenReturn(itemList)

        val itemListResult = itemPurchaseUseCase.getItemsByPurchaseId(1)

        Mockito.verify(itemPurchaseRepository, Mockito.times(1)).getItemsByPurchase(1)
        Mockito.verifyNoMoreInteractions(itemPurchaseRepository)

        assertThat(itemListResult.size).isEqualTo(itemList.size)
        assertThat(itemListResult).contains(item1)
        assertThat(itemListResult).contains(item2)
        assertThat(itemListResult).contains(item3)
    }

    @Test
    fun getItemsByPurchase_WithNoData_Success() = runTest {
        val itemList = listOf<ItemPurchase>()

        Mockito.`when`(itemPurchaseRepository.getItemsByPurchase(1)).thenReturn(itemList)

        val itemListResult = itemPurchaseUseCase.getItemsByPurchaseId(1)

        Mockito.verify(itemPurchaseRepository, Mockito.times(1)).getItemsByPurchase(1)
        Mockito.verifyNoMoreInteractions(itemPurchaseRepository)

        assertThat(itemListResult).isEmpty()
    }

    @Test
    fun getItemByPurchaseAndProduct_WithData_Success() = runTest {
        val item = ItemPurchaseFactory.createItemPurchase(1, 1)

        Mockito.`when`(itemPurchaseRepository.getItemByPurchaseAndProduct(1, 1)).thenReturn(item)

        val itemResult = itemPurchaseUseCase.getItemByPurchaseAndProduct(1,1)

        Mockito.verify(itemPurchaseRepository, Mockito.times(1)).getItemByPurchaseAndProduct(1,1)
        Mockito.verifyNoMoreInteractions(itemPurchaseRepository)

        assertThat(itemResult).isNotNull()
        assertThat(itemResult).isEqualTo(item)
    }

    @Test
    fun getItemByPurchaseAndProduct_WithNoData_Success() = runTest {
        val item: ItemPurchase? = null

        Mockito.`when`(itemPurchaseRepository.getItemByPurchaseAndProduct(1, 1)).thenReturn(item)

        val itemResult = itemPurchaseUseCase.getItemByPurchaseAndProduct(1,1)

        Mockito.verify(itemPurchaseRepository, Mockito.times(1)).getItemByPurchaseAndProduct(1,1)
        Mockito.verifyNoMoreInteractions(itemPurchaseRepository)

        assertThat(itemResult).isNull()
    }

    @Test
    fun delete_Success() = runTest {
        val item = ItemPurchaseFactory.createItemPurchase(1, 1)

        Mockito.`when`(itemPurchaseRepository.delete(item)).thenReturn(Unit)

        itemPurchaseUseCase.delete(item)

        Mockito.verify(itemPurchaseRepository, Mockito.times(1)).delete(item)
        Mockito.verifyNoMoreInteractions(itemPurchaseRepository)
    }

    @Test
    fun insert_Success() = runTest {
        val captor: ArgumentCaptor<ItemPurchase> = ArgumentCaptor.forClass(ItemPurchase::class.java)

        val product = ProductFactory.createProduct(500)
        val requestQuantity = 15

        itemPurchaseUseCase.insert(1, product, requestQuantity)

        Mockito.verify(itemPurchaseRepository).insert(MockitoUtil.captureArgument(captor))

        val itemCaptured = captor.value

        assertThat(itemCaptured).isNotNull()
        assertThat(itemCaptured.purchaseId).isEqualTo(1)
        assertThat(itemCaptured.prodId).isEqualTo(product.id)
        assertThat(itemCaptured.prodDesc).isEqualTo(product.description)
        assertThat(itemCaptured.prodPrice).isEqualTo(product.amount)
        assertThat(itemCaptured.prodUrl).isEqualTo(product.imageUrl)
        assertThat(itemCaptured.quantity).isEqualTo(requestQuantity)
    }

    @Test
    fun updateQuantity_Success() = runTest {
        val captor: ArgumentCaptor<ItemPurchase> = ArgumentCaptor.forClass(ItemPurchase::class.java)

        val itemPurchase = ItemPurchaseFactory.createItemPurchase(1, 500)
        val requestQuantity = 30

        itemPurchaseUseCase.updateQuantity(itemPurchase, requestQuantity)

        Mockito.verify(itemPurchaseRepository).update(MockitoUtil.captureArgument(captor))

        val itemCaptured = captor.value

        assertThat(itemCaptured).isNotNull()
        assertThat(itemCaptured.purchaseId).isEqualTo(itemPurchase.purchaseId)
        assertThat(itemCaptured.prodId).isEqualTo(itemPurchase.prodId)
        assertThat(itemCaptured.prodDesc).isEqualTo(itemPurchase.prodDesc)
        assertThat(itemCaptured.prodPrice).isEqualTo(itemPurchase.prodPrice)
        assertThat(itemCaptured.prodUrl).isEqualTo(itemPurchase.prodUrl)
        assertThat(itemCaptured.quantity).isEqualTo(requestQuantity)
    }

    @Test
    fun handleDelete_Success() = runTest {
        val item = ItemPurchaseFactory.createItemPurchase(1, 1, 0)

        itemPurchaseUseCase.updateOrDeleteItemBasedOnQuantity(item)

        Mockito.verify(itemPurchaseRepository, Mockito.times(1)).delete(item)
        Mockito.verifyNoMoreInteractions(itemPurchaseRepository)
    }

    @Test
    fun handleUpdate_Success() = runTest {
        val item = ItemPurchaseFactory.createItemPurchase(1, 1, 15)

        itemPurchaseUseCase.updateOrDeleteItemBasedOnQuantity(item)

        Mockito.verify(itemPurchaseRepository, Mockito.times(1)).update(item)
        Mockito.verifyNoMoreInteractions(itemPurchaseRepository)
    }
}