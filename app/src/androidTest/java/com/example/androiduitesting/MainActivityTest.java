package com.example.androiduitesting;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before public void setup() { Intents.init(); }
    @After  public void teardown() { Intents.release(); }

    private void addCityAndVerifyInList(String name) {
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name))
                .perform(replaceText(name), closeSoftKeyboard());
        onView(withId(R.id.button_confirm)).perform(click());
        // assert it's actually in the list before we click it
        onView(withText(name)).check(matches(isDisplayed()));
    }

    @Test
    public void switchesToShowActivity() {
        addCityAndVerifyInList("Edmonton");
        // click the first row; avoids any IME/layout flakiness
        onData(anything()).inAdapterView(withId(R.id.city_list))
                .atPosition(0).perform(click());
        intended(hasComponent(ShowActivity.class.getName()));
    }

    @Test
    public void cityNameIsConsistent() {
        addCityAndVerifyInList("Tokyo");
        onData(anything()).inAdapterView(withId(R.id.city_list))
                .atPosition(0).perform(click());
        onView(withId(R.id.text_city_name)).check(matches(withText("Tokyo")));
    }

    @Test
    public void backButtonReturns() {
        addCityAndVerifyInList("Berlin");
        onData(anything()).inAdapterView(withId(R.id.city_list))
                .atPosition(0).perform(click());
        onView(withId(R.id.button_back)).perform(click());
        onView(withId(R.id.button_add)).check(matches(isDisplayed()));
    }
}
