package ru.yandex.mobile_school.views.custom;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hash on 24/05/2017.
 */

public class LockableDrawer extends DrawerLayout {

	public LockableDrawer(Context context) {
		super(context);
	}

	public LockableDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LockableDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		View drawer = getChildAt(1);

		return !(getDrawerLockMode(drawer) == LOCK_MODE_LOCKED_OPEN &&
				ev.getRawX() > drawer.getWidth()) && super.onInterceptTouchEvent(ev);
	}

}
