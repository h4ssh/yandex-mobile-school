package ru.yandex.mobile_school;

import android.app.Application;

import ru.yandex.mobile_school.di.AppComponent;
import ru.yandex.mobile_school.di.AppModule;
import ru.yandex.mobile_school.di.DaggerAppComponent;

public class App extends Application {
    private static AppComponent component;

    private static App appContext;

    public static AppComponent getComponent() {
        return component;
    }

    public static App getAppContext() { return appContext; }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = (App) getApplicationContext();
        component = buildComponent();
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }
}
