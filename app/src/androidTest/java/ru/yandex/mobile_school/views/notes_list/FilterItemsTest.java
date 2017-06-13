package ru.yandex.mobile_school.views.notes_list;


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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FilterItemsTest {

	@Rule
	public ActivityTestRule<NotesActivity> mActivityTestRule =
			new ActivityTestRule<>(NotesActivity.class);

	@Test
	public void filterItemsTest() {
		ViewInteraction appCompatImageButton = onView(
				allOf(withContentDescription("Open menu"),
						withParent(allOf(withId(R.id.action_bar),
								withParent(withId(R.id.action_bar_container)))),
						isDisplayed()));
		appCompatImageButton.perform(click());

		ViewInteraction appCompatCheckedTextView = onView(
				allOf(withId(R.id.design_menu_item_text),
						withText("Download from server"), isDisplayed()));
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
				allOf(withId(R.id.design_menu_item_text), withText("Filter"), isDisplayed()));
		appCompatCheckedTextView2.perform(click());

		ViewInteraction appCompatCheckBox = onView(
				allOf(withId(R.id.notes_list_filter_start_date_check), withText("Start date:")));
		appCompatCheckBox.perform(scrollTo(), click());

		ViewInteraction appCompatCheckBox2 = onView(
				allOf(withId(R.id.notes_list_filter_end_date_check), withText("End date:")));
		appCompatCheckBox2.perform(scrollTo(), click());

		ViewInteraction appCompatButton2 = onView(
				allOf(withId(android.R.id.button1), withText("OK")));
		appCompatButton2.perform(scrollTo(), click());

	}

}
