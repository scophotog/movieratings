package org.sco.movieratings;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sco.movieratings.activity.MainActivity;

import androidx.test.espresso.DataInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FirstEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void hasToolbar() {
        onView(withId(R.id.toolbar));
    }

    /**
     * Uses {@link androidx.test.espresso.Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific row.
     * <p>
     * Note: A custom matcher can be used to match the content and have more readable code.
     * See the Custom Matcher Sample.
     * </p>
     *
     * @param str the content of the field
     * @return a {@link DataInteraction} referencing the row
     */
    private static DataInteraction onRow(String str) {
        return null;
//        return onData(hasEntry(equalTo(MainActivity.ROW_TEXT), is(str)));
    }

}
