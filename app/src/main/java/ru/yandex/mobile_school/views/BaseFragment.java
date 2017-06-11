package ru.yandex.mobile_school.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.presenters.IBasePresenter;


public abstract class BaseFragment extends Fragment implements IView {

    protected BaseActivityCallback activityCallback;

    protected Unbinder unbinder;

    protected abstract IBasePresenter getPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityCallback = (BaseActivityCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement activityCallback");
        }
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }
    }

    @Override
    public void showLoading() {
        activityCallback.showProgressBar();
    }

    @Override
    public void hideLoading() {
        activityCallback.hideProgressBar();
    }

    @Override
    public void showError(String error) {
        activityCallback.showErrorDialog(error);
    }

    public void showDialog(String title, String message) {
        activityCallback.showDialog(title, message);
    }

    protected boolean onBackPressed() {
        return false;
    }

    public int getMenuResId() {
        return R.menu.fragment_menu;
    }
}
