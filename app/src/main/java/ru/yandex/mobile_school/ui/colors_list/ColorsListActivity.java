package ru.yandex.mobile_school.ui.colors_list;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.ui.base.BaseFragment;
import ru.yandex.mobile_school.ui.base.SingleFragmentActivity;

public class ColorsListActivity extends SingleFragmentActivity {

	@BindView(R.id.colors_list_drawer_layout) DrawerLayout mDrawerLayout;
	@BindView(R.id.colors_list_nav_view) NavigationView mNavigationView;
	@BindView(R.id.fragment_container) LinearLayout mFragmentContainer;

	private ActionBarDrawerToggle mToggle;

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_colors_list;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ButterKnife.bind(this);

		ActionBar bar = getSupportActionBar();
		if (bar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}
		mToggle = new ActionBarDrawerToggle(
				this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
		mDrawerLayout.addDrawerListener(mToggle);
		mToggle.syncState();
		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				mDrawerLayout.closeDrawers();
				Fragment child = getFragment();
				return child != null && child.onOptionsItemSelected(item);
			}
		});
		mNavigationView.inflateMenu(R.menu.colors_list_menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected Fragment createFragment() {
		return ColorsListFragment.newInstance();
	}


	public void setNavBarItemsEnabled(boolean enabled) {
		Menu menu = mNavigationView.getMenu();
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setEnabled(enabled);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
			mDrawerLayout.setScrimColor(Color.TRANSPARENT);
			mDrawerLayout.setFocusableInTouchMode(false);
			//mFragmentContainer.setPadding(mDrawerLayout.getWidth(), 0,0,0);
		} else {
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			mDrawerLayout.closeDrawers();
			//mFragmentContainer.setPadding(0,0,0,0);
			mDrawerLayout.setFocusableInTouchMode(true);
		}
	}
}
