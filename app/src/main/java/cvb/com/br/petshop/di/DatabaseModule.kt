package cvb.com.br.petshop.di

import android.content.Context
import androidx.room.Room
import cvb.com.br.petshop.data.datasource.local.LocalItemPurchaseDataSource
import cvb.com.br.petshop.data.datasource.local.LocalProductDataSource
import cvb.com.br.petshop.data.datasource.local.LocalPurchaseDataSource
import cvb.com.br.petshop.data.repository.ItemPurchaseRepository
import cvb.com.br.petshop.data.repository.ProductRepository
import cvb.com.br.petshop.data.repository.PurchaseRepository
import cvb.com.br.petshop.db.AppDataBase
import cvb.com.br.petshop.db.dao.ItemPurchaseDao
import cvb.com.br.petshop.db.dao.ProductDao
import cvb.com.br.petshop.db.dao.PurchaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDataBase {
        return Room.databaseBuilder(
            appContext,
            AppDataBase::class.java,
            "PetShop"
        ).addCallback(AppDataBase.databaseCallback).build()
    }

    //==============

    @Provides
    fun providesProductDao(appDatabase: AppDataBase): ProductDao {
        return appDatabase.productDao()
    }

    @Provides
    fun providesPurchaseDao(appDatabase: AppDataBase): PurchaseDao {
        return appDatabase.purchaseDao()
    }

    @Provides
    fun providesItemPurchaseDao(appDatabase: AppDataBase): ItemPurchaseDao {
        return appDatabase.itemPurchaseDao()
    }

    //==============

    @ProductLocalRepository
    @Singleton
    @Provides
    fun providesLocalProductDataSource(productDao: ProductDao): ProductRepository = LocalProductDataSource(productDao)

    @PurchaseLocalRepository
    @Singleton
    @Provides
    fun providesLocalPurchaseDataSource(purchaseDao: PurchaseDao): PurchaseRepository = LocalPurchaseDataSource(purchaseDao)

    @ItemPurchaseLocalRepository
    @Singleton
    @Provides
    fun providesLocalItemPurchaseDataSource(itemPurchaseDao: ItemPurchaseDao): ItemPurchaseRepository = LocalItemPurchaseDataSource(itemPurchaseDao)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProductLocalRepository

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PurchaseLocalRepository

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ItemPurchaseLocalRepository





