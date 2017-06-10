package ru.yandex.mobile_school.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.yandex.mobile_school.Const;
import ru.yandex.mobile_school.model.api.ApiInterface;
import ru.yandex.mobile_school.model.api.ServiceGenerator;

@Module
public class ModelModule {

    @Provides
    @Singleton
    ApiInterface provideApiInterface() {
        return ServiceGenerator.getApiInterface(Const.BASE_URL);
    }

    @Provides
    @Singleton
    @Named(Const.UI_THREAD)
    Scheduler provideShedulerUI() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @Named(Const.IO_THREAD)
    Scheduler provideSchedulerIO() {
        return Schedulers.io();
    }
}
