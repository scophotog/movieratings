package org.sco.movieratings.mocked

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.Interceptor
import org.sco.movieratings.api.ApiKeyModule
import org.sco.movieratings.api.BaseUrlModule
import org.sco.movieratings.api.ImagePathUrlModule
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [BaseUrlModule::class, ApiKeyModule::class, ImagePathUrlModule::class])
class FakeBaseUrlModule {
    @Provides
    fun provideUrl(): String = "http://localhost:8080"

    @Provides
    @Named("api_key")
    fun provideNullApiKey(): Interceptor? = null

    @Provides
    @Singleton
    @Named("image_path")
    fun imagePath() = "http://localhost:8080"
}