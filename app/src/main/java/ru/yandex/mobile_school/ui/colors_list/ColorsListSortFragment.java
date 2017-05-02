package ru.yandex.mobile_school.ui.colors_list;

import android.app.Dialog;
import android.content.DialogInterface;
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
						if (mListener != null)
							mListener.onSortPositiveClick(pos, ascending);
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
		return builder.create();
	}


}
