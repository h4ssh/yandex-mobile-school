package ru.yandex.mobile_school.ui.colors_list;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;

public class ColorsListExportFragment extends DialogFragment {

	public interface  ColorsListExportDialogListener {
		void onExportClick(String path);
		void onImportClick(String path);
	}

	private static final String DEFAULT_PATH = "/storage/emulated/0/itemlist.ili";;
	private ColorsListExportDialogListener mListener;

	@BindView(R.id.colors_list_export_path) EditText mPathEditText;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mListener = (ColorsListExportFragment.ColorsListExportDialogListener) getTargetFragment();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_colors_list_export, null);
		ButterKnife.bind(this, view);
		builder.setTitle(getString(R.string.colors_list_export_title));
		builder.setView(view)
				.setPositiveButton(R.string.colors_list_export_export, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (mListener != null)
							mListener.onExportClick(mPathEditText.getText().toString());
					}
				})
				.setNegativeButton(R.string.colors_list_export_import, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (mListener != null)
							mListener.onImportClick(mPathEditText.getText().toString());
					}
				})
				.setNeutralButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dismiss();
					}
				});
		mPathEditText.setText(DEFAULT_PATH);
		return builder.create();
	}
}
