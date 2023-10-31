package cvb.com.br.petshop.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import cvb.com.br.petshop.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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






