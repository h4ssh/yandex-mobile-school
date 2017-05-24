package ru.yandex.mobile_school.ui.colors_list;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.api.NotesAPIClient;
import ru.yandex.mobile_school.data.ColorItem;
import ru.yandex.mobile_school.data.DataStorage;
import ru.yandex.mobile_school.ui.base.BaseFragment;
import ru.yandex.mobile_school.ui.base.SingleFragmentActivity;
import ru.yandex.mobile_school.ui.color_picker.ColorPickerFragment;

import static android.app.Activity.RESULT_OK;

public class ColorsListFragment extends BaseFragment implements
		ColorsListSortFragment.ColorsListSortDialogListener,
		ColorsListFilterFragment.ColorsListFilterDialogListener,
		ColorsListExportFragment.ColorsListExportDialogListener,
		ColorsListSearchFragment.ColorsListSearchDialogListener,
		ColorsListGenerateFragment.ColorsListGenerateDialogListener,
		ColorsListUserFragment.ColorsListUserDialogListener,
		ColorsListAsyncActor.ColorsListAsyncActorListener,
		ColorsListAdapter.AdapterAsyncActionsListener,
		ColorsListAdapter.AdapterOnClickListener,
		ColorsListLooperThread.Callback,
		NotesAPIClient.NotesAPICallbacks {

	private static final int REQUEST_CODE_ADD = 1;
	private static final int REQUEST_CODE_EDIT = 2;
	private static final int ASYNC_ACTION_NOTIFICATION_ID = 111;

	private static final String EXTRA_EDIT_POSITION = "extra_edit_position";

	private int mEditPosition = 0;
	private int mPendingOperations = 0;
	private ColorsListAsyncActor mAsyncActor;
	private ColorsListAdapter mListAdapter;
	private NotificationManager mNotifyManager;
	private NotificationCompat.Builder mBuilder;
	private ColorsListLooperThread mLooperThread;

	@BindView(R.id.colors_list_fab)	FloatingActionButton mAddColorFAB;
	@BindView(R.id.colors_list_progress_bar) ProgressBar mProgressBar;
	@BindView(R.id.colors_list_view) RecyclerView mColorsListView;

	static ColorsListFragment newInstance() {
		return new ColorsListFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (savedInstanceState != null) {
			mEditPosition = savedInstanceState.getInt(EXTRA_EDIT_POSITION);
		}
		mLooperThread = new ColorsListLooperThread(getContext(), new Handler(), this);
		mLooperThread.start();
		mLooperThread.prepareHandler();

		mListAdapter = new ColorsListAdapter(DataStorage.get(getContext()).getColorItems());
		mListAdapter.setAdapterSortListener(this);
		mListAdapter.setAdapterOnClickListener(this);
	}

	@Override
	public void onDestroy() {
		mLooperThread.quit();
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_colors_list, container, false);
		ButterKnife.bind(this, view);

		ColorsListActivity activity = (ColorsListActivity) getActivity();
		activity.getSupportActionBar().setTitle(R.string.app_name);
		activity.setNavBarItemsEnabled(true);

		displayProgressBarIfNeeded(null);
		mAsyncActor = new ColorsListAsyncActor(getContext());
		mColorsListView.setLayoutManager(new LinearLayoutManager(getContext()));
		mColorsListView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
		mColorsListView.setAdapter(mListAdapter);
		mAddColorFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerFragment fragment = ColorPickerFragment.newInstance(null);
				fragment.setTargetFragment(getFragment(), REQUEST_CODE_ADD);
				ColorsListActivity listActivity = (ColorsListActivity) getActivity();
				listActivity.replaceFragment(fragment);
				listActivity.setNavBarItemsEnabled(false);
			}
		});
		mAddColorFAB.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ColorsListGenerateFragment filterFragment = new ColorsListGenerateFragment();
				filterFragment.setTargetFragment(getFragment(), 0);
				filterFragment.show(getActivity().getSupportFragmentManager(), "");
				return true;
			}
		});
		mAsyncActor.setListener(this);
		return view;
	}

	private ColorsListFragment getFragment() {
		return this;
	}

	@Override
	public void onClick(int position, ColorItem colorItem) {
		mEditPosition = position;
		ColorPickerFragment fragment = ColorPickerFragment.newInstance(colorItem);
		fragment.setTargetFragment(getFragment(), REQUEST_CODE_EDIT);
		ColorsListActivity listActivity = (ColorsListActivity) getActivity();
		listActivity.replaceFragment(fragment);
		listActivity.setNavBarItemsEnabled(false);
	}

	@Override
	public void onLongClick(int position, ColorItem colorItem) {
		getDeleteAlertDialog(colorItem).show();
	}

	private AlertDialog getDeleteAlertDialog(final ColorItem item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(R.string.delete_dialog_title);
		builder.setMessage(R.string.delete_dialog_text);
		builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListAdapter.deleteItem(item);
				DataStorage storage = DataStorage.get(getContext());
				storage.deleteColorItem(item);
				if (item.getServerId() != 0) {
					NotesAPIClient.get().deleteUserNote(storage.getUserId(), item.getId(),
							item.getServerId(), getFragment());
				}
			}
		});
		builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		return builder.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
			ColorItem item = data.getParcelableExtra(ColorPickerFragment.EXTRA_COLOR_ITEM);
			addColorItem(item);
		}
		if (requestCode == REQUEST_CODE_EDIT) {
			ColorItem old = mListAdapter.getColorItem(mEditPosition);
			ColorItem updated;
			if (resultCode == RESULT_OK) {
				updated = data.getParcelableExtra(ColorPickerFragment.EXTRA_COLOR_ITEM);
			} else {
				updated = old;
				updated.setViewed();
			}
			old.updateWith(updated);
			displayProgressBarIfNeeded(true);
			mListAdapter.resort();
			alert(getString(R.string.colors_list_sort_started));
			DataStorage storage = DataStorage.get(getContext());
			storage.updateColorItem(updated);
			NotesAPIClient.get().updateUserNote(storage.getUserId(), updated.getId(),
					updated.getServerId(), updated.toNote(), this);
		}
	}

	private void addColorItem(ColorItem item) {
		DataStorage storage = DataStorage.get(getContext());
		storage.addColorItem(item);
		NotesAPIClient.get().addUserNote(storage.getUserId(), item.getId(), item.toNote(), this);
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
		if (mPendingOperations > 0) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
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
			case R.id.colors_list_cloud_download:
				ColorsListUserFragment userFragment = new ColorsListUserFragment();
				userFragment.setTargetFragment(this, 0);
				userFragment.show(getActivity().getSupportFragmentManager(), "");
				break;
			case R.id.colors_list_menu_sort:
				ColorsListSortFragment sortFragment = new ColorsListSortFragment();
				sortFragment.setTargetFragment(this, 0);
				sortFragment.show(getActivity().getSupportFragmentManager(), "");
				break;
			case R.id.colors_list_menu_filter:
				ColorsListFilterFragment filterFragment = new ColorsListFilterFragment();
				filterFragment.setTargetFragment(this, 0);
				filterFragment.show(getActivity().getSupportFragmentManager(), "");
				break;
			case R.id.colors_list_menu_import_export:
				ColorsListExportFragment exportFragment = new ColorsListExportFragment();
				exportFragment.setTargetFragment(this, 0);
				exportFragment.show(getActivity().getSupportFragmentManager(), "");
				break;
			case R.id.colors_list_menu_search:
				ColorsListSearchFragment searchFragment = new ColorsListSearchFragment();
				searchFragment.setTargetFragment(this, 0);
				searchFragment.show(getActivity().getSupportFragmentManager(), "");
				break;
			case R.id.colors_list_menu_reset:
				mListAdapter.resetFilters();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSortPositiveClick(int sortParam, boolean ascending) {
		displayProgressBarIfNeeded(true);
		alert(getString(R.string.colors_list_sort_started));
		Resources res = getResources();
		String[] sortParams = res.getStringArray(R.array.colors_list_sort_by_items);
		String selectedParam = sortParams[sortParam];
		if (selectedParam.equals(res.getString(R.string.colors_list_sort_by_title)))
			mListAdapter.sortBy(ColorsListAdapter.SORT_PARAM_TITLE, ascending);
		else if (selectedParam.equals(res.getString(R.string.colors_list_sort_by_created)))
			mListAdapter.sortBy(ColorsListAdapter.SORT_PARAM_CREATED, ascending);
		else if (selectedParam.equals(res.getString(R.string.colors_list_sort_by_edited)))
			mListAdapter.sortBy(ColorsListAdapter.SORT_PARAM_EDITED, ascending);
		else if (selectedParam.equals(res.getString(R.string.colors_list_sort_by_viewed)))
			mListAdapter.sortBy(ColorsListAdapter.SORT_PARAM_VIEWED, ascending);
	}

	@Override
	public void onFilterPositiveClick(int filterParam, Date startDate, Date endDate) {
		displayProgressBarIfNeeded(true);
		alert(getString(R.string.colors_list_filter_started));
		Resources res = getResources();
		String[] filterParams = res.getStringArray(R.array.colors_list_filter_by_items);
		String selectedParam = filterParams[filterParam];
		String filterName = "";
		if (selectedParam.equals(res.getString(R.string.colors_list_filter_by_created)))
			filterName = ColorsListAdapter.FILTER_PARAM_CREATED;
		else if (selectedParam.equals(res.getString(R.string.colors_list_filter_by_edited)))
			filterName = ColorsListAdapter.FILTER_PARAM_EDITED;
		else if (selectedParam.equals(res.getString(R.string.colors_list_filter_by_viewed)))
			filterName = ColorsListAdapter.FILTER_PARAM_VIEWED;
		mListAdapter.filter(filterName, startDate, endDate);
	}

	@Override
	public void onExportClick(String path) {
		displayProgressBarIfNeeded(true);
		//mAsyncActor.exportItems(path);
		mLooperThread.queueTask(path, ColorsListLooperThread.WHAT_EXPORT);
	}

	@Override
	public void onImportClick(String path) {
		displayProgressBarIfNeeded(true);
		//mAsyncActor.importItems(path);
		mLooperThread.queueTask(path, ColorsListLooperThread.WHAT_IMPORT);
	}

	@Override
	public void onSearchClick(String query) {
		mListAdapter.search(query);
	}

	@Override
	public void onItemsAddFinish() {
		displayProgressBarIfNeeded(false);
		mListAdapter.changeData(DataStorage.get(getContext()).getColorItems());
		alert(getString(R.string.colors_list_generator_finish));
		mBuilder.setContentText("Generating complete").setProgress(0,0,false);
		mNotifyManager.notify(ASYNC_ACTION_NOTIFICATION_ID, mBuilder.build());
	}

	@Override
	public void onItemsAddProgress(int percent) {
		mBuilder.setProgress(100, percent, false);
		mNotifyManager.notify(ASYNC_ACTION_NOTIFICATION_ID, mBuilder.build());
	}

//	@Override
//	public void onItemsExportFinish(boolean result) {
//		displayProgressBarIfNeeded(false);
//		if (result) {
//			alert(getString(R.string.colors_list_export_success));
//		} else {
//			alert(getString(R.string.colors_list_export_error));
//		}
//	}
//
//	@Override
//	public void onItemsImportFinish(boolean result) {
//		displayProgressBarIfNeeded(false);
//		if (result) {
//			mListAdapter.changeData(DataStorage.get(getContext()).getColorItems());
//			alert(getString(R.string.colors_list_import_success));
//		} else {
//			alert(getString(R.string.colors_list_import_error));
//		}
//	}

	@Override
	public void onSortFinish() {
		displayProgressBarIfNeeded(false);
		alert(getString(R.string.colors_list_sort_finished));
	}

	@Override
	public void onFilterFinish() {
		displayProgressBarIfNeeded(false);
		alert(getString(R.string.colors_list_filter_finished));
	}

	@Override
	public void onGenerate(int quantity) {
		alert(getString(R.string.colors_list_generator_start));
		displayProgressBarIfNeeded(true);
		showGenerateNotification();
		mAsyncActor.generateItems(quantity);
	}

	@Override
	public void onColorsExported(boolean result) {
		displayProgressBarIfNeeded(false);
		if (result) {
			alert(getString(R.string.colors_list_export_success));
		} else {
			alert(getString(R.string.colors_list_export_error));
		}
	}

	@Override
	public void onColorsImported(boolean result) {
		displayProgressBarIfNeeded(false);
		if (result) {
			mListAdapter.changeData(DataStorage.get(getContext()).getColorItems());
			alert(getString(R.string.colors_list_import_success));
		} else {
			alert(getString(R.string.colors_list_import_error));
		}
	}

	private static final String TAG_API = "API CALLBACKS";

	@Override
	public void onGetUserNotes(int user, ArrayList<ColorItem> items) {
		DataStorage.get(getContext()).replaceColorItems(items);
		mListAdapter.changeData(items);
	}

	@Override
	public void onAddUserNote(int user, UUID itemId, int noteId) {
		Log.d(TAG_API, "On add user note: " + user + " UUID: " + itemId.toString() + " noteId: " + noteId);
		DataStorage storage = DataStorage.get(getContext());
		ColorItem item = storage.getColorItem(itemId);
		item.setServerId(noteId);
		storage.updateColorItem(item);
		mListAdapter.getColorItem(itemId).setServerId(noteId);
	}

	@Override
	public void onDeleteUserNote(int user, UUID itemId) {
	}

	@Override
	public void onUpdateUserNote(int user, UUID itemId) {
		Log.d(TAG_API, "update");
	}

	@Override
	public void onError(String message) {
		alert("API error: " + message);
	}

	@Override
	public void onUserChanged(int newUser) {
		DataStorage.get(getContext()).setUserId(newUser);
		NotesAPIClient.get().getUserNotes(newUser, this);
		alert(getString(R.string.colors_list_download_start));
	}
}
