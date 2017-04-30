package ru.yandex.mobile_school.ui.colors_list;

import android.support.v4.app.Fragment;

import ru.yandex.mobile_school.ui.base.SingleFragmentActivity;


public class ColorsListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ColorsListFragment.newInstance();
	}
}
