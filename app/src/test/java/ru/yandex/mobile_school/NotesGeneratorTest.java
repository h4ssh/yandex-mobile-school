package ru.yandex.mobile_school;

import org.junit.Test;

import ru.yandex.mobile_school.model.NotesGenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;


public class NotesGeneratorTest {

	@Test
	public void dummyConstructorTest() throws Exception {
		NotesGenerator mGenerator = new NotesGenerator();
		assertThat(mGenerator, not(equalTo(null)));
	}

}
