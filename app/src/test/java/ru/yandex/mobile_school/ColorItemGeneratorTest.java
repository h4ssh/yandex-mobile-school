package ru.yandex.mobile_school;

import org.junit.Test;

import ru.yandex.mobile_school.data.ColorItemGenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by hash on 29/05/2017.
 */

public class ColorItemGeneratorTest {

	@Test
	public void dummyConstructorTest() throws Exception{
		ColorItemGenerator mGenerator = new ColorItemGenerator();
		assertThat(mGenerator, not(equalTo(null)));
	}

}
