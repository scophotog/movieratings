package org.sco.movieratings;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sco.movieratings.activity.MovieActivity;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.fragment.MovieFragment;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MovieActivityTest {

    @Rule
    public ActivityTestRule<MovieActivity> activityRule =
            new ActivityTestRule<MovieActivity>(MovieActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, MovieActivity.class);
                    Movie movie = makeMovie();
                    result.putExtra(MovieFragment.MOVIE, (Parcelable) movie);
                    return result;
                }
            };

    @Test
    public void showMovieTest() {
        onView(withId(R.id.movie_title)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_title)).check(matches(withText("Foo Bar Title")));
    }

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
