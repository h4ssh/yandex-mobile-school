package ru.yandex.mobile_school.ui.colors_list;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.data.ColorItem;
import ru.yandex.mobile_school.data.DataStorage;
import ru.yandex.mobile_school.ui.color_picker.ColorPickerActivity;
import ru.yandex.mobile_school.utils.ArrayUtils;

import static android.app.Activity.RESULT_OK;

public class ColorsListFragment extends Fragment {

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
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (savedInstanceState != null) {
			mColors = savedInstanceState.getParcelableArrayList(EXTRA_COLOR_ITEMS);
			mEditPosition = savedInstanceState.getInt(EXTRA_EDIT_POSITION);
		} else {
			mColors = DataStorage.get(getContext()).getColorItems();
		}

		Intent intent = getActivity().getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			searchColors(query);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.colors_list_menu, menu);
	}

	private void searchColors(String query) {

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
				ColorItem item = mColors.get(position);
				startActivityForResult(ColorPickerActivity.newIntent(getContext(), item), REQUEST_CODE_EDIT);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
			ColorItem item = data.getParcelableExtra(ColorPickerActivity.EXTRA_COLOR_ITEM);
			DataStorage.get(getContext()).addColorItem(item);
			mColors.add(item);
			((BaseAdapter)colorsListView.getAdapter()).notifyDataSetChanged();
		}
		if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
			mColors.remove(mEditPosition);
			ColorItem updated = data.getParcelableExtra(ColorPickerActivity.EXTRA_COLOR_ITEM);
			mColors.add(updated);
			DataStorage.get(getContext()).updateColorItem(updated);
			((BaseAdapter)colorsListView.getAdapter()).notifyDataSetChanged();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList(EXTRA_COLOR_ITEMS, mColors);
		outState.putInt(EXTRA_EDIT_POSITION, mEditPosition);
		super.onSaveInstanceState(outState);
	}
}
