package ru.yandex.mobile_school.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ru.yandex.mobile_school.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {

	protected abstract Fragment createFragment();

	@LayoutRes
	protected int getLayoutResId() {
		return R.layout.activity_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResId());
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_container);
		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction()
					.add(R.id.fragment_container, fragment)
					.commit();
		}
		if (savedInstanceState != null)
			restoreFragmentState(fragment);
	}

	protected void restoreFragmentState(Fragment fragment) {
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
