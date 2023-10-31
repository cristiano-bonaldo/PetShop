package cvb.com.br.petshop.di

import android.content.Context
import androidx.room.Room
import cvb.com.br.petshop.data.datasource.local.LocalItemPurchaseDataSource
import cvb.com.br.petshop.data.datasource.local.LocalProductDataSource
import cvb.com.br.petshop.data.datasource.local.LocalPurchaseDataSource
import cvb.com.br.petshop.data.datasource.remote.RemoteProductDataSource
import cvb.com.br.petshop.data.db.AppDataBase
import cvb.com.br.petshop.data.db.dao.ItemPurchaseDao
import cvb.com.br.petshop.data.db.dao.ProductDao
import cvb.com.br.petshop.data.db.dao.PurchaseDao
import cvb.com.br.petshop.data.repository.ItemPurchaseRepositoryImpl
import cvb.com.br.petshop.data.repository.ProductRepositoryImpl
import cvb.com.br.petshop.data.repository.PurchaseRepositoryImpl
import cvb.com.br.petshop.domain.repository.ItemPurchaseRepository
import cvb.com.br.petshop.domain.repository.ProductRepository
import cvb.com.br.petshop.domain.repository.PurchaseRepository
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

    @ProductRepositoryImplementation
    @Singleton
    @Provides
    fun providesProductRepositoryImpl(
        remoteProductDataSource: RemoteProductDataSource,
        localProductDataSource: LocalProductDataSource,
    ): ProductRepository =
        ProductRepositoryImpl(remoteProductDataSource, localProductDataSource)

    @PurchaseRepositoryImplementation
    @Singleton
    @Provides
    fun providesPurchaseRepositoryImpl(localPurchaseDataSource: LocalPurchaseDataSource): PurchaseRepository =
        PurchaseRepositoryImpl(localPurchaseDataSource)

    @ItemPurchaseRepositoryImplementation
    @Singleton
    @Provides
    fun providesItemPurchaseRepositoryImpl(localItemPurchaseDataSource: LocalItemPurchaseDataSource): ItemPurchaseRepository =
        ItemPurchaseRepositoryImpl(localItemPurchaseDataSource)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProductRepositoryImplementation

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PurchaseRepositoryImplementation

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ItemPurchaseRepositoryImplementation





