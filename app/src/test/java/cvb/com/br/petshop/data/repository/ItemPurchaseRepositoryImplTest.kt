package cvb.com.br.petshop.data.repository

import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.data.util.ItemPurchaseFactory
import cvb.com.br.petshop.data.util.PurchaseFactory
import cvb.com.br.petshop.domain.datasource.ItemPurchaseDataSource
import cvb.com.br.petshop.domain.model.ItemPurchase
import cvb.com.br.petshop.domain.model.Purchase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ItemPurchaseRepositoryImplTest {

    @Mock
    lateinit var itemPurchaseDataSource: ItemPurchaseDataSource

    @InjectMocks
    lateinit var itemPurchaseRepositoryImpl: ItemPurchaseRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun update_Success() = runTest {
        val item = ItemPurchaseFactory.createItemPurchase(1, 1)

        itemPurchaseRepositoryImpl.update(item)

        Mockito.verify(itemPurchaseDataSource, Mockito.times(1)).update(item)
        Mockito.verifyNoMoreInteractions(itemPurchaseDataSource)
    }

    @Test
    fun insert_Success() = runTest {
        val item = ItemPurchaseFactory.createItemPurchase(1, 1)

        itemPurchaseRepositoryImpl.insert(item)

        Mockito.verify(itemPurchaseDataSource, Mockito.times(1)).insert(item)
        Mockito.verifyNoMoreInteractions(itemPurchaseDataSource)
    }

    @Test
    fun delete_Success() = runTest {
        val item = ItemPurchaseFactory.createItemPurchase(1, 1)

        itemPurchaseRepositoryImpl.delete(item)

        Mockito.verify(itemPurchaseDataSource, Mockito.times(1)).delete(item)
        Mockito.verifyNoMoreInteractions(itemPurchaseDataSource)
    }

    @Test
    fun getItemsByPurchase_WithData_Success() = runTest {
        val item1 = ItemPurchaseFactory.createItemPurchase(1, 1)
        val item2 = ItemPurchaseFactory.createItemPurchase(1, 2)
        val item3 = ItemPurchaseFactory.createItemPurchase(1, 3)

        val list = listOf(item1, item2, item3)

        Mockito.`when`(itemPurchaseDataSource.getItemsByPurchase(1)).thenReturn(list)

        val listResult = itemPurchaseRepositoryImpl.getItemsByPurchase(1)

        Mockito.verify(itemPurchaseDataSource, Mockito.times(1)).getItemsByPurchase(1)
        Mockito.verifyNoMoreInteractions(itemPurchaseDataSource)

        assertThat(listResult.size).isEqualTo(list.size)
        assertThat(listResult).contains(item1)
        assertThat(listResult).contains(item2)
        assertThat(listResult).contains(item3)
    }

    @Test
    fun getItemsByPurchase_WithNoData_Success() = runTest {
        val list = listOf<ItemPurchase>()

        Mockito.`when`(itemPurchaseDataSource.getItemsByPurchase(1)).thenReturn(list)

        val listResult = itemPurchaseRepositoryImpl.getItemsByPurchase(1)

        Mockito.verify(itemPurchaseDataSource, Mockito.times(1)).getItemsByPurchase(1)
        Mockito.verifyNoMoreInteractions(itemPurchaseDataSource)

        assertThat(listResult).isEmpty()
    }

    @Test
    fun getItemByPurchaseAndProduct_WithData_Success() = runTest {
        val item = ItemPurchaseFactory.createItemPurchase(1, 1)

        Mockito.`when`(itemPurchaseDataSource.getItemByPurchaseAndProduct(1, 1)).thenReturn(item)

        val itemResult = itemPurchaseRepositoryImpl.getItemByPurchaseAndProduct(1 ,1)

        Mockito.verify(itemPurchaseDataSource, Mockito.times(1)).getItemByPurchaseAndProduct(1, 1)
        Mockito.verifyNoMoreInteractions(itemPurchaseDataSource)

        assertThat(itemResult).isNotNull()
        assertThat(itemResult).isEqualTo(item)
    }

    @Test
    fun getItemByPurchaseAndProduct_WithNoData_Success() = runTest {
        val item: ItemPurchase? = null

        Mockito.`when`(itemPurchaseDataSource.getItemByPurchaseAndProduct(1, 1)).thenReturn(item)

        val itemResult = itemPurchaseRepositoryImpl.getItemByPurchaseAndProduct(1 ,1)

        Mockito.verify(itemPurchaseDataSource, Mockito.times(1)).getItemByPurchaseAndProduct(1, 1)
        Mockito.verifyNoMoreInteractions(itemPurchaseDataSource)

        assertThat(itemResult).isNull()
    }
}
