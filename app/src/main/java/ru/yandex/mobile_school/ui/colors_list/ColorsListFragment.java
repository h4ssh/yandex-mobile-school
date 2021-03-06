package ru.yandex.mobile_school.ui.colors_list;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.data.ColorItem;
import ru.yandex.mobile_school.data.DataStorage;
import ru.yandex.mobile_school.ui.color_picker.ColorPickerActivity;
import ru.yandex.mobile_school.utils.DateUtils;

import static android.app.Activity.RESULT_OK;

public class ColorsListFragment extends Fragment implements
		ColorsListSortFragment.ColorsListSortDialogListener,
		ColorsListFilterFragment.ColorsListFilterDialogListener,
		ColorsListExportFragment.ColorsListExportDialogListener,
		ColorsListSearchFragment.ColorsListSearchDialogListener {

	private static final int REQUEST_CODE_ADD = 1;
	private static final int REQUEST_CODE_EDIT = 2;

	private static final String EXTRA_COLOR_ITEMS = "extra_color_items";
	private static final String EXTRA_EDIT_POSITION = "extra_edit_position";

	private ArrayList<ColorItem> mColors;
	private int mEditPosition = 0;

	@BindView(R.id.colors_list_fab)	FloatingActionButton addColorFAB;
	@BindView(R.id.colors_list_view) ListView colorsListView;

	static ColorsListFragment newInstance() {
		return new ColorsListFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (savedInstanceState != null) {
			mColors = savedInstanceState.getParcelableArrayList(EXTRA_COLOR_ITEMS);
			mEditPosition = savedInstanceState.getInt(EXTRA_EDIT_POSITION);
		} else {
			mColors = DataStorage.get(getContext()).getColorItems();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.colors_list_menu, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_colors_list, container, false);
		ButterKnife.bind(this, view);

		colorsListView.setAdapter(new ColorsListAdapter(getContext(), mColors));
		colorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mEditPosition = position;
				ColorItem item = ((ColorsListAdapter)colorsListView.getAdapter()).getColorItem(position);
				startActivityForResult(ColorPickerActivity.newIntent(getContext(), item), REQUEST_CODE_EDIT);
			}
		});
		colorsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				getDeleteAlertDialog(position).show();
				return true;
			}
		});
		addColorFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(ColorPickerActivity.newIntent(getContext()), REQUEST_CODE_ADD);
			}
		});

		return view;
	}

	private AlertDialog getDeleteAlertDialog(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(R.string.delete_dialog_title);
		builder.setMessage(R.string.delete_dialog_text);
		builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ColorsListAdapter adapter = (ColorsListAdapter) colorsListView.getAdapter();
				ColorItem item = adapter.getColorItem(position);
				adapter.deleteItem(item);
				DataStorage.get(getContext()).deleteColorItem(item);
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
			ColorItem item = data.getParcelableExtra(ColorPickerActivity.EXTRA_COLOR_ITEM);
			DataStorage.get(getContext()).addColorItem(item);
			mColors.add(item);
			ColorsListAdapter adapter = (ColorsListAdapter) colorsListView.getAdapter();
			adapter.resetFilters();
			adapter.resort();
		}
		if (requestCode == REQUEST_CODE_EDIT) {
			ColorsListAdapter adapter = (ColorsListAdapter) colorsListView.getAdapter();
			ColorItem old = adapter.getColorItem(mEditPosition);
			ColorItem updated;
			if (resultCode == RESULT_OK) {
				updated = data.getParcelableExtra(ColorPickerActivity.EXTRA_COLOR_ITEM);
			} else {
				updated = old;
				updated.setViewed();
			}
			old.updateWith(updated);
			adapter.resort();
			DataStorage.get(getContext()).updateColorItem(updated);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList(EXTRA_COLOR_ITEMS, mColors);
		outState.putInt(EXTRA_EDIT_POSITION, mEditPosition);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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
				ColorsListAdapter adapter = (ColorsListAdapter) colorsListView.getAdapter();
				adapter.resetFilters();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSortPositiveClick(int sortParam, boolean ascending) {
		Resources res = getResources();
		String[] sortParams = res.getStringArray(R.array.colors_list_sort_by_items);
		String selectedParam = sortParams[sortParam];
		ColorsListAdapter adapter =  (ColorsListAdapter) colorsListView.getAdapter();
		if (selectedParam.equals(res.getString(R.string.colors_list_sort_by_title)))
			adapter.sortBy(ColorsListAdapter.SORT_PARAM_TITLE, ascending);
		else if (selectedParam.equals(res.getString(R.string.colors_list_sort_by_created)))
			adapter.sortBy(ColorsListAdapter.SORT_PARAM_CREATED, ascending);
		else if (selectedParam.equals(res.getString(R.string.colors_list_sort_by_edited)))
			adapter.sortBy(ColorsListAdapter.SORT_PARAM_EDITED, ascending);
		else if (selectedParam.equals(res.getString(R.string.colors_list_sort_by_viewed)))
			adapter.sortBy(ColorsListAdapter.SORT_PARAM_VIEWED, ascending);
	}

	@Override
	public void onFilterPositiveClick(int filterParam, Date startDate, Date endDate) {
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
		ColorsListAdapter adapter =  (ColorsListAdapter) colorsListView.getAdapter();
		adapter.getFilter().filter(DateUtils.getFilterString(filterName, startDate, endDate));
	}

	@Override
	public void onExportClick(String path) {
		boolean result = DataStorage.get(getContext()).exportColorItems(path);
		if (result) {
			Toast.makeText(getContext(), getString(R.string.colors_list_export_success), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), getString(R.string.colors_list_export_error), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onImportClick(String path) {
		boolean result = DataStorage.get(getContext()).importColorItems(path);
		if (result) {
			ColorsListAdapter adapter = (ColorsListAdapter) colorsListView.getAdapter();
			mColors = DataStorage.get(getContext()).getColorItems();
			adapter.changeData(mColors);
			Toast.makeText(getContext(), getString(R.string.colors_list_import_success), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), getString(R.string.colors_list_import_error), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onSearchClick(String query) {
		ColorsListAdapter adapter =  (ColorsListAdapter) colorsListView.getAdapter();
		adapter.search(query);
	}
}
