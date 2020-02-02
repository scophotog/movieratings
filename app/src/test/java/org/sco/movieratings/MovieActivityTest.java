package org.sco.movieratings;

import org.sco.movieratings.api.models.Movie;

import java.util.ArrayList;


public class MovieActivityTest {

//    @Rule
//    public ActivityTestRule<MovieActivity> activityRule =
//            new ActivityTestRule<MovieActivity>(MovieActivity.class) {
//                @Override
//                protected Intent getActivityIntent() {
//                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//                    Intent result = new Intent(targetContext, MovieActivity.class);
//                    Movie movie = makeMovie();
//                    result.putExtra(MovieFragment.MOVIE, (Parcelable) movie);
//                    return result;
//                }
//            };
//
//    @Test
//    public void showMovieTest() {
//        onView(withId(R.id.movie_title)).check(matches(isDisplayed()));
//        onView(withId(R.id.movie_title)).check(matches(withText("Foo Bar Title")));
//    }

    private Movie makeMovie() {
        return new Movie("/",
                false,
                "Foo Two",
                "1/1/2000",
                new ArrayList<Integer>(),
                0,
                "Foo Bar",
                "English",
                "Foo Bar Title",
                "http://foo.bar",
                5.0,
                5,
                true,
                5.0);

    }
}
