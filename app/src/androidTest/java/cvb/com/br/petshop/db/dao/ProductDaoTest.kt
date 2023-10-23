package cvb.com.br.petshop.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import cvb.com.br.petshop.db.AppDataBase
import cvb.com.br.petshop.db.dao.util.EntityProductFactory
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ProductDaoTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDataBase

    private lateinit var productDao: ProductDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()

        productDao = db.productDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertProduct() = runTest {
        val entityProduct = EntityProductFactory.createProduct(1)

        productDao.insert(entityProduct)

        val list = productDao.getAll()

        val productFromList = list[0]

        assertThat(productFromList).isEqualTo(entityProduct)

        assertThat(productFromList.id).isEqualTo(entityProduct.id)
        assertThat(productFromList.description).isEqualTo(entityProduct.description)
        assertThat(productFromList.weight).isEqualTo(entityProduct.weight)
        assertThat(productFromList.quantity).isEqualTo(entityProduct.quantity)
        assertThat(productFromList.imageUrl).isEqualTo(entityProduct.imageUrl)
        assertThat(productFromList.createdAt).isEqualTo(entityProduct.createdAt)
    }

    @Test
    fun getProductList() = runTest {
        val entityProduct1 = EntityProductFactory.createProduct(1)
        val entityProduct2 = EntityProductFactory.createProduct(2)
        val entityProduct3 = EntityProductFactory.createProduct(3)

        val listTest = arrayListOf(entityProduct1, entityProduct2, entityProduct3)

        productDao.insert(entityProduct1)
        productDao.insert(entityProduct2)
        productDao.insert(entityProduct3)

        val listFromDB = productDao.getAll()

        assertThat(listFromDB.size).isEqualTo(listTest.size)
        assertThat(listFromDB).containsExactly(entityProduct1, entityProduct2, entityProduct3)
    }
}

