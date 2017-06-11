package ru.yandex.mobile_school.views.notes_list;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.App;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.model.StorageModel;
import ru.yandex.mobile_school.presenters.IBasePresenter;
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
		NotesListAdapter.AdapterAsyncActionsListener,
		NotesListAdapter.AdapterOnClickListener,
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
	private NotesListAdapter mListAdapter;
	private NotificationManager mNotifyManager;
	private NotificationCompat.Builder mBuilder;
	private NotesListLooperThread mLooperThread;

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
        presenter.onCreate(this);

        setHasOptionsMenu(true);

		if (savedInstanceState != null) {
			mEditPosition = savedInstanceState.getInt(EXTRA_EDIT_POSITION);
		}
		mLooperThread = new NotesListLooperThread(getContext(), new Handler(), this);
		mLooperThread.start();
		mLooperThread.prepareHandler();

		mListAdapter = new NotesListAdapter(StorageModel.get(getContext()).getColorItems());
		mListAdapter.setAdapterSortListener(this);
		mListAdapter.setAdapterOnClickListener(this);
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

		NotesActivity activity = (NotesActivity) getActivity();
		activity.getSupportActionBar().setTitle(R.string.app_name);
		activity.setNavBarItemsEnabled(true);

		displayProgressBarIfNeeded(null);
		mAsyncActor = new NotesListAsyncActor(getContext());
		mNotesListView.setLayoutManager(new LinearLayoutManager(getContext()));
		mNotesListView.addItemDecoration(new DividerItemDecoration(getContext(),
				DividerItemDecoration.VERTICAL));
		mNotesListView.setAdapter(mListAdapter);
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
		return view;
	}

	private NotesFragment getFragment() {
		return this;
	}

	@Override
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

	@Override
	public void onLongClick(int position, Note note) {
		getDeleteAlertDialog(note).show();
	}

	private AlertDialog getDeleteAlertDialog(final Note item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(R.string.delete_dialog_title);
		builder.setMessage(R.string.delete_dialog_text);
		builder.setPositiveButton(R.string.button_delete, (dialog, which) -> {
            mListAdapter.deleteItem(item);
            StorageModel storage = StorageModel.get(getContext());
            storage.deleteColorItem(item);
            if (item.getServerId() != 0) {
                presenter.deleteNote(storage.getUserId(), item.getServerId());
            }
        });
		builder.setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.dismiss());
		return builder.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
			Note item = data.getParcelableExtra(NoteEditFragment.EXTRA_COLOR_ITEM);
			addColorItem(item);
		}
		if (requestCode == REQUEST_CODE_EDIT) {
			Note old = mListAdapter.getColorItem(mEditPosition);
			Note updated;
			if (resultCode == RESULT_OK) {
				updated = data.getParcelableExtra(NoteEditFragment.EXTRA_COLOR_ITEM);
			} else {
				updated = old;
				updated.setViewed();
			}
			old.updateWith(updated);
			displayProgressBarIfNeeded(true);
			mListAdapter.resort();
			alert(getString(R.string.notes_list_sort_started));
			StorageModel storage = StorageModel.get(getContext());
			storage.updateColorItem(updated);
			presenter.updateNote(storage.getUserId(),
					updated.getServerId(), updated.toNoteDTO());
		}
	}

	private void addColorItem(Note item) {
		StorageModel storage = StorageModel.get(getContext());
		storage.addColorItem(item);
		presenter.addNote(storage.getUserId(), item.getId(), item.toNoteDTO());
		mListAdapter.addItem(item);
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
				mListAdapter.resetFilters();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSortPositiveClick(int sortParam, boolean ascending) {
		displayProgressBarIfNeeded(true);
		alert(getString(R.string.notes_list_sort_started));
		Resources res = getResources();
		String[] sortParams = res.getStringArray(R.array.notes_list_sort_by_items);
		String selectedParam = sortParams[sortParam];
		if (selectedParam.equals(res.getString(R.string.notes_list_sort_by_title))) {
			mListAdapter.sortBy(NotesListAdapter.SORT_PARAM_TITLE, ascending);
		} else if (selectedParam.equals(res.getString(R.string.notes_list_sort_by_created))) {
			mListAdapter.sortBy(NotesListAdapter.SORT_PARAM_CREATED, ascending);
		} else if (selectedParam.equals(res.getString(R.string.notes_list_sort_by_edited))) {
			mListAdapter.sortBy(NotesListAdapter.SORT_PARAM_EDITED, ascending);
		} else if (selectedParam.equals(res.getString(R.string.notes_list_sort_by_viewed))) {
			mListAdapter.sortBy(NotesListAdapter.SORT_PARAM_VIEWED, ascending);
		}
	}

	@Override
	public void onFilterPositiveClick(int filterParam, Date startDate, Date endDate) {
		displayProgressBarIfNeeded(true);
		alert(getString(R.string.notes_list_filter_started));
		Resources res = getResources();
		String[] filterParams = res.getStringArray(R.array.notes_list_filter_by_items);
		String selectedParam = filterParams[filterParam];
		String filterName = "";
		if (selectedParam.equals(res.getString(R.string.notes_list_filter_by_created))) {
			filterName = NotesListAdapter.FILTER_PARAM_CREATED;
		} else if (selectedParam.equals(res.getString(R.string.notes_list_filter_by_edited))) {
			filterName = NotesListAdapter.FILTER_PARAM_EDITED;
		} else if (selectedParam.equals(res.getString(R.string.notes_list_filter_by_viewed))) {
			filterName = NotesListAdapter.FILTER_PARAM_VIEWED;
		}
		mListAdapter.filter(filterName, startDate, endDate);
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
	public void onSearchClick(String query) {
		mListAdapter.search(query);
	}

	@Override
	public void onItemsAddFinish() {
		displayProgressBarIfNeeded(false);
		mListAdapter.changeData(StorageModel.get(getContext()).getColorItems());
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
	public void onSortFinish() {
		displayProgressBarIfNeeded(false);
		alert(getString(R.string.notes_list_sort_finished));
	}

	@Override
	public void onFilterFinish() {
		displayProgressBarIfNeeded(false);
		alert(getString(R.string.notes_list_filter_finished));
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
			mListAdapter.changeData(StorageModel.get(getContext()).getColorItems());
			alert(getString(R.string.notes_list_import_success));
		} else {
			alert(getString(R.string.notes_list_import_error));
		}
	}

	public void onGetUserNotes(ArrayList<Note> items) {
        // TODO: move to presenter
		StorageModel.get(getContext()).replaceColorItems(items);
		mListAdapter.changeData(items);
	}

	public void onAddUserNote(UUID itemId, int noteId) {
        // TODO: move to presenter
		StorageModel storage = StorageModel.get(getContext());
		Note item = storage.getColorItem(itemId);
		item.setServerId(noteId);
		storage.updateColorItem(item);
		mListAdapter.getColorItem(itemId).setServerId(noteId);
	}

	@Override
	public void onUserChanged(int newUser) {
		StorageModel.get(getContext()).setUserId(newUser);
        presenter.requestNotes(newUser);
		alert(getString(R.string.notes_list_download_start));
	}

	@Override
	protected IBasePresenter getPresenter() {
		return null; // TODO: return presenter
	}
}
