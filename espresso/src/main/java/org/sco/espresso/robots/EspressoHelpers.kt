package org.sco.espresso.robots

import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf

fun clickElementWithId(@IdRes id: Int) {
    Espresso.onView(ViewMatchers.withId(id)).perform(ViewActions.click())
}

fun clickItemInList(@IdRes listId: Int, position: Int) {
    Espresso.onView(ViewMatchers.withId(listId)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            position,
            ViewActions.click()
        )
    )
}

fun assertTextInElement(@IdRes id: Int, test: String) {
    Espresso.onView(ViewMatchers.withId(id))
        .check(ViewAssertions.matches(ViewMatchers.withText(test)))
}

fun assertElementIsDisplayed(@IdRes id: Int) {
    Espresso.onView(ViewMatchers.withId(id))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

fun assertTitleBarTextMatches(@IdRes id: Int, text: String) {
    Espresso.onView(
        AllOf.allOf(
            CoreMatchers.instanceOf(
                TextView::class.java
            ),
            ViewMatchers.withParent(ViewMatchers.withId(id))
        )
    )
        .check(ViewAssertions.matches(ViewMatchers.withText(text)))
}


