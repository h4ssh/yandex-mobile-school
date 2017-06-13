package ru.yandex.mobile_school.views.notes_list;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.utils.DateUtils;

public class NotesListFilterFragment extends DialogFragment {

	private static final String SHARED_PREFS_NAME = "shared_prefs_filter_name";
	private static final String SHARED_PREFS_PARAM = "shared_prefs_filter_param";
	private static final String SHARED_PREFS_START_DATE = "shared_prefs_filter_start_date";
	private static final String SHARED_PREFS_END_DATE = "shared_prefs_filter_end_date";

	public interface NotesListFilterDialogListener {
		void onFilterPositiveClick(int filterParam, Date startDate, Date endDate);
	}

	NotesListFilterDialogListener mListener;

	@BindView(R.id.notes_list_filter_param) Spinner mFilterParamSpinner;
	@BindView(R.id.notes_list_filter_start_date_check)	CheckBox mStartDateCheck;
	@BindView(R.id.notes_list_filter_start_date) DatePicker mStartDatePicker;
	@BindView(R.id.notes_list_filter_end_date_check) CheckBox mEndDateCheck;
	@BindView(R.id.notes_list_filter_end_date) DatePicker mEndDatePicker;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mListener = (NotesListFilterDialogListener) getTargetFragment();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_notes_list_filter, null);
		ButterKnife.bind(this, view);
		builder.setTitle(getString(R.string.notes_list_filter_title));
		String[] filterParamItems = getResources().
				getStringArray(R.array.notes_list_filter_by_items);
		builder.setView(view)
				.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						int pos = mFilterParamSpinner.getSelectedItemPosition();
						Date start = mStartDateCheck.isChecked() ?
								getSelectedDate(mStartDatePicker) : null;
						Date end = mEndDateCheck.isChecked() ?
								getSelectedDate(mEndDatePicker) : null;
						if (mListener != null) {
							saveState(pos, start, end);
							mListener.onFilterPositiveClick(pos, start, end);
						}
					}
				})
				.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dismiss();
					}
				});

		mFilterParamSpinner.setAdapter(new ArrayAdapter<>(getContext(),
				R.layout.support_simple_spinner_dropdown_item, filterParamItems));

		mStartDateCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					mStartDatePicker.setEnabled(isChecked);
			}
		});
		mEndDateCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mEndDatePicker.setEnabled(isChecked);
			}
		});

		restoreState();
		return builder.create();

	}

	private void restoreState() {
		mFilterParamSpinner.setSelection(getSavedParam());
		Date startDate = getSavedStartDate();
		if (startDate != null) {
			setDatePickerDate(mStartDatePicker, startDate);
			mStartDateCheck.setChecked(true);
		} else {
			setDatePickerDateToday(mStartDatePicker);
			mStartDateCheck.setChecked(false);
		}
		Date endDate = getSavedEndDate();
		if (endDate != null) {
			setDatePickerDate(mEndDatePicker, endDate);
			mEndDateCheck.setChecked(true);
		} else {
			setDatePickerDateToday(mEndDatePicker);
			mEndDateCheck.setChecked(false);
		}
	}

	private void saveState(int pos, Date start, Date end) {
		SharedPreferences.Editor editor = getContext().getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
		editor.putInt(SHARED_PREFS_PARAM, pos);
		if (start != null) {
			editor.putString(SHARED_PREFS_START_DATE, DateUtils.dateToDateString(start));
		} else {
			editor.remove(SHARED_PREFS_START_DATE);
		}
		if (end != null) {
			editor.putString(SHARED_PREFS_END_DATE, DateUtils.dateToDateString(end));
		} else {
			editor.remove(SHARED_PREFS_END_DATE);
		}
		editor.apply();
	}

	private Date getSelectedDate(DatePicker picker) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, picker.getYear());
		cal.set(Calendar.MONTH, picker.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, picker.getDayOfMonth());
		return cal.getTime();
	}

	private void setDatePickerDateToday(DatePicker picker) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		picker.updateDate(year, month, day);
	}

	private Date getSavedStartDate() {
		return 	getSavedDate(SHARED_PREFS_START_DATE);
	}

	private Date getSavedEndDate() {
		return getSavedDate(SHARED_PREFS_END_DATE);
	}

	private Date getSavedDate(String key) {
		SharedPreferences preferences = getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		String dateString = preferences.getString(key, null);
		if (dateString == null) {
			return null;
		} else {
			return DateUtils.parseDateString(dateString);
		}
	}

	private int getSavedParam() {
		SharedPreferences preferences = getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getInt(SHARED_PREFS_PARAM, 0);
	}

	private void setDatePickerDate(DatePicker picker, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		picker.updateDate(
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH)
		);
	}
}
