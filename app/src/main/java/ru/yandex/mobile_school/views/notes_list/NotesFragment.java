package ru.yandex.mobile_school.views.notes_list;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.App;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.presenters.IBasePresenter;
import ru.yandex.mobile_school.presenters.NotesListAsyncActor;
import ru.yandex.mobile_school.presenters.NotesListLooperThread;
import ru.yandex.mobile_school.presenters.NotesPresenter;
import ru.yandex.mobile_school.views.BaseFragment;
import ru.yandex.mobile_school.views.note_edit.NoteEditFragment;

import static android.app.Activity.RESULT_OK;

public class NotesFragment extends BaseFragment implements
        NotesListSortFragment.NotesListSortDialogListener,
        NotesListFilterFragment.NotesListFilterDialogListener,
        NotesListExportFragment.NotesListExportDialogListener,
        NotesListSearchFragment.NotesListSearchDialogListener,
        NotesListGenerateFragment.NotesListGenerateDialogListener,
        NotesListUserFragment.NotesListUserDialogListener,
        NotesListAsyncActor.NotesListAsyncActorListener,
		NotesListLooperThread.Callback{

	private static final int REQUEST_CODE_ADD = 1;
	private static final int REQUEST_CODE_EDIT = 2;
	private static final int ASYNC_ACTION_NOTIFICATION_ID = 111;

	private static final String EXTRA_EDIT_POSITION = "extra_edit_position";

	@Inject
	NotesPresenter presenter;

	private int mEditPosition = 0;
	private int mPendingOperations = 0;
	private NotesListAsyncActor mAsyncActor;
	private NotificationManager mNotifyManager;
	private NotificationCompat.Builder mBuilder;
	private NotesListLooperThread mLooperThread;

    private Intent resultIntent;
    private int resultCode;
    private int requestCode = 0;

	@BindView(R.id.notes_list_fab)	FloatingActionButton mAddNoteFAB;
	@BindView(R.id.notes_list_progress_bar) ProgressBar mProgressBar;
	@BindView(R.id.notes_list_view) RecyclerView mNotesListView;

	static NotesFragment newInstance() {
		return new NotesFragment();
	}

	public NotesFragment() {
        // Required empty public constructor
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        App.getComponent().inject(this);

        setHasOptionsMenu(true);

		if (savedInstanceState != null) {
			mEditPosition = savedInstanceState.getInt(EXTRA_EDIT_POSITION);
		}
		mLooperThread = new NotesListLooperThread(new Handler(), this);
		mLooperThread.start();
		mLooperThread.prepareHandler();
	}

    @Override
	public void onDestroy() {
		mLooperThread.quit();
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
		ButterKnife.bind(this, view);
        presenter.onCreateView(this);

		NotesActivity activity = (NotesActivity) getActivity();
		activity.getSupportActionBar().setTitle(R.string.app_name);
		activity.setNavBarItemsEnabled(true);

		displayProgressBarIfNeeded(null);
		mAsyncActor = new NotesListAsyncActor();
		mNotesListView.setLayoutManager(new LinearLayoutManager(getContext()));
		mNotesListView.addItemDecoration(new DividerItemDecoration(getContext(),
				DividerItemDecoration.VERTICAL));
		mNotesListView.setAdapter(presenter.getNotesAdapter());
		mAddNoteFAB.setOnClickListener(v -> {
            NoteEditFragment fragment = NoteEditFragment.newInstance(null);
            fragment.setTargetFragment(getFragment(), REQUEST_CODE_ADD);
            NotesActivity listActivity = (NotesActivity) getActivity();
            listActivity.replaceFragment(fragment);
            listActivity.setNavBarItemsEnabled(false);
        });
		mAddNoteFAB.setOnLongClickListener(v -> {
            NotesListGenerateFragment filterFragment = new NotesListGenerateFragment();
            filterFragment.setTargetFragment(getFragment(), 0);
            filterFragment.show(getActivity().getSupportFragmentManager(), "");
            return true;
        });
		mAsyncActor.setListener(this);

        if (requestCode != 0){
            parseResult();
        }

		return view;
	}

	private NotesFragment getFragment() {
		return this;
	}

	public void onClick(int position, Note note) {
		mEditPosition = position;
		NoteEditFragment fragment = NoteEditFragment.newInstance(note);
		fragment.setTargetFragment(getFragment(), REQUEST_CODE_EDIT);
		NotesActivity listActivity = (NotesActivity) getActivity();
		View itemView = mNotesListView.getLayoutManager().findViewByPosition(position);
		View sharedView = itemView.findViewById(R.id.notes_list_item_color_view);
		listActivity.replaceFragmentWithShared(fragment, sharedView, note.getId().toString());
		listActivity.setNavBarItemsEnabled(false);
	}

	public void onLongClick(Note note) {
		getDeleteAlertDialog(note).show();
	}

	private AlertDialog getDeleteAlertDialog(final Note item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(R.string.delete_dialog_title);
		builder.setMessage(R.string.delete_dialog_text);
		builder.setPositiveButton(R.string.button_delete, (dialog, which) -> {
            presenter.deleteLocalNote(item);
            if (item.getServerId() != 0) {
                presenter.deleteNote(item.getServerId());
            }
        });
		builder.setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.dismiss());
		return builder.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.resultIntent = data;
	}

	private void parseResult() {
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            Note item = resultIntent.getParcelableExtra(NoteEditFragment.EXTRA_COLOR_ITEM);
            addColorItem(item);
        }
        if (requestCode == REQUEST_CODE_EDIT) {
            Note old = presenter.getNoteAtPosition(mEditPosition);
            Note updated;
            if (resultCode == RESULT_OK) {
                updated = resultIntent.getParcelableExtra(NoteEditFragment.EXTRA_COLOR_ITEM);
            } else {
                updated = old;
                updated.setViewed();
            }
            old.updateWith(updated);
            displayProgressBarIfNeeded(true);
            presenter.resortNotes();
            presenter.updateLocalNote(updated);
            presenter.updateNote(updated.getServerId(), updated.toNoteDTO());
        }
        requestCode = 0;
    }

	private void addColorItem(Note item) {
		presenter.addLocalNote(item);
		presenter.postNote(item.getId(), item.toNoteDTO());
	}

	private void alert(String text) {
		Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
	}

	private void displayProgressBarIfNeeded(Boolean opStart) {
		if (opStart != null) {
			if (opStart) {
				mPendingOperations++;
			} else {
				mPendingOperations--;
			}
		}

		if (mProgressBar == null) {
            return; // TODO: fix and delete scope
        }

		if (mPendingOperations > 0) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else  {
			mProgressBar.setVisibility(View.GONE);
		}
	}

	private void showGenerateNotification() {
		mNotifyManager =
				(NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(getContext());
		mBuilder.setContentTitle("Generate items")
				.setContentText("Generating in progress")
				.setSmallIcon(R.drawable.ic_add_white_24dp);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(EXTRA_EDIT_POSITION, mEditPosition);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.notes_list_cloud_download:
				NotesListUserFragment userFragment = new NotesListUserFragment();
                Bundle args = new Bundle();
                args.putInt(NotesListUserFragment.EXTRA_USER, presenter.getUserId());
                userFragment.setArguments(args);
				userFragment.setTargetFragment(this, 0);
				userFragment.show(getActivity().getSupportFragmentManager(), "");
				break;
			case R.id.notes_list_menu_sort:
				NotesListSortFragment sortFragment = new NotesListSortFragment();
				sortFragment.setTargetFragment(this, 0);
				sortFragment.show(getActivity().getSupportFragmentManager(), "");
				break;
			case R.id.notes_list_menu_filter:
				NotesListFilterFragment filterFragment = new NotesListFilterFragment();
				filterFragment.setTargetFragment(this, 0);
				filterFragment.show(getActivity().getSupportFragmentManager(), "");
				break;
			case R.id.notes_list_menu_import_export:
				NotesListExportFragment exportFragment = new NotesListExportFragment();
				exportFragment.setTargetFragment(this, 0);
				exportFragment.show(getActivity().getSupportFragmentManager(), "");
				break;
			case R.id.notes_list_menu_search:
				NotesListSearchFragment searchFragment = new NotesListSearchFragment();
				searchFragment.setTargetFragment(this, 0);
				searchFragment.show(getActivity().getSupportFragmentManager(), "");
				break;
			case R.id.notes_list_menu_reset:
				presenter.resetFilters();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSortPositiveClick(int sortParam, boolean ascending) {
		displayProgressBarIfNeeded(true);
		presenter.sort(sortParam, ascending);
	}

	@Override
	public void onFilterPositiveClick(int filterParam, Date startDate, Date endDate) {
		displayProgressBarIfNeeded(true);
		presenter.filter(filterParam, startDate, endDate);
	}

	@Override
	public void onExportClick(String path) {
		displayProgressBarIfNeeded(true);
		mLooperThread.queueTask(path, NotesListLooperThread.WHAT_EXPORT);
	}

	@Override
	public void onImportClick(String path) {
		displayProgressBarIfNeeded(true);
		mLooperThread.queueTask(path, NotesListLooperThread.WHAT_IMPORT);
	}

	@Override
	public void onSearchClick(String query){
        presenter.search(query);
	}

	@Override
	public void onItemsAddFinish() {
		displayProgressBarIfNeeded(false);
        presenter.refreshLocal();
		alert(getString(R.string.notes_list_generator_finish));
		mBuilder.setContentText("Generating complete").setProgress(0, 0, false);
		mNotifyManager.notify(ASYNC_ACTION_NOTIFICATION_ID, mBuilder.build());
	}

	@Override
	public void onItemsAddProgress(int percent) {
		mBuilder.setProgress(100, percent, false);
		mNotifyManager.notify(ASYNC_ACTION_NOTIFICATION_ID, mBuilder.build());
	}

	@Override
	public void onGenerate(int quantity) {
		alert(getString(R.string.notes_list_generator_start));
		displayProgressBarIfNeeded(true);
		showGenerateNotification();
		mAsyncActor.generateItems(quantity);
	}

	@Override
	public void onColorsExported(boolean result) {
		displayProgressBarIfNeeded(false);
		if (result) {
			alert(getString(R.string.notes_list_export_success));
		} else {
			alert(getString(R.string.notes_list_export_error));
		}
	}

	@Override
	public void onColorsImported(boolean result) {
		displayProgressBarIfNeeded(false);
		if (result) {
			presenter.refreshLocal();
			alert(getString(R.string.notes_list_import_success));
		} else {
			alert(getString(R.string.notes_list_import_error));
		}
	}

	@Override
	public void onUserChanged(int newUser) {
		presenter.setUserId(newUser);
        presenter.requestNotes(newUser);
		alert(getString(R.string.notes_list_download_start));
	}

	@Override
	protected IBasePresenter getPresenter() {
		return presenter;
	}

    @Override
    public void hideLoading() {
        displayProgressBarIfNeeded(false);
    }
}
