package ru.yandex.mobile_school.ui.colors_list;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.data.DataStorage;

public class ColorsListUserFragment extends DialogFragment {

	public interface ColorsListUserDialogListener {
		void onUserChanged(int newUser);
	}

	ColorsListUserDialogListener mListener;

	@BindView(R.id.colors_list_user_edit) EditText mUserEdit;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mListener = (ColorsListUserDialogListener) getTargetFragment();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_colors_list_user, null);
		ButterKnife.bind(this, view);
		mUserEdit.setText(Integer.toString(DataStorage.get(getContext()).getUserId()));
		builder.setTitle(R.string.colors_list_user_title);
		builder.setView(view)
				.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int userId = 0;
						try {
							userId = Integer.parseInt(mUserEdit.getText().toString());
						} catch (NumberFormatException ignored) {
						}
						if (userId != 0 && mListener != null) {
							mListener.onUserChanged(userId);
						}
					}
				})
				.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dismiss();
					}
				});
		return builder.create();
	}
}
