package ru.yandex.mobile_school.views.notes_list;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.views.notes_list.helpers.RecyclerViewItemCountAssertion;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GenerateItemsTest {

	@Rule
	public ActivityTestRule<NotesActivity> mActivityTestRule =
			new ActivityTestRule<>(NotesActivity.class);

	@Test
	public void generate5ItemsTest() {

		RecyclerView recyclerView = (RecyclerView)
				mActivityTestRule.getActivity().findViewById(R.id.notes_list_view);
		int itemsCount = recyclerView.getAdapter().getItemCount();

		ViewInteraction floatingActionButton = onView(
				allOf(withId(R.id.notes_list_fab), isDisplayed()));
		floatingActionButton.perform(longClick());

		ViewInteraction appCompatEditText = onView(
				allOf(withId(R.id.colors_list_generate_edit), isDisplayed()));
		appCompatEditText.perform(click());

		ViewInteraction appCompatEditText2 = onView(
				allOf(withId(R.id.colors_list_generate_edit), isDisplayed()));
		appCompatEditText2.perform(replaceText("5"), closeSoftKeyboard());

		ViewInteraction appCompatButton = onView(
				allOf(withId(android.R.id.button1), withText("OK")));
		appCompatButton.perform(scrollTo(), click());

		onView(withId(R.id.notes_list_view)).check(new RecyclerViewItemCountAssertion(itemsCount + 5));
	}

}
