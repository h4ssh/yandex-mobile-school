package ru.yandex.mobile_school.api;

public class ResponseBase {

	private static final String STATUS_OK = "ok";

	String status;

	public boolean isSuccessful() {
		return status.equals(STATUS_OK);
	}
}
