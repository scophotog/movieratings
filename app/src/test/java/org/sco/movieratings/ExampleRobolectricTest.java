package org.sco.movieratings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.sco.movieratings.activity.MainActivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ExampleRobolectricTest {

    private MainActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(MainActivity.class).create().resume().get();
    }

    @Test
    public void viewTopRated() {
        assertThat(activity,is(not(null)));
        assertThat(activity.findViewById(R.id.bn_top_rated).isShown(),is(equalTo(true)));
//        onView(withId(R.id.bn_top_rated)).check(matches(isDisplayed()));
//        onView(withId(R.id.bn_top_rated)).perform(click());
//        onView(allOf(instanceOf(TextView.class),
//                withParent(withId(R.id.toolbar))))
//                .check(matches(withText("Movie Ratings Top Rated")));
    }
//
//    @Test
//    public void viewMostPopular() {
//        onView(withId(R.id.bn_most_popular)).check(matches(isDisplayed()));
//        onView(withId(R.id.bn_most_popular)).perform(click());
//        onView(allOf(instanceOf(TextView.class),
//                withParent(withId(R.id.toolbar))))
//                .check(matches(withText("Movie Ratings Most Popular")));
//    }
//
//    @Test
//    public void viewFavorites() {
//        final String appName = getApplicationContext().getString(R.string.app_name);
//        final String screenTitle = getApplicationContext().getString(R.string.my_favorites_settings);
//        final String expectedResult = String.format("%s %s", appName, screenTitle);
//        onView(withId(R.id.bn_my_favorites)).check(matches(isDisplayed()));
//        onView(withId(R.id.bn_my_favorites)).perform(click());
//        onView(allOf(instanceOf(TextView.class),
//                withParent(withId(R.id.toolbar))))
//                .check(matches(withText(expectedResult)));
//    }

}
