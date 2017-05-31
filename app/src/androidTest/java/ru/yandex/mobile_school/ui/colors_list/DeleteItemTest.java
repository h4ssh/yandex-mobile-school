package ru.yandex.mobile_school.ui.colors_list;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.ui.colors_list.helpers.RecyclerViewItemCountAssertion;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeleteItemTest {

	@Rule
	public ActivityTestRule<ColorsListActivity> mActivityTestRule =
			new ActivityTestRule<>(ColorsListActivity.class);

	@Test
	public void deleteItemTest() {

		onView(allOf(withContentDescription("Open menu"),
				withParent(allOf(withId(R.id.action_bar),
						withParent(withId(R.id.action_bar_container)))),
						isDisplayed()))
				.perform(click());

		onView(allOf(withId(R.id.design_menu_item_text), withText("Download from server"), isDisplayed()))
				.perform(click());

		onView(allOf(withId(android.R.id.button1), withText("OK")))
				.perform(scrollTo(), click());

		RecyclerView recyclerView = (RecyclerView)
				mActivityTestRule.getActivity().findViewById(R.id.colors_list_view);
		int itemsCount = recyclerView.getAdapter().getItemCount();

		onView(withId(R.id.colors_list_view))
				.perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

		onView(allOf(withId(android.R.id.button1), withText("Delete")))
				.perform(scrollTo(), click());

		onView(withId(R.id.colors_list_view)).check(new RecyclerViewItemCountAssertion(itemsCount - 1));
	}

}
