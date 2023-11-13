package cvb.com.br.petshop.di

import cvb.com.br.petshop.data.datasource.local.LocalProductDataSource
import cvb.com.br.petshop.data.datasource.remote.RemoteProductDataSource
import cvb.com.br.petshop.data.db.dao.ProductDao
import cvb.com.br.petshop.data.network.interceptor.ApiInterceptor
import cvb.com.br.petshop.data.network.service.ApiService
import cvb.com.br.petshop.domain.datasource.ProductDataSource
import cvb.com.br.petshop.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        apiInterceptor: ApiInterceptor
    ) =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @ProductRemoteDataSource
    @Singleton
    @Provides
    fun providesProductRemoteDataSource(apiService: ApiService): ProductDataSource =
        RemoteProductDataSource(apiService)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProductRemoteDataSource

