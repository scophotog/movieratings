package org.sco.movieratings;

import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sco.movieratings.activity.MainActivity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class ExampleRobolectricTest {

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
        final String appName = getApplicationContext().getString(R.string.app_name);
        final String screenTitle = getApplicationContext().getString(R.string.my_favorites_settings);
        final String expectedResult = String.format("%s %s", appName, screenTitle);
        onView(withId(R.id.bn_my_favorites)).check(matches(isDisplayed()));
        onView(withId(R.id.bn_my_favorites)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withId(R.id.toolbar))))
                .check(matches(withText(expectedResult)));
    }

}
