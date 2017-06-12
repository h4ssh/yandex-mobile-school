package ru.yandex.mobile_school.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.yandex.mobile_school.model.BaseModel;
import ru.yandex.mobile_school.model.StorageModel;
import ru.yandex.mobile_school.presenters.BasePresenter;
import ru.yandex.mobile_school.presenters.NotesListLooperThread;
import ru.yandex.mobile_school.presenters.NotesPresenter;
import ru.yandex.mobile_school.views.notes_list.NotesFragment;
import ru.yandex.mobile_school.presenters.NotesListAsyncActor;

@SuppressWarnings("WeakerAccess")
@Singleton
@Component(modules = {AppModule.class, ModelModule.class, PresenterModule.class, ViewModule.class})
public interface AppComponent {

    void inject(BaseModel baseModel);

    void inject(BasePresenter basePresenter);

    void inject(NotesPresenter notesPresenter);

    void inject(NotesFragment notesFragment);

    void inject(StorageModel storage);

    void inject(NotesListAsyncActor notesListAsyncActor);

    void inject(NotesListLooperThread looperThread);
}
