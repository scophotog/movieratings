package org.sco.movieratings.movielist

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Named

@Module
@InstallIn(FragmentComponent::class)
object MovieListFragmentModule {

    @Provides
    @FragmentScoped
    fun provideMovieListAdapter(@Named("image_path") imagePath: String) =
        MovieListAdapter(imagePath)
}