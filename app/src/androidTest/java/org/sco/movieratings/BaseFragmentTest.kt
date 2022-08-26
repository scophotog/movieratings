package org.sco.movieratings

import androidx.navigation.Navigation
import androidx.test.espresso.IdlingRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

//    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    private lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var okHttp: OkHttpClient

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create("okhttp", okHttp))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    internal fun launchMovieListFragment() {
//        launchFragmentInHiltContainer<MovieListFragment> {
//            navController.setGraph(R.navigation.nav_graph)
//            navController.setCurrentDestination(R.id.movieListFragment)
//            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
//                if (viewLifecycleOwner != null) {
//                    Navigation.setViewNavController(this.requireView(), navController)
//                }
//            }
//        }
    }
}