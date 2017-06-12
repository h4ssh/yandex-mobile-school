package ru.yandex.mobile_school.views.note_edit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.presenters.IBasePresenter;
import ru.yandex.mobile_school.views.custom.ColorView;
import ru.yandex.mobile_school.views.custom.LockableHorizontalScrollView;
import ru.yandex.mobile_school.views.custom.LockableScrollView;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.views.BaseFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class NoteEditFragment extends BaseFragment {

	public static final String EXTRA_COLOR_ITEM = "extra_color_item";

	private static final String ARG_COLOR_ITEM = "color_item";
	private static final String SAVED_COLOR_ITEM = "saved_color_item";
	private static final String DATE_FORMAT = "d MMM HH:mm:ss";

	private static final int COLOR_VIEW_SIZE_IN_DP = 50;
	private static final int COLOR_VIEW_MARGIN_IN_DP = 25;
	private static final int COLOR_VIEW_FAVORITE_MARGIN_IN_DP = 8;
	private static final int COLOR_VIEWS_COUNT = 16;
	private static final float COLOR_VIEWS_RANGE = 360f;

	private static final int INDEX_HUE = 0;
	private static final int INDEX_SAT = 1;
	private static final int INDEX_VAL = 2;

	@BindView(R.id.color_fragment_title) EditText mTitleEdit;
	@BindView(R.id.color_fragment_description) EditText mDescriptionEdit;
	@BindView(R.id.colors_scroll_view)	LockableHorizontalScrollView mColorScroll;
	@BindView(R.id.colors_picker_main_scroll) LockableScrollView mMainScroll;
	@BindView(R.id.favorite_scroll_layout) LinearLayout mFavoriteScrollLayout;
	@BindView(R.id.colors_scroll_layout) LinearLayout mColorScrollLayout;
	@BindView(R.id.current_color_view) ColorView mCurrentColorView;
	@BindView(R.id.curent_color_r) TextView mCurrentColorR;
	@BindView(R.id.curent_color_g) TextView mCurrentColorG;
	@BindView(R.id.curent_color_b) TextView mCurrentColorB;
	@BindView(R.id.curent_color_h) TextView mCurrentColorH;
	@BindView(R.id.curent_color_s) TextView mCurrentColorS;
	@BindView(R.id.curent_color_v) TextView mCurrentColorV;
	@BindView(R.id.color_picker_date_created) TextView mDateCreatedText;
	@BindView(R.id.color_picker_date_edited) TextView mDateEditedText;
	@BindView(R.id.color_picker_date_viewed) TextView mDateViewedText;

	private static final ArrayList<Integer> FAVORITE_COLORS = new ArrayList<>();
	private LinearLayout.LayoutParams defaultViewParams;
	private LinearLayout.LayoutParams favoriteViewParams;
	private final View.OnTouchListener defaultTouchListener = new ViewColorTouchListener();
	private Note mNote;

	public static NoteEditFragment newInstance(Note item) {
		NoteEditFragment fragment = new NoteEditFragment();
		if (item != null) {
			Bundle args = new Bundle();
			args.putParcelable(ARG_COLOR_ITEM, item);
			fragment.setArguments(args);
		}
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		postponeEnterTransition();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setSharedElementEnterTransition(TransitionInflater.from(getContext())
					.inflateTransition(android.R.transition.move));
		}
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note_edit, container, false);
		ButterKnife.bind(this, view);

		Bundle arguments = getArguments();
		if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_COLOR_ITEM)) {
			mNote = savedInstanceState.getParcelable(SAVED_COLOR_ITEM);
		} else if (arguments != null && arguments.containsKey(ARG_COLOR_ITEM)) {
			mNote = arguments.getParcelable(ARG_COLOR_ITEM);
			((AppCompatActivity) getActivity()).getSupportActionBar()
					.setTitle(R.string.note_edit_fragment_add_title);
		} else {
			mNote = new Note();
			((AppCompatActivity) getActivity()).getSupportActionBar()
					.setTitle(R.string.note_edit_fragment_edit_title);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mCurrentColorView.setTransitionName(mNote.getId().toString());
		}
		mNote.setViewed();
		mCurrentColorView.setCurrentColor(mNote.getColor());
		mTitleEdit.setText(mNote.getTitle());
		mDescriptionEdit.setText(mNote.getDescription());
		setCurrentColorDescription(mNote.getColor());
		setColorItemMetadata();

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int size = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, COLOR_VIEW_SIZE_IN_DP, metrics);
		int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, COLOR_VIEW_MARGIN_IN_DP, metrics);
		int favMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, COLOR_VIEW_FAVORITE_MARGIN_IN_DP, metrics);
		defaultViewParams = new LinearLayout.LayoutParams(size, size);
		defaultViewParams.setMargins(margin, margin, margin, margin);
		favoriteViewParams = new LinearLayout.LayoutParams(size, size);
		favoriteViewParams.setMargins(0, 0, favMargin, 0);

		float colorInterval = COLOR_VIEWS_RANGE / COLOR_VIEWS_COUNT;
		for (int i = 0; i < COLOR_VIEWS_COUNT; i++) {

			final ColorView colorView = newColorView(
					getContext(), colorInterval / 2 + (colorInterval * i));
			colorView.setOnTouchListener(defaultTouchListener);
			mColorScrollLayout.addView(colorView, defaultViewParams);
		}

		restoreFavoriteColors(view);

		mCurrentColorView.setOnLongClickListener(v -> {
            final ColorView colorView = newColorView(getContext(),
                    mCurrentColorView.getCurrentColor());
            colorView.setOnClickListener(v1 -> {
                @ColorInt int pickedColor = colorView.getCurrentColor();
                mCurrentColorView.setCurrentColor(pickedColor);
                setCurrentColorDescription(pickedColor);
            });
            colorView.setOnLongClickListener(cv -> {
                ViewGroup parent = (ViewGroup) cv.getParent();
                FAVORITE_COLORS.remove((Integer) ((ColorView) cv).getCurrentColor());
                parent.removeView(cv);
                return true;
            });
            mFavoriteScrollLayout.addView(colorView, favoriteViewParams);
            FAVORITE_COLORS.add(colorView.getCurrentColor());
            return true;
        });

		return view;
	}

	private void setColorItemMetadata() {
		if (mNote == null) {
			return;
		}
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		format.setTimeZone(TimeZone.getDefault());
		mDateCreatedText.setText(getString(R.string.note_edit_created_date,
				format.format(mNote.getCreatedDate())));
		mDateEditedText.setText(getString(R.string.note_edit_edited_date,
				format.format(mNote.getEditedDate())));
		mDateViewedText.setText(getString(R.string.note_edit_viewed_date,
				format.format(mNote.getViewedDate())));
	}

	private void restoreFavoriteColors(View view) {
		for (Integer color: FAVORITE_COLORS) {
			final ColorView favorite = newColorView(getContext(), color);
			favorite.setOnClickListener(v -> {
                @ColorInt int pickedColor = favorite.getCurrentColor();
                mCurrentColorView.setCurrentColor(pickedColor);
                setCurrentColorDescription(pickedColor);
            });
			view.setOnLongClickListener(v -> {
                ViewGroup parent = (ViewGroup) v.getParent();
                FAVORITE_COLORS.remove((Integer) ((ColorView) v).getCurrentColor());
                parent.removeView(v);
                return true;
            });
			mFavoriteScrollLayout.addView(favorite, favoriteViewParams);
		}
	}

	private void setCurrentColorDescription(@ColorInt int currentColor) {
		mCurrentColorR.setText(getResources()
				.getString(R.string.color_description_r, Color.red(currentColor)));
		mCurrentColorG.setText(getResources()
				.getString(R.string.color_description_g, Color.green(currentColor)));
		mCurrentColorB.setText(getResources()
				.getString(R.string.color_description_b, Color.blue(currentColor)));
		float[] hsv = new float[] {0, 0, 0};
		Color.colorToHSV(currentColor, hsv);
		mCurrentColorH.setText(getResources()
				.getString(R.string.color_description_h, hsv[INDEX_HUE]));
		mCurrentColorS.setText(getResources()
				.getString(R.string.color_description_s, hsv[INDEX_SAT]));
		mCurrentColorV.setText(getResources()
				.getString(R.string.color_description_v, hsv[INDEX_VAL]));
	}

	private static ColorView newColorView(Context context, float hue) {
		return new ColorView(context, hue);
	}

	private static ColorView newColorView(Context context, @ColorInt int color) {
		return  new ColorView(context, color);
	}

	private class ViewColorTouchListener implements View.OnTouchListener {
		private static final float HUE_SENSIVITY = 0.2f;
		private static final float VALUE_SENSIVITY = 0.003f;

		private ColorView mColorView;

		private final GestureDetector gestureDetector =
				new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

					@Override
					public void onLongPress(MotionEvent e) {
						Vibrator vibrator = (Vibrator) getContext()
								.getSystemService(Context.VIBRATOR_SERVICE);
						vibrator.vibrate(100);
						mMainScroll.setScrollingEnabled(false);
						mColorScroll.setScrollingEnabled(false);
						mColorView.setOnTouchListener((v, event) -> {
                            int index = event.getActionIndex();
                            int pointerId = event.getPointerId(index);

                            VelocityTracker	veloTracker = VelocityTracker.obtain();

                            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                veloTracker.addMovement(event);
                                veloTracker.computeCurrentVelocity(10);
                                float deltaX = VelocityTrackerCompat
                                        .getXVelocity(veloTracker, pointerId);
                                float deltaY = 	VelocityTrackerCompat
                                        .getYVelocity(veloTracker,	pointerId);
                                mColorView.variateColor(deltaX * HUE_SENSIVITY,
                                        -deltaY * VALUE_SENSIVITY);
                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                mMainScroll.setScrollingEnabled(true);
                                mColorScroll.setScrollingEnabled(true);
                                mColorView.setOnTouchListener(defaultTouchListener);
                            }
                            veloTracker.recycle();
                            return true;
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
			if (v instanceof ColorView) {
				mColorView = (ColorView) v;
			}
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
				mNote.setColor(mCurrentColorView.getCurrentColor());
				mNote.setTitle(mTitleEdit.getText().toString());
				mNote.setDescription(mDescriptionEdit.getText().toString());
				Intent intent = new Intent();
				intent.putExtra(EXTRA_COLOR_ITEM, mNote);

				View view = getView();
				if (view != null) {
					InputMethodManager mImm = (InputMethodManager) getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					mImm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
				getActivity().getSupportFragmentManager().popBackStack();
				getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected IBasePresenter getPresenter() {
		return null;
	}

	@Override
	protected boolean onBackPressed() {
		Fragment targetFragment = getTargetFragment();
		int requestCode = getTargetRequestCode();
		getActivity().getSupportFragmentManager().popBackStackImmediate();
		if (targetFragment != null) {
			targetFragment.onActivityResult(requestCode, RESULT_CANCELED, null);
		}
		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Note savedColor = new Note(
				mCurrentColorView.getCurrentColor(),
				mTitleEdit.getText().toString(),
				mDescriptionEdit.getText().toString());
		outState.putParcelable(SAVED_COLOR_ITEM, savedColor);
		super.onSaveInstanceState(outState);
	}
}
