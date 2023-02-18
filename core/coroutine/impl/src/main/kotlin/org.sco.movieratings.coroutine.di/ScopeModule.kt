package org.sco.movieratings.coroutine.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object ScopeModule {

    @Provides
    fun provideCoroutineScope(coroutineScopeManager: CoroutineScopeManager): CoroutineScope =
        coroutineScopeManager.coroutineScope
}