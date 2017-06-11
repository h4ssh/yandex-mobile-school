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
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by hash on 31/05/2017.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ViewItemTest {

	@Rule
	public ActivityTestRule<NotesActivity> mActivityTestRule =
			new ActivityTestRule<>(NotesActivity.class);

	@Test
	public void viewItemTest() {
		onView(allOf(withContentDescription("Open menu"),
				withParent(allOf(withId(R.id.action_bar),
						withParent(withId(R.id.action_bar_container)))),
				isDisplayed()))
				.perform(click());

		onView(allOf(withId(R.id.design_menu_item_text),
				withText("Download from server"), isDisplayed()))
				.perform(click());

		onView(allOf(withId(android.R.id.button1), withText("OK")))
				.perform(scrollTo(), click());

		ViewInteraction recyclerView = onView(withId(R.id.notes_list_view));
		recyclerView.perform(actionOnItemAtPosition(0, click()));

		pressBack();

		ViewInteraction recyclerView2 = onView(withId(R.id.notes_list_view));
		recyclerView2.perform(actionOnItemAtPosition(0, click()));

		ViewInteraction actionMenuItemView = onView(
				allOf(withId(R.id.color_fragment_menu_done),
						withContentDescription("Done"), isDisplayed()));
		actionMenuItemView.perform(click());
	}
}
