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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchItemsTest {

	@Rule
	public ActivityTestRule<ColorsListActivity> mActivityTestRule = new ActivityTestRule<>(ColorsListActivity.class);

	@Test
	public void searchItemsTest() {
		ViewInteraction appCompatImageButton = onView(
				allOf(withContentDescription("Open menu"),
						withParent(allOf(withId(R.id.action_bar),
								withParent(withId(R.id.action_bar_container)))),
						isDisplayed()));
		appCompatImageButton.perform(click());

		ViewInteraction appCompatCheckedTextView = onView(
				allOf(withId(R.id.design_menu_item_text), withText("Download from server"), isDisplayed()));
		appCompatCheckedTextView.perform(click());

		ViewInteraction appCompatButton = onView(
				allOf(withId(android.R.id.button1), withText("OK")));
		appCompatButton.perform(scrollTo(), click());

		ViewInteraction appCompatImageButton2 = onView(
				allOf(withContentDescription("Open menu"),
						withParent(allOf(withId(R.id.action_bar),
								withParent(withId(R.id.action_bar_container)))),
						isDisplayed()));
		appCompatImageButton2.perform(click());

		ViewInteraction appCompatCheckedTextView2 = onView(
				allOf(withId(R.id.design_menu_item_text), withText("Search"), isDisplayed()));
		appCompatCheckedTextView2.perform(click());

		ViewInteraction appCompatEditText = onView(
				allOf(withId(R.id.colors_list_search_query), isDisplayed()));
		appCompatEditText.perform(click());

		ViewInteraction appCompatEditText2 = onView(
				allOf(withId(R.id.colors_list_search_query), isDisplayed()));
		appCompatEditText2.perform(replaceText("Test"), closeSoftKeyboard());

		ViewInteraction appCompatButton2 = onView(
				allOf(withId(android.R.id.button1), withText("Search")));
		appCompatButton2.perform(scrollTo(), click());

		ViewInteraction appCompatImageButton3 = onView(
				allOf(withContentDescription("Open menu"),
						withParent(allOf(withId(R.id.action_bar),
								withParent(withId(R.id.action_bar_container)))),
						isDisplayed()));
		appCompatImageButton3.perform(click());

		ViewInteraction appCompatCheckedTextView3 = onView(
				allOf(withId(R.id.design_menu_item_text), withText("Reset search\\filter"), isDisplayed()));
		appCompatCheckedTextView3.perform(click());

	}

}
