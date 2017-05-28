package ru.yandex.mobile_school.ui.colors_list;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.yandex.mobile_school.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddColorTest {

	@Rule
	public ActivityTestRule<ColorsListActivity> mActivityTestRule = new ActivityTestRule<>(ColorsListActivity.class);

	@Test
	public void addColorTest() {
		ViewInteraction floatingActionButton = onView(
				allOf(withId(R.id.colors_list_fab), isDisplayed()));
		floatingActionButton.perform(click());

		ViewInteraction appCompatEditText = onView(
				allOf(withId(R.id.color_fragment_title), isDisplayed()));
		appCompatEditText.perform(replaceText("Test"), closeSoftKeyboard());

		ViewInteraction appCompatEditText2 = onView(
				allOf(withId(R.id.color_fragment_description), isDisplayed()));
		appCompatEditText2.perform(replaceText("testing"), closeSoftKeyboard());

		ViewInteraction actionMenuItemView = onView(
				allOf(withId(R.id.color_fragment_menu_done), withContentDescription("Done"), isDisplayed()));
		actionMenuItemView.perform(click());

	}

}
