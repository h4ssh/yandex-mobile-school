package ru.yandex.mobile_school.api;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import ru.yandex.mobile_school.data.ColorItem;

public class NotesAPIClient {

	public interface NotesAPICallbacks {
		void onGetUserNotes(int user, List<ColorItem> items);
		void onAddUserNote(int user, UUID id, int serverId);
		void onError(String message);
	}

	private static NotesAPIClient INSTANCE;
	private NotesAPI mNotesAPI;

	private static final String BASE_URL = "https://notesbackend-yufimtsev.rhcloud.com/";
	private static final String STATUS_OK = "ok";
	private static final String ERROR = "unknown error";

	public static NotesAPIClient get() {
		if (INSTANCE == null) {
			INSTANCE = new NotesAPIClient();
		}
		return INSTANCE;
	}

	private NotesAPIClient() {
		Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL)
				.addConverterFactory(MoshiConverterFactory.create());
		Retrofit retrofit = builder.build();
		mNotesAPI = retrofit.create(NotesAPI.class);
	}

	public void getUserNotes(final int userId, final NotesAPICallbacks callbacks) {
		Call<ResponseNotes> call = mNotesAPI.getUserNotes(userId);
		call.enqueue(new Callback<ResponseNotes>() {
			@Override
			public void onResponse(@NonNull Call<ResponseNotes> call, @NonNull Response<ResponseNotes> response) {
				ResponseNotes responseNotes = response.body();
				if (responseNotes != null && responseNotes.status.equals(STATUS_OK)) {
					ArrayList<ColorItem> results = new ArrayList<>();
					for (Note note: responseNotes.data) {
						results.add(new ColorItem(note));
					}
					if (callbacks != null) {
						callbacks.onGetUserNotes(userId, results);
					}
				} else {
					if (callbacks != null) {
						callbacks.onError(ERROR);
					}
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseNotes> call, @NonNull Throwable t) {
				if (callbacks != null) {
					callbacks.onError(t.getMessage());
				}
			}
		});
	}

	public void addUserNote(final int userId, final UUID noteId, Note note,
							final NotesAPICallbacks callbacks) {
		Call<ResponseAddNote> call = mNotesAPI.addUserNote(userId, note);
		call.enqueue(new Callback<ResponseAddNote>() {
			@Override
			public void onResponse(Call<ResponseAddNote> call, Response<ResponseAddNote> response) {
				ResponseAddNote responseAddNote = response.body();
				if (responseAddNote != null && responseAddNote.status.equals(STATUS_OK)) {
					if (callbacks != null) {
						callbacks.onAddUserNote(userId, noteId, responseAddNote.data);
					}
				} else {
					if (callbacks != null) {
						callbacks.onError(ERROR);
					}
				}
			}

			@Override
			public void onFailure(Call<ResponseAddNote> call, Throwable t) {
				if (callbacks != null) {
					callbacks.onError(t.getMessage());
				}
			}
		});
	}
}
