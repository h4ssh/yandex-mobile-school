package ru.yandex.mobile_school;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

interface IColorPicker{
	void onColorPicked(@ColorInt int color, String title, String descr);
}

public class ColorPickerFragment extends Fragment {

	private static final String ARG_COLOR = "color";
	private static final String ARG_TITLE = "title";
	private static final String ARG_DESCR = "description";

	private static final int COLOR_VIEW_SIZE_IN_DP = 50;
	private static final int COLOR_VIEW_MARGIN_IN_DP = 25;
	private static final int COLOR_VIEW_FAVORITE_MARGIN_IN_DP = 8;
	private static final int COLOR_VIEWS_COUNT = 16;

	@BindView(R.id.color_fragment_title) EditText mTitleEdit;
	@BindView(R.id.color_fragment_description) EditText mDescriptionEdit;
	@BindView(R.id.colors_scroll_view) LockableHorizontalScrollView mColorScroll;
	@BindView(R.id.favorite_scroll_layout) LinearLayout mFavoriteScrollLayout;
	@BindView(R.id.colors_scroll_layout) LinearLayout mColorScrollLayout;
	@BindView(R.id.current_color_view) ColorView mCurrentColorView;
	@BindView(R.id.curent_color_r) TextView mCurrentColorR;
	@BindView(R.id.curent_color_g) TextView mCurrentColorG;
	@BindView(R.id.curent_color_b) TextView mCurrentColorB;
	@BindView(R.id.curent_color_h) TextView mCurrentColorH;
	@BindView(R.id.curent_color_s) TextView mCurrentColorS;
	@BindView(R.id.curent_color_v) TextView mCurrentColorV;

	private static final ArrayList<Integer> FAVORITE_COLORS = new ArrayList<>();
	private IColorPicker mDelegate;
	private LinearLayout.LayoutParams defaultViewParams;
	private LinearLayout.LayoutParams favoriteViewParams;
	private final View.OnTouchListener defaultTouchListener = new ViewColorTouchListener();

	static ColorPickerFragment newInstance(@ColorInt int color, @Nullable String title,
										   @Nullable String descr, @Nullable IColorPicker delegate) {
		Bundle args = new Bundle();
		args.putInt(ARG_COLOR, color);
		if (title != null) args.putString(ARG_TITLE, title);
		if (descr != null) args.putString(ARG_DESCR, descr);
		ColorPickerFragment fragment = new ColorPickerFragment();
		fragment.setArguments(args);
		fragment.setDelegate(delegate);
		return fragment;
	}

	private void setDelegate (IColorPicker delegate) {
		mDelegate = delegate;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_color_picker, container, false);
		ButterKnife.bind(this,view);

		Bundle arguments = getArguments();
		if (arguments != null) {
			if (arguments.containsKey(ARG_TITLE))
				mTitleEdit.setText(arguments.getString(ARG_TITLE));
			if (arguments.containsKey(ARG_DESCR))
				mDescriptionEdit.setText(arguments.getString(ARG_DESCR));
			if (arguments.containsKey(ARG_COLOR))
				mCurrentColorView.setCurrentColor(arguments.getInt(ARG_COLOR));
		}

