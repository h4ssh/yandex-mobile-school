package ru.yandex.mobile_school.model.api;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.yandex.mobile_school.model.dto.NoteDTO;
import ru.yandex.mobile_school.model.dto.PostNoteDTO;
import ru.yandex.mobile_school.model.dto.RemoveNoteDTO;
import ru.yandex.mobile_school.model.dto.RequestNotesDTO;
import ru.yandex.mobile_school.model.dto.UpdateNoteDTO;

public interface ApiInterface {

    @GET("user/{user_id}/notes")
    Observable<RequestNotesDTO> getUserNotes(@Path("user_id") int userId);

    @POST("user/{user_id}/notes")
    Observable<PostNoteDTO> addUserNote(@Path("user_id") int userId, @Body NoteDTO note);

    @POST("user/{user_id}/note/{note_id}")
    Observable<UpdateNoteDTO> updateUserNote(@Path("user_id") int userId, @Path("note_id") int noteId, @Body NoteDTO note);

    @DELETE("user/{user_id}/note/{note_id}")
    Observable<RemoveNoteDTO> removeUserNote(@Path("user_id") int userId, @Path("note_id") int noteId);
}
