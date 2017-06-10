package ru.yandex.mobile_school.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import ru.yandex.mobile_school.model.NotesModel;

/**
 * Created by hash on 10/06/2017.
 */

@Module
public class PresenterModule {

    @Provides
    CompositeDisposable provideCompositeSubscription() {
        return new CompositeDisposable();
    }

    @Provides
    @Singleton
    NotesModel provideNotes() { return new NotesModel(); }
}
