package ru.yandex.mobile_school.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application app;

    public AppModule(Application app) { this.app = app; }

    @Singleton
    @Provides
    Context provideContext() { return app; }
}
