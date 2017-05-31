package ru.yandex.mobile_school.api;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import ru.yandex.mobile_school.data.ColorItem;

public class NotesAPIClient {

	public interface NotesAPICallbacks {
		void onGetUserNotes(int user, ArrayList<ColorItem> items);
		void onAddUserNote(int user, UUID itemId, int serverId);
		void onDeleteUserNote(int user, UUID itemId);
		void onUpdateUserNote(int user, UUID itemId);
		void onError(String message);
	}

	private static NotesAPIClient sInstance;
	private NotesAPI mNotesAPI;

	private static final String BASE_URL = "https://notesbackend-yufimtsev.rhcloud.com/";
	private static final String ERROR = "unknown error";

	public static NotesAPIClient get() {
		if (sInstance == null) {
			sInstance = new NotesAPIClient();
		}
		return sInstance;
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
			public void onResponse(@NonNull Call<ResponseNotes> call,
								   @NonNull Response<ResponseNotes> response) {
				ResponseNotes responseNotes = response.body();
				if (responseNotes != null && responseNotes.isSuccessful()) {
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

	public void addUserNote(final int userId, final UUID itemId, Note note,
							final NotesAPICallbacks callbacks) {
		Call<ResponseAddNote> call = mNotesAPI.addUserNote(userId, note);
		call.enqueue(new Callback<ResponseAddNote>() {
			@Override
			public void onResponse(Call<ResponseAddNote> call, Response<ResponseAddNote> response) {
				ResponseAddNote responseAddNote = response.body();
				if (responseAddNote != null && responseAddNote.isSuccessful()) {
					if (callbacks != null) {
						callbacks.onAddUserNote(userId, itemId, responseAddNote.data);
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

	public void deleteUserNote(final int userId, final UUID itemId, int noteId,
							   final NotesAPICallbacks callbacks) {
		Call<ResponseBase> call = mNotesAPI.deleteUserNote(userId, noteId);
		call.enqueue(new Callback<ResponseBase>() {
			@Override
			public void onResponse(Call<ResponseBase> call, Response<ResponseBase> response) {
				ResponseBase responseBase = response.body();
				if (responseBase != null & responseBase.isSuccessful()) {
					if (callbacks != null) {
						callbacks.onDeleteUserNote(userId, itemId);
					}
				} else {
					if (callbacks != null) {
						callbacks.onError(ERROR);
					}
				}
			}

			@Override
			public void onFailure(Call<ResponseBase> call, Throwable t) {
				if (callbacks != null) {
					callbacks.onError(ERROR);
				}
			}
		});
	}

	public void updateUserNote(final int userId, final UUID itemId, int noteId, Note note,
							   final NotesAPICallbacks callbacks) {
		Call<ResponseBase> call = mNotesAPI.updateUserNote(userId, noteId, note);
		call.enqueue(new Callback<ResponseBase>() {
			@Override
			public void onResponse(Call<ResponseBase> call, Response<ResponseBase> response) {
				ResponseBase responseBase = response.body();
				if (responseBase != null & responseBase.isSuccessful()) {
					if (callbacks != null) {
						callbacks.onUpdateUserNote(userId, itemId);
					}
				} else {
					if (callbacks != null) {
						callbacks.onError(ERROR);
					}
				}
			}

			@Override
			public void onFailure(Call<ResponseBase> call, Throwable t) {
				if (callbacks != null) {
					callbacks.onError(ERROR);
				}
			}
		});

	}
}
