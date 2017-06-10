package ru.yandex.mobile_school.views;

/**
 * Created by hash on 10/06/2017.
 */

public interface IView {
    void showError(String error);

    void showLoading();

    void hideLoading();
}
