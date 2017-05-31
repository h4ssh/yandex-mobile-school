package ru.yandex.mobile_school;

import org.junit.Test;

import java.util.Date;

import ru.yandex.mobile_school.utils.DateFilter;

import static junit.framework.Assert.assertEquals;

/**
 * Created by hash on 29/05/2017.
 */

public class DateFilterUnitTest {

	private static final String TEST_PARAM_NAME = "test";

	@Test
	public void getParamNameTest() {
		DateFilter dateFilter = new DateFilter(TEST_PARAM_NAME, new Date(), new Date());
		assertEquals(dateFilter.getParamName(), TEST_PARAM_NAME);
	}

	@Test
	public void matchTest() {
		DateFilter dateFilter = new DateFilter(TEST_PARAM_NAME, new Date(123456), new Date(345678));
		assertEquals(dateFilter.match(new Date(234567)), true);
	}
}
