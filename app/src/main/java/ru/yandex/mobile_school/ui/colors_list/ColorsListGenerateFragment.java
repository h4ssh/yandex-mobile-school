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

public class ColorsListGenerateFragment extends DialogFragment {

	private final static String DEFAULT_QUANTITY = "5000";

	public interface ColorsListGenerateDialogListener {
		void onGenerate(int quantity);
	}

	ColorsListGenerateFragment.ColorsListGenerateDialogListener mListener;

	@BindView(R.id.colors_list_generate_edit) EditText mGenerateCountEditText;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mListener = (ColorsListGenerateFragment.ColorsListGenerateDialogListener) getTargetFragment();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_colors_list_generate, null);
		ButterKnife.bind(this, view);
		mGenerateCountEditText.setText(DEFAULT_QUANTITY);
		builder.setTitle(R.string.colors_list_generator_title);
		builder.setView(view)
				.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (mListener != null) {
							int quantity = Integer.parseInt(DEFAULT_QUANTITY);
							try {
								quantity = Integer.parseInt(mGenerateCountEditText.getText().toString());
							} catch (Exception ignored) {
							}
							mListener.onGenerate(quantity);
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
