package cvb.com.br.petshop.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.data.enum.PurchaseStatusEnum
import cvb.com.br.petshop.db.AppDataBase
import cvb.com.br.petshop.db.dao.util.EntityPurchaseFactory
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class PurchaseDaoTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDataBase

    private lateinit var purchaseDao: PurchaseDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()

        purchaseDao = db.purchaseDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertPurchaseStatusOpen() = runTest {
        val entityPurchase = EntityPurchaseFactory.createPurchaseWithStatusOpen(1)

        purchaseDao.insert(entityPurchase)

        val entityPurchaseFromDB = purchaseDao.getPurchaseInProgress()

        assertThat(entityPurchaseFromDB).isEqualTo(entityPurchase)

        assertThat(entityPurchaseFromDB?.id).isEqualTo(entityPurchase.id)
        assertThat(entityPurchaseFromDB?.status).isEqualTo(entityPurchase.status)
        assertThat(entityPurchaseFromDB?.createdAt).isEqualTo(entityPurchase.createdAt)
        assertThat(entityPurchaseFromDB?.convertedAt).isEqualTo(entityPurchase.convertedAt)
    }

    @Test
    fun updatePurchaseSetClosed() = runTest {
        val entityPurchase = EntityPurchaseFactory.createPurchaseWithStatusOpen(1)

        purchaseDao.insert(entityPurchase)

        val entityPurchaseClosed = entityPurchase.copy(
            status = PurchaseStatusEnum.PURCHASE_STATUS_CLOSED.status,
            convertedAt = 1698002831000
        )

        purchaseDao.update(entityPurchaseClosed)

        val entityPurchaseFromDB = purchaseDao.getPurchaseInProgress()

        assertThat(entityPurchaseFromDB).isNull()
    }

    @Test
    fun getPurchaseInProgressIsEmpty() = runTest {
        val entityPurchaseClose1 = EntityPurchaseFactory.createPurchaseWithStatusClosed(1)
        val entityPurchaseClose2 = EntityPurchaseFactory.createPurchaseWithStatusClosed(2)
        val entityPurchaseClose3 = EntityPurchaseFactory.createPurchaseWithStatusClosed(3)

        purchaseDao.insert(entityPurchaseClose1)
        purchaseDao.insert(entityPurchaseClose2)
        purchaseDao.insert(entityPurchaseClose3)

        val listFromDB = purchaseDao.getPurchaseInProgress()

        assertThat(listFromDB).isNull()
    }

    @Test
    fun getPurchaseInProgressSuccess() = runTest {
        val entityPurchaseClose1 = EntityPurchaseFactory.createPurchaseWithStatusClosed(1)
        val entityPurchaseClose2 = EntityPurchaseFactory.createPurchaseWithStatusClosed(2)
        val entityPurchaseClose3 = EntityPurchaseFactory.createPurchaseWithStatusClosed(3)

        val entityPurchaseOpen = EntityPurchaseFactory.createPurchaseWithStatusOpen(4)

        purchaseDao.insert(entityPurchaseClose1)
        purchaseDao.insert(entityPurchaseClose2)
        purchaseDao.insert(entityPurchaseClose3)

        purchaseDao.insert(entityPurchaseOpen)

        val purchaseFromDB = purchaseDao.getPurchaseInProgress()

        assertThat(purchaseFromDB).isEqualTo(entityPurchaseOpen)

        assertThat(purchaseFromDB?.id).isEqualTo(entityPurchaseOpen.id)
        assertThat(purchaseFromDB?.status).isEqualTo(entityPurchaseOpen.status)
        assertThat(purchaseFromDB?.createdAt).isEqualTo(entityPurchaseOpen.createdAt)
        assertThat(purchaseFromDB?.convertedAt).isEqualTo(entityPurchaseOpen.convertedAt)
    }
}

