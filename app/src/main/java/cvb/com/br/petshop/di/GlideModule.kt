package cvb.com.br.petshop.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import cvb.com.br.petshop.R
import cvb.com.br.petshop.data.datasource.remote.RemoteProductDataSource
import cvb.com.br.petshop.data.repository.ProductRepository
import cvb.com.br.petshop.network.interceptor.ApiInterceptor
import cvb.com.br.petshop.network.service.ApiService
import cvb.com.br.petshop.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GlideModule {

    @Provides
    @Singleton
    fun provideGlide(@ApplicationContext appContext: Context) =
        Glide.with(appContext).setDefaultRequestOptions(
            RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_glide_place_holder)
                .error(R.drawable.ic_glide_error)
        )
}






