package ru.yandex.mobile_school.views;

/**
 * Created by hash on 10/06/2017.
 */

public interface BaseActivityCallback {
    void setTitle(String title);

    void showProgressBar();

    void hideProgressBar();

    void showError(String error);

    void showDialog(String title, String message);

    void showErrorDialog(String error);
}
