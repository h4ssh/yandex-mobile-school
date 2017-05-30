package ru.yandex.mobile_school;

import org.junit.Test;

import java.util.Date;

import ru.yandex.mobile_school.utils.DateFilter;
import ru.yandex.mobile_school.utils.DateUtils;

import static junit.framework.Assert.assertEquals;

/**
 * Created by hash on 29/05/2017.
 */

public class DateUtilsUnitTest {

	@Test
	public void DateFilterTest() throws Exception {
		String paramName = "TEST";
		Date now = new Date();
		String filterString = DateUtils.getFilterString(paramName, now, now);
		DateFilter dateFilter = DateUtils.getDateFilter(filterString);
		assertEquals(dateFilter.getParamName(), paramName);
	}
}
