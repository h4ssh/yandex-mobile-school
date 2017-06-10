package ru.yandex.mobile_school.model;

import io.reactivex.Observable;
import ru.yandex.mobile_school.model.dto.NoteDTO;
import ru.yandex.mobile_school.model.dto.PostNoteDTO;
import ru.yandex.mobile_school.model.dto.RemoveNoteDTO;
import ru.yandex.mobile_school.model.dto.RequestNotesDTO;
import ru.yandex.mobile_school.model.dto.UpdateNoteDTO;

/**
 * Created by hash on 10/06/2017.
 */

public class NotesModel extends BaseModel {

    public Observable<RequestNotesDTO> requestNotes(int userId) {
        return apiInterface
                .getUserNotes(userId)
                .compose(applySchedulers());
    }

    public Observable<UpdateNoteDTO> updateNote(int userId, int noteId, NoteDTO note) {
        return apiInterface
                .updateUserNote(userId, noteId, note)
                .compose(applySchedulers());
    }

    public Observable<RemoveNoteDTO> removeNote(int userId, int noteId) {
        return apiInterface
                .removeUserNote(userId, noteId)
                .compose(applySchedulers());
    }

    public Observable<PostNoteDTO> postNote(int userId, NoteDTO note) {
        return apiInterface
                .addUserNote(userId, note)
                .compose(applySchedulers());
    }
}
