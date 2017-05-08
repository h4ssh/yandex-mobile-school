package ru.yandex.mobile_school.ui.colors_list;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;

public class ColorsListSortFragment extends DialogFragment {

	private static final String SHARED_PREFS_NAME = "shared_prefs_sort_name";
	private static final String SHARED_PREFS_PARAM = "shared_prefs_sort_param";
	private static final String SHARED_PREFS_ORDER = "shared_prefs_sort_order";

	public interface ColorsListSortDialogListener {
		void onSortPositiveClick(int sortParam, boolean ascending);
	}

	ColorsListSortDialogListener mListener;


	@BindView(R.id.colors_list_sort_by) Spinner mSortBySpinner;
	@BindView(R.id.colors_list_sort_order) Spinner mSortOrderSpinner;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mListener = (ColorsListSortDialogListener) getTargetFragment();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_colors_list_sort, null);
		ButterKnife.bind(this, view);
		builder.setTitle(R.string.colors_list_sort_title);
		String[] sortByItems = getResources().getStringArray(R.array.colors_list_sort_by_items);
		String[] sortOrderItems = getResources().getStringArray(R.array.colors_list_sort_order_items);
		builder.setView(view)
				.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						int pos = mSortBySpinner.getSelectedItemPosition();
						boolean ascending = mSortOrderSpinner.getSelectedItemPosition() == 0;
						if (mListener != null) {
							saveState(pos, ascending);
							mListener.onSortPositiveClick(pos, ascending);
						}
					}
				})
				.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dismiss();
					}
				});

		mSortBySpinner.setAdapter(new ArrayAdapter<>(getContext(),
				R.layout.support_simple_spinner_dropdown_item, sortByItems));
		mSortOrderSpinner.setAdapter(new ArrayAdapter<>(getContext(),
				R.layout.support_simple_spinner_dropdown_item, sortOrderItems));
		restoreState();
		return builder.create();
	}

	private void saveState(int pos, boolean ascending) {
		SharedPreferences.Editor editor = getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(SHARED_PREFS_PARAM, pos);
		editor.putBoolean(SHARED_PREFS_ORDER, ascending);
		editor.apply();
	}

	private void restoreState() {
		SharedPreferences preferences = getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		mSortBySpinner.setSelection(preferences.getInt(SHARED_PREFS_PARAM, 0));
		mSortOrderSpinner.setSelection(preferences.getBoolean(SHARED_PREFS_ORDER, true) ? 0 : 1);
	}

}
