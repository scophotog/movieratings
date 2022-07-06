package org.sco.movieratings.moviedetails

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object MovieFragmentModule {

    @Provides
    @FragmentScoped
    fun providePreviewAdapter() = MoviePreviewAdapter()

    @Provides
    @FragmentScoped
    fun provideReviewAdapter() = MovieReviewAdapter()

    @Provides
    @FragmentScoped
    fun provideMoviePresenter(
        previewAdapter: MoviePreviewAdapter,
        reviewAdapter: MovieReviewAdapter
    ) =
        MoviePresenter(previewAdapter, reviewAdapter)
}