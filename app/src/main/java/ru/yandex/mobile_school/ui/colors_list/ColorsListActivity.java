package ru.yandex.mobile_school.ui.colors_list;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.ui.base.SingleFragmentActivity;
import ru.yandex.mobile_school.ui.views.LockableDrawer;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class ColorsListActivity extends SingleFragmentActivity {

	private static final int DEFAULT_SCRIM_COLOR = 0x99000000;

	@BindView(R.id.colors_list_drawer_layout) LockableDrawer mDrawerLayout;
	@BindView(R.id.colors_list_nav_view) NavigationView mNavigationView;
	@BindView(R.id.fragment_container) LinearLayout mFragmentContainer;

	private ActionBarDrawerToggle mToggle;
	private int mDrawableLeftPadding;
	private int mDrawableTopPadding;
	private int mDrawableRightPadding;
	private int mDrawableBottomPadding;

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
		mDrawableLeftPadding = mDrawerLayout.getPaddingLeft();
		mDrawableTopPadding = mDrawerLayout.getPaddingTop();
		mDrawableRightPadding = mDrawerLayout.getPaddingRight();
		mDrawableBottomPadding = mDrawerLayout.getPaddingBottom();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
			setDrawerLandscape();
		} else {
			setDrawerPortrait();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START) &&
				mDrawerLayout.getDrawerLockMode(Gravity.START) !=
						DrawerLayout.LOCK_MODE_LOCKED_OPEN) {
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
		if (newConfig.orientation == ORIENTATION_LANDSCAPE) {
			setDrawerLandscape();
		} else {
			setDrawerPortrait();
		}
	}

	private void setDrawerPortrait() {
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		mDrawerLayout.setScrimColor(DEFAULT_SCRIM_COLOR);
		mDrawerLayout.closeDrawers();
		mFragmentContainer.setPadding(mDrawableLeftPadding, mDrawableTopPadding,
				mDrawableRightPadding, mDrawableBottomPadding);
	}

	private void setDrawerLandscape() {
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
		mDrawerLayout.setScrimColor(Color.TRANSPARENT);
		int width = mNavigationView.getWidth();
		if (width == 0) {
			mNavigationView.post(new Runnable() {
				@Override
				public void run() {
					mFragmentContainer.setPadding(mNavigationView.getWidth(), mDrawableTopPadding,
							mDrawableRightPadding, mDrawableBottomPadding);
				}
			});
		} else {
			mFragmentContainer.setPadding(width, mDrawableTopPadding,
					mDrawableRightPadding, mDrawableBottomPadding);
		}
	}
}
