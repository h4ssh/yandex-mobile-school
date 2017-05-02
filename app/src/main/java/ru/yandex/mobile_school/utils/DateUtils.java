package ru.yandex.mobile_school.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

	private static final String ISO8601Format = ("yyyy-MM-dd'T'HH:mm:ss'Z'");

	public static String getCurrentDateString() {
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat(ISO8601Format, Locale.US);
		dateFormat.setTimeZone(TimeZone.getDefault());
		return dateFormat.format(now);
	}

	public static Date parseDateString(String dateString) {
		SimpleDateFormat format = new SimpleDateFormat(ISO8601Format);
		try {
			return format.parse(dateString);
		} catch (Exception ignored) {
			return null;
		}

	}
}
