package ru.yandex.mobile_school.ui.colors_list;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.ui.base.SingleFragmentActivity;
import ru.yandex.mobile_school.ui.color_picker.ColorPickerFragment;

public class ColorsListActivity extends SingleFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment createFragment() {
		return ColorsListFragment.newInstance();
	}
}
