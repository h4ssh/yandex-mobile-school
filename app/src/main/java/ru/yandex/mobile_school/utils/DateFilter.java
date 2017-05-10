package ru.yandex.mobile_school.utils;

import java.util.Date;

public class DateFilter {
	private String mParamName;
	private String mStartDate;
	private String mEndDate;

	public DateFilter(String paramName, Date startDate, Date endDate) {
		mParamName = paramName;
		if (startDate != null) mStartDate = DateUtils.dateToDateString(startDate);
		if (endDate != null) mEndDate = DateUtils.dateToDateString(endDate);
	}

	public String getParamName() {
		return mParamName;
	}

	public boolean match(Date date) {
		return (mStartDate == null || DateUtils.parseDateString(mStartDate).before(date))
				&& (mEndDate == null || DateUtils.parseDateString(mEndDate).after(date));
	}
}
