package ru.yandex.mobile_school;

import android.graphics.Color;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import ru.yandex.mobile_school.data.ColorItem;

/**
 * Created by hash on 28/05/2017.
 */

public class ColorItemUnitTest {

	private static final String TEST_TITLE = "Test title";
	private static final String TEST_DESCRIPTION = "Test description";

	@Test
	public void constructorTestSimple() throws Exception {
		ColorItem colorItem = new ColorItem(Color.WHITE, TEST_TITLE, TEST_DESCRIPTION);
		assertEquals(Color.WHITE, colorItem.getColor());
		assertEquals(TEST_TITLE, colorItem.getTitle());
		assertEquals(TEST_DESCRIPTION, colorItem.getDescription());
	}

	@Test
	public void constructorTestNullTitle() throws Exception {
		ColorItem colorItem = new ColorItem(Color.YELLOW, null, TEST_DESCRIPTION);
		assertEquals(Color.YELLOW, colorItem.getColor());
		assertEquals("", colorItem.getTitle());
		assertEquals(TEST_DESCRIPTION, colorItem.getDescription());
	}

	@Test
	public void constructorTestNullDescription() throws Exception {
		ColorItem colorItem = new ColorItem(Color.RED, TEST_TITLE, null);
		assertEquals(Color.RED, colorItem.getColor());
		assertEquals(TEST_TITLE, colorItem.getTitle());
		assertEquals("", colorItem.getDescription());
	}

	@Test
	public void updateWithTest() throws Exception {
		ColorItem initial = new ColorItem(Color.RED, TEST_TITLE, TEST_DESCRIPTION);
		ColorItem updated = new ColorItem(Color.BLUE, null, null);
		initial.updateWith(updated);
		assertThat(initial.getId(), not(equalTo(updated.getId())));
		assertThat(initial.getColor(), equalTo(updated.getColor()));
		assertThat(initial.getTitle(), equalTo(updated.getTitle()));
		assertThat(initial.getCreated(), equalTo(updated.getCreated()));
		assertThat(initial.getEdited(), equalTo(updated.getEdited()));
		assertThat(initial.getViewed(), equalTo(updated.getViewed()));
	}
}
