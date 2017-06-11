package ru.yandex.mobile_school.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.views.BaseFragment;

public abstract class BaseActivity extends AppCompatActivity {

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
		if (savedInstanceState != null) {
			restoreFragmentState(fragment);
		}
	}

	public void replaceFragment(Fragment fragment) {
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.replace(R.id.fragment_container, fragment)
				.addToBackStack(null)
				.commit();
	}

	public void replaceFragmentWithShared(Fragment fragment, View shared, String sharedName) {
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction()
				.addSharedElement(shared, sharedName)
				.replace(R.id.fragment_container, fragment)
				.addToBackStack(null)
				.commit();
	}

	@Override
	public void onBackPressed() {
		Fragment currentFragment = getFragment();
		if (currentFragment instanceof BaseFragment) {
			if (!((BaseFragment) currentFragment).onBackPressed()) {
				super.onBackPressed();
			}
		}
	}

	public void replaceFragmentForResult() {

	}

	public void onFragmentResult(int requestCode, int result, Intent data) {

	}

	public Fragment getFragment() {
		return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
	}

	protected void restoreFragmentState(Fragment fragment) {
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
