package ru.yandex.mobile_school.ui.colors_list;

import android.app.Dialog;
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

public class ColorsListFilterFragment extends DialogFragment {

	public interface ColorsListFilterDialogListener {
		void onFilterPositiveClick(int filterParam, Date startDate, Date endDate);
	}

	ColorsListFilterDialogListener mListener;

	@BindView(R.id.colors_list_filter_param) Spinner mFilterParamSpinner;
	@BindView(R.id.colors_list_filter_start_date_check)	CheckBox mStartDateCheck;
	@BindView(R.id.colors_list_filter_start_date) DatePicker mStartDatePicker;
	@BindView(R.id.colors_list_filter_end_date_check) CheckBox mEndDateCheck;
	@BindView(R.id.colors_list_filter_end_date) DatePicker mEndDatePicker;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mListener = (ColorsListFilterFragment.ColorsListFilterDialogListener) getTargetFragment();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_colors_list_filter, null);
		ButterKnife.bind(this, view);
		builder.setTitle(getString(R.string.colors_list_filter_title));
		String[] filterParamItems = getResources().getStringArray(R.array.colors_list_filter_by_items);
		builder.setView(view)
				.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						int pos = mFilterParamSpinner.getSelectedItemPosition();
						Date start = mStartDateCheck.isChecked() ? getSelectedDate(mStartDatePicker) : null;
						Date end = mEndDateCheck.isChecked() ? getSelectedDate(mEndDatePicker) : null;
						if (mListener != null)
							mListener.onFilterPositiveClick(pos, start, end);
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
		setDatePickerDateToday(mStartDatePicker);
		setDatePickerDateToday(mEndDatePicker);

		return builder.create();

	}

	private Date getSelectedDate(DatePicker picker) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, picker.getYear());
		cal.set(Calendar.MONTH, picker.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, picker.getDayOfMonth());
		return cal.getTime();
	}

	private void setDatePickerDateToday(DatePicker picker) {
		Calendar cal=Calendar.getInstance();
		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		picker.updateDate(year, month, day);
	}
}
