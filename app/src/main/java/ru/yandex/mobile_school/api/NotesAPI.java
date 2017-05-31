package ru.yandex.mobile_school.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotesAPI {

	@GET("user/{user_id}/notes")
	Call<ResponseNotes> getUserNotes(@Path("user_id") int userId);

	@GET("user/{user_id}/note/{note_id}")
	Call<ResponseNote> getUserNote(@Path("user_id") int userId, @Path("note_id") int noteId);

	@POST("user/{user_id}/notes")
	Call<ResponseAddNote> addUserNote(@Path("user_id") int userId, @Body Note note);

	@POST("user/{user_id}/note/{note_id}")
	Call<ResponseBase> updateUserNote(@Path("user_id") int userId, @Path("note_id") int noteId, @Body Note note);

	@DELETE("user/{user_id}/note/{note_id}")
	Call<ResponseBase> deleteUserNote(@Path("user_id") int userId, @Path("note_id") int noteId);

}
