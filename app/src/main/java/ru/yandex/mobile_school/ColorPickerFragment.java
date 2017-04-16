package ru.yandex.mobile_school;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColorPickerFragment extends Fragment {
	private static final int COLOR_VIEW_SIZE_IN_DP = 50;
	private static final int COLOR_VIEW_MARGIN_IN_DP = 25;
	private static final int COLOR_VIEWS_COUNT = 16;

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

	private LinearLayout.LayoutParams defaultViewParams;
	private View.OnTouchListener defaultTouchListener = new ViewColorTouchListener();

	static ColorPickerFragment newInstance() {
		return new ColorPickerFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_color_picker, container, false);
		ButterKnife.bind(this,view);

		setCurrentColorDescription(mCurrentColorView.getCurrentColor());

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, COLOR_VIEW_SIZE_IN_DP, metrics);
		int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, COLOR_VIEW_MARGIN_IN_DP, metrics);
		defaultViewParams = new LinearLayout.LayoutParams(size, size);
		defaultViewParams.setMargins(margin, margin, margin, margin);

		float colorInterval = 360f/ COLOR_VIEWS_COUNT;
		for (int i = 0; i < COLOR_VIEWS_COUNT; i++) {

			final ColorView colorView = newColorView(colorInterval / 2 + (colorInterval * i));
			colorView.setOnTouchListener(defaultTouchListener);
			mColorScrollLayout.addView(colorView, defaultViewParams);
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
						parent.removeView(v);
						return true;
					}
				});
				mFavoriteScrollLayout.addView(view, defaultViewParams);
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

	static ColorView newColorView(float hue) {
		return new ColorView(YMSApplication.getAppContext(), hue);
	}

	static ColorView newColorView(@ColorInt int color) {
		return  new ColorView(YMSApplication.getAppContext(), color);
	}

	private class ViewColorTouchListener implements View.OnTouchListener {
		private static final float HUE_SENSIVITY = 0.2f;
		private static final float VALUE_SENSIVITY = 0.003f;

		private ColorView mColorView;

		private GestureDetector gestureDetector =
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

}
