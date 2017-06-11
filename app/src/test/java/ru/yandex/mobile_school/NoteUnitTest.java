package ru.yandex.mobile_school;

import android.graphics.Color;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import ru.yandex.mobile_school.model.Note;

/**
 * Created by hash on 28/05/2017.
 */

public class NoteUnitTest {

	private static final String TEST_TITLE = "Test title";
	private static final String TEST_DESCRIPTION = "Test description";

	@Test
	public void constructorTestSimple() throws Exception {
		Note note = new Note(Color.WHITE, TEST_TITLE, TEST_DESCRIPTION);
		assertEquals(Color.WHITE, note.getColor());
		assertEquals(TEST_TITLE, note.getTitle());
		assertEquals(TEST_DESCRIPTION, note.getDescription());
	}

	@Test
	public void constructorTestNullTitle() throws Exception {
		Note note = new Note(Color.YELLOW, null, TEST_DESCRIPTION);
		assertEquals(Color.YELLOW, note.getColor());
		assertEquals("", note.getTitle());
		assertEquals(TEST_DESCRIPTION, note.getDescription());
	}

	@Test
	public void constructorTestNullDescription() throws Exception {
		Note note = new Note(Color.RED, TEST_TITLE, null);
		assertEquals(Color.RED, note.getColor());
		assertEquals(TEST_TITLE, note.getTitle());
		assertEquals("", note.getDescription());
	}

	@Test
	public void updateWithTest() throws Exception {
		Note initial = new Note(Color.RED, TEST_TITLE, TEST_DESCRIPTION);
		Note updated = new Note(Color.BLUE, null, null);
		initial.updateWith(updated);
		assertThat(initial.getId(), not(equalTo(updated.getId())));
		assertThat(initial.getColor(), equalTo(updated.getColor()));
		assertThat(initial.getTitle(), equalTo(updated.getTitle()));
		assertThat(initial.getCreated(), equalTo(updated.getCreated()));
		assertThat(initial.getEdited(), equalTo(updated.getEdited()));
		assertThat(initial.getViewed(), equalTo(updated.getViewed()));
	}
}
