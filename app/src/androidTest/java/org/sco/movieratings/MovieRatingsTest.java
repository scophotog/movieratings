package org.sco.movieratings;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sco.movieratings.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MovieRatingsTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void viewTopRated() {
        onView(withId(R.id.bn_top_rated)).check(matches(isDisplayed()));
        onView(withId(R.id.bn_top_rated)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withId(R.id.toolbar))))
                .check(matches(withText("Movie Ratings Top Rated")));
    }

    @Test
    public void viewMostPopular() {
        onView(withId(R.id.bn_most_popular)).check(matches(isDisplayed()));
        onView(withId(R.id.bn_most_popular)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withId(R.id.toolbar))))
                .check(matches(withText("Movie Ratings Most Popular")));
    }

    @Test
    public void viewFavorites() {
        onView(withId(R.id.bn_my_favorites)).check(matches(isDisplayed()));
        onView(withId(R.id.bn_my_favorites)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withId(R.id.toolbar))))
                .check(matches(withText("Movie Ratings Favorites")));
    }

    @Test
    public void addFavorite() {
        onView(withId(R.id.bn_top_rated)).perform(click());
        onView(withId(R.id.movie_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.mark_as_favorite)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.bn_my_favorites)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withId(R.id.toolbar))))
                .check(matches(withText("Movie Ratings Favorites")));
        onView(withId(R.id.moviePoster)).check(matches(isDisplayed()));

        onView(withId(R.id.movie_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.mark_as_favorite)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.bn_my_favorites)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withId(R.id.toolbar))))
                .check(matches(withText("Movie Ratings Favorites")));
        onView(withId(R.id.empty_view)).check(matches(withText("No movies :(")));
    }
}
