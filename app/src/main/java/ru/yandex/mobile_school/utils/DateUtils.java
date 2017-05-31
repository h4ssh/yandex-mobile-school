package ru.yandex.mobile_school.utils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

	private static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

	public static String getCurrentDateString() {
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat(ISO8601_FORMAT, Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getDefault());
		return dateFormat.format(now);
	}

	public static String dateToDateString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(ISO8601_FORMAT, Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getDefault());
		return dateFormat.format(date);
	}

	public static Date parseDateString(String dateString) {
		SimpleDateFormat format = new SimpleDateFormat(ISO8601_FORMAT);
		try {
			return format.parse(dateString);
		} catch (Exception ignored) {
			return null;
		}
	}

	public static String getFilterString(String paramName, Date startDate, Date endDate) {
		DateFilter filter = new DateFilter(paramName, startDate, endDate);
		Moshi moshi = new Moshi.Builder().build();
		JsonAdapter<DateFilter> jsonAdapter = moshi.adapter(DateFilter.class);
		return jsonAdapter.toJson(filter);
	}

	public static DateFilter getDateFilter(String json) {
		Moshi moshi = new Moshi.Builder().build();
		JsonAdapter<DateFilter> jsonAdapter = moshi.adapter(DateFilter.class);
		try {
			return jsonAdapter.fromJson(json);
		} catch (IOException ignored) {
			return null;
		}
	}
}
