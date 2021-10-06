package org.sco.movieratings.di

import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PicassoModule {

    @Provides
    fun providePicasso(): Picasso? {
        return Picasso.get()
    }
}