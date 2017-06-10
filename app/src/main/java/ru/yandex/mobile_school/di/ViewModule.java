package ru.yandex.mobile_school.di;

import dagger.Module;
import dagger.Provides;
import ru.yandex.mobile_school.presenters.NotesPresenter;

@Module
public class ViewModule {

    @Provides
    NotesPresenter provideNotesPresenter() {
        return new NotesPresenter();
    }
}
