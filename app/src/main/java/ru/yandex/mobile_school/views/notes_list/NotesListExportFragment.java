package ru.yandex.mobile_school.views.notes_list;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;

import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class NotesListExportFragment extends DialogFragment {

	public interface NotesListExportDialogListener {
		void onExportClick(String path);
		void onImportClick(String path);
	}

	private static final int REQUEST_CODE_PICK_FILE = 10;
	private static final int PERMISSION_REQUEST_WRITE_EXTERNAL = 11;
	private static final String DEFAULT_PATH = "/storage/emulated/0/itemlist.ili";;
	private NotesListExportDialogListener mListener;

	@BindView(R.id.notes_list_export_path) EditText mPathEditText;
	@BindView(R.id.notes_list_pick_button) Button mPickButton;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Ask for permission
		if (ContextCompat.checkSelfPermission(getActivity(),
				Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(),
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					PERMISSION_REQUEST_WRITE_EXTERNAL);
		}

		mListener = (NotesListExportDialogListener) getTargetFragment();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_notes_list_export, null);
		ButterKnife.bind(this, view);
		builder.setTitle(getString(R.string.notes_list_export_title));
		builder.setView(view)
				.setPositiveButton(R.string.notes_list_export_export,
						(dialog, id) -> {
                            if (mListener != null) {
                                mListener.onExportClick(mPathEditText.getText().toString());
                            }
                        })
				.setNegativeButton(R.string.notes_list_export_import,
						(dialog, id) -> {
                            if (mListener != null) {
                                mListener.onImportClick(mPathEditText.getText().toString());
                            }
                        })
				.setNeutralButton(R.string.button_cancel, (dialog, id) -> dismiss());
		mPathEditText.setText(DEFAULT_PATH);
		mPickButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            PackageManager manager = getContext().getPackageManager();
            List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
            if (infos.size() > 0) {
                startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
            } else {
                Toast.makeText(getContext(), getString(R.string.notes_list_export_intent_error),
                        Toast.LENGTH_SHORT).show();
            }



        });
		return builder.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			String path = uri.getPath();
			mPathEditText.setText(path);
		}
	}
}
