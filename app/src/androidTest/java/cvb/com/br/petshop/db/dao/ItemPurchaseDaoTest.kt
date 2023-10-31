package cvb.com.br.petshop.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.data.db.AppDataBase
import cvb.com.br.petshop.data.db.dao.ItemPurchaseDao
import cvb.com.br.petshop.db.dao.util.EntityItemPurchaseFactory
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ItemPurchaseDaoTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDataBase

    private lateinit var itemPurchaseDao: ItemPurchaseDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()

        itemPurchaseDao = db.itemPurchaseDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertItemPurchase() = runTest {
        val purchaseId = 1L
        val productId = 1L

        val entityItemPurchase = EntityItemPurchaseFactory.createItemPurchase(purchaseId, productId)

        itemPurchaseDao.insert(entityItemPurchase)

        val entityItemPurchaseFromDB = itemPurchaseDao.getByPurchaseAndProduct(purchaseId, productId)

        assertThat(entityItemPurchaseFromDB).isEqualTo(entityItemPurchase)

        assertThat(entityItemPurchaseFromDB?.purchaseId).isEqualTo(entityItemPurchase.purchaseId)
        assertThat(entityItemPurchaseFromDB?.prodId).isEqualTo(entityItemPurchase.prodId)
        assertThat(entityItemPurchaseFromDB?.prodDesc).isEqualTo(entityItemPurchase.prodDesc)
        assertThat(entityItemPurchaseFromDB?.prodPrice).isEqualTo(entityItemPurchase.prodPrice)
        assertThat(entityItemPurchaseFromDB?.prodUrl).isEqualTo(entityItemPurchase.prodUrl)
        assertThat(entityItemPurchaseFromDB?.quantity).isEqualTo(entityItemPurchase.quantity)
    }

    @Test
    fun updateItemPurchase() = runTest {
        val purchaseId = 1L
        val productId = 1L

        val entityItemPurchase = EntityItemPurchaseFactory.createItemPurchase(purchaseId, productId)

        itemPurchaseDao.insert(entityItemPurchase)

        val entityItemPurchaseUpdate = entityItemPurchase.copy(
            prodDesc = "Produto-Teste-Update",
            prodPrice = 20.35,
            prodUrl = "https://petshop/imagem_url_teste_new",
            quantity = 12
        )

        itemPurchaseDao.update(entityItemPurchaseUpdate)

        val entityItemPurchaseFromDB = itemPurchaseDao.getByPurchaseAndProduct(purchaseId, productId)

        assertThat(entityItemPurchaseFromDB).isEqualTo(entityItemPurchaseUpdate)

        assertThat(entityItemPurchaseFromDB?.purchaseId).isEqualTo(entityItemPurchaseUpdate.purchaseId)
        assertThat(entityItemPurchaseFromDB?.prodId).isEqualTo(entityItemPurchaseUpdate.prodId)
        assertThat(entityItemPurchaseFromDB?.prodDesc).isEqualTo(entityItemPurchaseUpdate.prodDesc)
        assertThat(entityItemPurchaseFromDB?.prodPrice).isEqualTo(entityItemPurchaseUpdate.prodPrice)
        assertThat(entityItemPurchaseFromDB?.prodUrl).isEqualTo(entityItemPurchaseUpdate.prodUrl)
        assertThat(entityItemPurchaseFromDB?.quantity).isEqualTo(entityItemPurchaseUpdate.quantity)
    }

    @Test
    fun deleteItemPurchase() = runTest {
        val purchaseId = 1L
        val productId = 1L

        val entityItemPurchase = EntityItemPurchaseFactory.createItemPurchase(purchaseId, productId)

        itemPurchaseDao.insert(entityItemPurchase)

        var entityItemPurchaseFromDB = itemPurchaseDao.getByPurchaseAndProduct(purchaseId, productId)

        assertThat(entityItemPurchaseFromDB).isNotNull()

        itemPurchaseDao.delete(entityItemPurchase)

        entityItemPurchaseFromDB = itemPurchaseDao.getByPurchaseAndProduct(purchaseId, productId)

        assertThat(entityItemPurchaseFromDB).isNull()
    }

    @Test
    fun getItemPurchase() = runTest {
        val entityItemPurchase1 = EntityItemPurchaseFactory.createItemPurchase(1, 1)
        val entityItemPurchase2 = EntityItemPurchaseFactory.createItemPurchase(1, 2)
        val entityItemPurchase3 = EntityItemPurchaseFactory.createItemPurchase(1, 3)
        val entityItemPurchase4 = EntityItemPurchaseFactory.createItemPurchase(2, 1)
        val entityItemPurchase5 = EntityItemPurchaseFactory.createItemPurchase(2, 2)
        val entityItemPurchase6 = EntityItemPurchaseFactory.createItemPurchase(3, 1)

        itemPurchaseDao.insert(entityItemPurchase1)
        itemPurchaseDao.insert(entityItemPurchase2)
        itemPurchaseDao.insert(entityItemPurchase3)
        itemPurchaseDao.insert(entityItemPurchase4)
        itemPurchaseDao.insert(entityItemPurchase5)
        itemPurchaseDao.insert(entityItemPurchase6)

        val itemPurchaseFromDB = itemPurchaseDao.getByPurchaseAndProduct(2,2)

        assertThat(itemPurchaseFromDB).isEqualTo(entityItemPurchase5)
    }

    @Test
    fun getListItemPurchase() = runTest {
        val entityItemPurchase1 = EntityItemPurchaseFactory.createItemPurchase(1, 1)
        val entityItemPurchase2 = EntityItemPurchaseFactory.createItemPurchase(1, 2)
        val entityItemPurchase3 = EntityItemPurchaseFactory.createItemPurchase(1, 3)
        val entityItemPurchase4 = EntityItemPurchaseFactory.createItemPurchase(2, 1)
        val entityItemPurchase5 = EntityItemPurchaseFactory.createItemPurchase(2, 2)
        val entityItemPurchase6 = EntityItemPurchaseFactory.createItemPurchase(3, 1)

        itemPurchaseDao.insert(entityItemPurchase1)
        itemPurchaseDao.insert(entityItemPurchase2)
        itemPurchaseDao.insert(entityItemPurchase3)
        itemPurchaseDao.insert(entityItemPurchase4)
        itemPurchaseDao.insert(entityItemPurchase5)
        itemPurchaseDao.insert(entityItemPurchase6)

        val listFromDB = itemPurchaseDao.getByPurchase(2)

        assertThat(listFromDB.size).isEqualTo(2)
        assertThat(listFromDB).containsExactly(entityItemPurchase4, entityItemPurchase5)
    }
}

