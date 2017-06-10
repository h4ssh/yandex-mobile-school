package ru.yandex.mobile_school.presenters;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.yandex.mobile_school.App;
import ru.yandex.mobile_school.views.BaseFragment;
import ru.yandex.mobile_school.views.IView;

/**
 * Created by hash on 10/06/2017.
 */

public abstract class BasePresenter implements IBasePresenter {
    @Inject
    CompositeDisposable compositeDisposable;

    BasePresenter() {
        App.getComponent().inject(this);
    }

    void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected abstract IView getView();

    protected abstract void destroyView();

    protected void showLoadingState(Disposable disposable) {
        getView().showLoading();
    }

    void hideLoadingState() {
        getView().hideLoading();
    }

    void showError(Throwable e) {
        getView().showError(e.getMessage());
    }

    void showDialog(String title, String message){
        ((BaseFragment)getView()).showDialog(title, message);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        destroyView();
    }
}

