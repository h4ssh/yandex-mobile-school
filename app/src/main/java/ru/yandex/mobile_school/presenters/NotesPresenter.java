package ru.yandex.mobile_school.presenters;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.yandex.mobile_school.App;
import ru.yandex.mobile_school.data.ColorItem;
import ru.yandex.mobile_school.model.NotesModel;
import ru.yandex.mobile_school.model.dto.NoteDTO;
import ru.yandex.mobile_school.model.dto.RequestNotesDTO;
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

}
