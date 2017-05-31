package ru.yandex.mobile_school.ui.base;

import android.support.v4.app.Fragment;

import ru.yandex.mobile_school.R;

public abstract class BaseFragment extends Fragment {

	protected boolean onBackPressed() {
		return false;
	}

	public int getMenuResId() {
		return R.menu.fragment_menu;
	}
}
