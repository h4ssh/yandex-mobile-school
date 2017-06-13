package ru.yandex.mobile_school.views.notes_list;

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

public class NotesListGenerateFragment extends DialogFragment {

	private final static String DEFAULT_QUANTITY = "5000";

	public interface NotesListGenerateDialogListener {
		void onGenerate(int quantity);
	}

	NotesListGenerateDialogListener mListener;

	@BindView(R.id.notes_list_generate_edit) EditText mGenerateCountEditText;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mListener = (NotesListGenerateDialogListener) getTargetFragment();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_notes_list_generate, null);
		ButterKnife.bind(this, view);
		mGenerateCountEditText.setText(DEFAULT_QUANTITY);
		builder.setTitle(R.string.notes_list_generator_title);
		builder.setView(view)
				.setPositiveButton(R.string.button_ok, (dialog, id) -> {
                    if (mListener != null) {
                        int quantity = Integer.parseInt(DEFAULT_QUANTITY);
                        try {
                            quantity = Integer.parseInt(mGenerateCountEditText.getText().toString());
                        } catch (Exception ignored) {
                        }
                        mListener.onGenerate(quantity);
                    }
                })
				.setNegativeButton(R.string.button_cancel, (dialog, id) -> dismiss());

		return builder.create();
	}

}
