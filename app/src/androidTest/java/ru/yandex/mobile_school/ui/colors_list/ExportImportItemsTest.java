package ru.yandex.mobile_school.ui.colors_list;


import android.os.Build;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.ui.colors_list.helpers.RecyclerViewItemCountAssertion;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
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
public class ExportImportItemsTest {

	@Before
	public void grantPhonePermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			getInstrumentation().getUiAutomation().executeShellCommand(
					"pm grant " + getTargetContext().getPackageName() +
							" android.permission.READ_EXTERNAL_STORAGE");
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			getInstrumentation().getUiAutomation().executeShellCommand(
					"pm grant " + getTargetContext().getPackageName() +
							" android.permission.WRITE_EXTERNAL_STORAGE");
		}
	}

	@Rule
	public ActivityTestRule<ColorsListActivity> mActivityTestRule =
			new ActivityTestRule<>(ColorsListActivity.class);

	@Test
	public void exportImportItemsTest() {
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

		RecyclerView recyclerView = (RecyclerView)
				mActivityTestRule.getActivity().findViewById(R.id.colors_list_view);
		int itemsCount = recyclerView.getAdapter().getItemCount();

		ViewInteraction appCompatImageButton2 = onView(
				allOf(withContentDescription("Open menu"),
						withParent(allOf(withId(R.id.action_bar),
								withParent(withId(R.id.action_bar_container)))),
						isDisplayed()));
		appCompatImageButton2.perform(click());

		ViewInteraction appCompatCheckedTextView2 = onView(
				allOf(withId(R.id.design_menu_item_text), withText("Import\\Export"), isDisplayed()));
		appCompatCheckedTextView2.perform(click());

		ViewInteraction appCompatButton2 = onView(
				allOf(withId(android.R.id.button1), withText("Export")));
		appCompatButton2.perform(scrollTo(), click());

		ViewInteraction appCompatImageButton3 = onView(
				allOf(withContentDescription("Open menu"),
						withParent(allOf(withId(R.id.action_bar),
								withParent(withId(R.id.action_bar_container)))),
						isDisplayed()));
		appCompatImageButton3.perform(click());

		ViewInteraction appCompatCheckedTextView3 = onView(
				allOf(withId(R.id.design_menu_item_text), withText("Import\\Export"), isDisplayed()));
		appCompatCheckedTextView3.perform(click());

		ViewInteraction appCompatButton3 = onView(
				allOf(withId(android.R.id.button2), withText("Import")));
		appCompatButton3.perform(scrollTo(), click());

		onView(withId(R.id.colors_list_view)).check(new RecyclerViewItemCountAssertion(itemsCount));
	}

}
