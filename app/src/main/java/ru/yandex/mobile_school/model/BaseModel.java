package ru.yandex.mobile_school.model;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import ru.yandex.mobile_school.App;
import ru.yandex.mobile_school.Const;
import ru.yandex.mobile_school.model.api.ApiInterface;

public class BaseModel {
    private final ObservableTransformer schedulersTransformer;

    @Inject
    protected ApiInterface apiInterface;

    @Inject
    @Named(Const.UI_THREAD)
    protected Scheduler uiThread;

    @Inject
    @Named(Const.IO_THREAD)
    protected Scheduler ioThread;

    public BaseModel() {
        App.getComponent().inject(this);
        schedulersTransformer = o -> ((Observable) o).subscribeOn(ioThread)
                .observeOn(uiThread)
                .unsubscribeOn(ioThread);
    }

    @SuppressWarnings("unchecked")
    protected  <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }
}
