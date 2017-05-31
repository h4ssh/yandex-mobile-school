package ru.yandex.mobile_school.ui.colors_list;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;

public class ColorsListSearchFragment extends DialogFragment {

	public interface ColorsListSearchDialogListener {
		void onSearchClick(String query);
	}

	@BindView(R.id.colors_list_search_query) EditText mSearchEditText;

	private ColorsListSearchDialogListener mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mListener = (ColorsListSearchFragment.ColorsListSearchDialogListener) getTargetFragment();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_colors_list_search, null);
		ButterKnife.bind(this, view);
		builder.setTitle(getString(R.string.colors_list_search_title));
		builder.setView(view)
				.setPositiveButton(R.string.colors_list_search_search, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (mListener != null) {
							mListener.onSearchClick(mSearchEditText.getText().toString());
						}
					}
				})
				.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dismiss();
					}
				});
		return builder.create();
	}
}
