package ru.yandex.mobile_school.presenters;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.yandex.mobile_school.App;
import ru.yandex.mobile_school.data.ColorItem;
import ru.yandex.mobile_school.model.NotesModel;
import ru.yandex.mobile_school.model.dto.NoteDTO;
import ru.yandex.mobile_school.model.dto.PostNoteDTO;
import ru.yandex.mobile_school.model.dto.RemoveNoteDTO;
import ru.yandex.mobile_school.model.dto.RequestNotesDTO;
import ru.yandex.mobile_school.model.dto.UpdateNoteDTO;
import ru.yandex.mobile_school.ui.colors_list.NotesFragment;
import ru.yandex.mobile_school.views.IView;

public class NotesPresenter extends BasePresenter {
    private NotesFragment view;

    @Inject
    NotesModel model;

    public NotesPresenter() {
        App.getComponent().inject(this);
    }

    public void onCreate(NotesFragment view) { this.view = view; }

    @Override
    protected IView getView() {
        return view;
    }

    @Override
    protected void destroyView() {
        view = null;
    }

    public void requestNotes(int userId) {
        Disposable subscription = model.requestNotes(userId)
                .doOnSubscribe(this::showLoadingState)
                .filter(p -> { p.setUserId(userId); return true; })
                .subscribe(this::handleReceiveNotes, this::showError, this::hideLoadingState);

        addDisposable(subscription);
    }

    private void handleReceiveNotes(RequestNotesDTO notes) {
        ArrayList<ColorItem> items = new ArrayList<>(notes.getData().size());
        for (NoteDTO note: notes.getData()) {
            items.add(new ColorItem(note));
        }
        view.onGetUserNotes(notes.getUserId(), items);
    }

    public void addNote(int userId, UUID itemId, NoteDTO note) {
        Disposable subscription = model.postNote(userId, note)
                .doOnSubscribe(this::showLoadingState)
                .filter(p -> { p.setUserId(userId); return true; })
                .filter(p -> { p.setUuid(itemId.toString()); return true; })
                .subscribe(this::handleAddNote, this::showError, this::hideLoadingState);

        addDisposable(subscription);
    }

    private void handleAddNote(PostNoteDTO result) {
        view.onAddUserNote(result.getUserId(), UUID.fromString(result.getUuid()), result.getData());
    }

    public void updateNote(int userId, UUID itemId, int noteId, NoteDTO note) {
        Disposable subscription = model.updateNote(userId, noteId, note)
                .doOnSubscribe(this::showLoadingState)
                .subscribe(this::handleUpdateNote, this::showError, this::hideLoadingState);

        addDisposable(subscription);
    }

    private void handleUpdateNote(UpdateNoteDTO result) {
    }

    public void deleteNote(int userId, int noteId) {
        Disposable subscription = model.removeNote(userId, noteId)
                .doOnSubscribe(this::showLoadingState)
                .subscribe(this::handleDeleteNote, this::showError, this::hideLoadingState);

        addDisposable(subscription);
    }

    private void handleDeleteNote(RemoveNoteDTO result) {
    }
}
