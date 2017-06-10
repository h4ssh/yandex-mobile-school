package ru.yandex.mobile_school.di;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by hash on 10/06/2017.
 */

@Module
public class PresenterModule {

    @Provides
    CompositeDisposable provideCompositeSubscription() {
        return new CompositeDisposable();
    }
}
