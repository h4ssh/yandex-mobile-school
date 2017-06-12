package ru.yandex.mobile_school.presenters;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.yandex.mobile_school.App;
import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.model.NotesModel;
import ru.yandex.mobile_school.model.dto.NoteDTO;
import ru.yandex.mobile_school.model.dto.PostNoteDTO;
import ru.yandex.mobile_school.model.dto.RemoveNoteDTO;
import ru.yandex.mobile_school.model.dto.RequestNotesDTO;
import ru.yandex.mobile_school.model.StorageModel;
import ru.yandex.mobile_school.model.dto.UpdateNoteDTO;
import ru.yandex.mobile_school.views.notes_list.NotesFragment;
import ru.yandex.mobile_school.views.IView;

public class NotesPresenter extends BasePresenter {

    private NotesFragment view;

    @Inject
    NotesModel model;

    @Inject
    StorageModel storage;

    public NotesPresenter() {
        App.getComponent().inject(this);
    }

    public void onCreateView(NotesFragment view) {
        this.view = view;
    }

    @Override
    protected IView getView() {
        return view;
    }

    @Override
    protected void destroyView() {
        view = null;
    }

    // region API

    public void requestNotes(int userId) {
        Disposable subscription = model.requestNotes(userId)
                .doOnSubscribe(this::showLoadingState)
                .subscribe(this::handleReceiveNotes, this::showError, this::hideLoadingState);

        addDisposable(subscription);
    }

    private void handleReceiveNotes(RequestNotesDTO notes) {
        ArrayList<Note> items = new ArrayList<>(notes.getData().size());
        for (NoteDTO note : notes.getData()) {
            items.add(new Note(note));
        }
        storage.replaceNotes(items);
        view.onGetUserNotes(items);
    }

    public void postNote(UUID itemId, NoteDTO note) {
        Disposable subscription = model.postNote(storage.getUserId(), note)
                .doOnSubscribe(this::showLoadingState)
                .filter(p -> {
                    p.setUuid(itemId.toString());
                    return true;
                })
                .subscribe(this::handlePostNote, this::showError, this::hideLoadingState);

        addDisposable(subscription);
    }

    private void handlePostNote(PostNoteDTO result) {
        Note note = storage.getNote(UUID.fromString(result.getUuid()));
        note.setServerId(result.getData());
        updateLocalNote(note);
        view.onAddUserNote(UUID.fromString(result.getUuid()), result.getData());
    }

    public void updateNote(int noteId, NoteDTO note) {
        Disposable subscription = model.updateNote(storage.getUserId(), noteId, note)
                .doOnSubscribe(this::showLoadingState)
                .subscribe(this::handleUpdateNote, this::showError, this::hideLoadingState);

        addDisposable(subscription);
    }

    private void handleUpdateNote(UpdateNoteDTO result) {
    }

    public void deleteNote(int noteId) {
        Disposable subscription = model.removeNote(storage.getUserId(), noteId)
                .doOnSubscribe(this::showLoadingState)
                .subscribe(this::handleDeleteNote, this::showError, this::hideLoadingState);

        addDisposable(subscription);
    }

    private void handleDeleteNote(RemoveNoteDTO result) {
    }

    // endregion API

    // region Storage

    public ArrayList<Note> getLocalNotes() {
        return storage.getNotes();
    }

    public void deleteLocalNote(Note note) {
        storage.deleteNote(note);
    }

    public void updateLocalNote(Note note) {
        storage.updateNote(note);
    }

    public void addLocalNote(Note note) {
        storage.addNote(note);
    }

    public Note getNote(UUID id) {
        return storage.getNote(id);
    }

    public void setUserId(int userId) {
        storage.setUserId(userId);
    }

    public int getUserId() { return storage.getUserId(); }

    // endregion Storage
}
