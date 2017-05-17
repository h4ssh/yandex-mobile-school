package ru.yandex.mobile_school.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
	Call<ResponseBody> updateUserNote(@Path("user_id") int userId, @Path("note_id") int noteId, @Body RequestBody body);

	@DELETE("user/{user_id}/note/{note_id}")
	Call<ResponseBody> deleteUserNote(@Path("user_id") int userId, @Path("note_id") int noteId);

}