		setCurrentColorDescription(mCurrentColorView.getCurrentColor());

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, COLOR_VIEW_SIZE_IN_DP, metrics);
		int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, COLOR_VIEW_MARGIN_IN_DP, metrics);
		int favMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, COLOR_VIEW_FAVORITE_MARGIN_IN_DP, metrics);
		defaultViewParams = new LinearLayout.LayoutParams(size, size);
		defaultViewParams.setMargins(margin, margin, margin, margin);
		favoriteViewParams = new LinearLayout.LayoutParams(size, size);
		favoriteViewParams.setMargins(0, 0, favMargin, 0);

		float colorInterval = 360f/ COLOR_VIEWS_COUNT;
		for (int i = 0; i < COLOR_VIEWS_COUNT; i++) {

			final ColorView colorView = newColorView(colorInterval / 2 + (colorInterval * i));
			colorView.setOnTouchListener(defaultTouchListener);
			mColorScrollLayout.addView(colorView, defaultViewParams);
		}

		for (Integer color: FAVORITE_COLORS) {
			ColorView favorite = newColorView(color);
			mFavoriteScrollLayout.addView(favorite, defaultViewParams);
		}

		mCurrentColorView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				final ColorView view = newColorView(mCurrentColorView.getCurrentColor());
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						@ColorInt int pickedColor = view.getCurrentColor();
						mCurrentColorView.setCurrentColor(pickedColor);
						setCurrentColorDescription(pickedColor);
					}
				});
				view.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						ViewGroup parent = (ViewGroup) v.getParent();
						FAVORITE_COLORS.remove((Integer)((ColorView)v).getCurrentColor());
						parent.removeView(v);
						return true;
					}
				});
				mFavoriteScrollLayout.addView(view, favoriteViewParams);
				FAVORITE_COLORS.add(view.getCurrentColor());
				return true;
			}
		});

		return view;
	}

	private void setCurrentColorDescription(@ColorInt int currentColor) {
		mCurrentColorR.setText(getResources().getString(R.string.color_description_r, Color.red(currentColor)));
		mCurrentColorG.setText(getResources().getString(R.string.color_description_g, Color.green(currentColor)));
		mCurrentColorB.setText(getResources().getString(R.string.color_description_b, Color.blue(currentColor)));
		float [] hsv = new float[] {0,0,0};
		Color.colorToHSV(currentColor, hsv);
		mCurrentColorH.setText(getResources().getString(R.string.color_description_h, hsv[0]));
		mCurrentColorS.setText(getResources().getString(R.string.color_description_s, hsv[1]));
		mCurrentColorV.setText(getResources().getString(R.string.color_description_v, hsv[2]));
	}

	private static ColorView newColorView(float hue) {
		return new ColorView(YMSApplication.getContext(), hue);
	}

	private static ColorView newColorView(@ColorInt int color) {
		return  new ColorView(YMSApplication.getContext(), color);
	}

	private class ViewColorTouchListener implements View.OnTouchListener {
		private static final float HUE_SENSIVITY = 0.2f;
		private static final float VALUE_SENSIVITY = 0.003f;

		private ColorView mColorView;

		private final GestureDetector gestureDetector =
				new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

					@Override
					public void onLongPress(MotionEvent e) {
						Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
						vibrator.vibrate(100);
						mColorScroll.setScrollingEnabled(false);
						mColorView.setOnTouchListener(new View.OnTouchListener() {
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								int index = event.getActionIndex();
								int pointerId = event.getPointerId(index);

								VelocityTracker	veloTracker = VelocityTracker.obtain();

								if (event.getAction() == MotionEvent.ACTION_MOVE) {
									veloTracker.addMovement(event);
									veloTracker.computeCurrentVelocity(10);
									float deltaX = VelocityTrackerCompat.getXVelocity(veloTracker, pointerId);
									float deltaY = 	VelocityTrackerCompat.getYVelocity(veloTracker,	pointerId);
									mColorView.variateColor(deltaX * HUE_SENSIVITY, -deltaY * VALUE_SENSIVITY);
								}
								else if (event.getAction() == MotionEvent.ACTION_UP) {
									mColorScroll.setScrollingEnabled(true);
									mColorView.setOnTouchListener(defaultTouchListener);
								}
								veloTracker.recycle();
								return true;
							}
						});
					}

					@Override
					public boolean onSingleTapConfirmed(MotionEvent e) {
						@ColorInt int pickedColor = mColorView.getCurrentColor();
						mCurrentColorView.setCurrentColor(pickedColor);
						setCurrentColorDescription(pickedColor);
						return true;
					}

					@Override
					public boolean onDoubleTap(MotionEvent e) {
						mColorView.resetColor();
						return true;
					}
				});

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v instanceof ColorView) mColorView = (ColorView) v;
			gestureDetector.onTouchEvent(event);
			return true;
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.color_picker_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.color_fragment_menu_done:
				if (mDelegate != null)
					mDelegate.onColorPicked(mCurrentColorView.getCurrentColor(),
							mTitleEdit.getText().toString(), mDescriptionEdit.getText().toString());
				break;
		}
		return super.onOptionsItemSelected(item);
	}


}
