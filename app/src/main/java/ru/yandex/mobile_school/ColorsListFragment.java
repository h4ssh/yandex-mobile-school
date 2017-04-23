package ru.yandex.mobile_school;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class ColorsListFragment extends Fragment {

	private static final int REQUEST_CODE_ADD = 1;
	private static final int REQUEST_CODE_EDIT = 2;

	private ArrayList<ColorItem> mColors;
	private int editPosition = 0;

	@BindView(R.id.colors_list_fab)	FloatingActionButton addColorFAB;
	@BindView(R.id.colors_list_view) ListView colorsListView;

	static ColorsListFragment newInstance() {
		return new ColorsListFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mColors = new ArrayList<>();
		fillWithSampleData();
	}

	private void fillWithSampleData() {
		ColorItem crimson = new ColorItem(Color.parseColor("#DC143C"), "Crimson", "Crimson is a strong, red color, inclining to purple");
		ColorItem azure = new ColorItem(Color.parseColor("#007FFF"), "Azure", "Azure is a variation of blue that is often described as the color of the sky on a clear day");
		ColorItem aureolin = new ColorItem(Color.parseColor("#FDEE00"), "Aureolin", "Aureolin is a pigment sparingly used in oil and watercolor painting");
		mColors.add(crimson);
		mColors.add(azure);
		mColors.add(aureolin);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_colors_list, container, false);
		ButterKnife.bind(this, view);

		colorsListView.setAdapter(new ColorsListAdapter(mColors));
		colorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				editPosition = position;
				ColorItem item = mColors.get(position);
				startActivityForResult(ColorPickerActivity.newIntent(item), REQUEST_CODE_EDIT);
			}
		});

		addColorFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(ColorPickerActivity.newIntent(), REQUEST_CODE_ADD);
			}
		});

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
			ColorItem item = new ColorItem(
					data.getIntExtra(ColorPickerActivity.EXTRA_COLOR, Color.WHITE),
					data.getStringExtra(ColorPickerActivity.EXTRA_TITLE),
					data.getStringExtra(ColorPickerActivity.EXTRA_DESCR));
			mColors.add(item);
			((BaseAdapter)colorsListView.getAdapter()).notifyDataSetChanged();
		}
		if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
			ColorItem item = mColors.get(editPosition);
			item.setColor(data.getIntExtra(ColorPickerActivity.EXTRA_COLOR, item.getColor()));
			item.setTitle(data.getStringExtra(ColorPickerActivity.EXTRA_TITLE));
			item.setDescription(data.getStringExtra(ColorPickerActivity.EXTRA_DESCR));
			((BaseAdapter)colorsListView.getAdapter()).notifyDataSetChanged();
		}
	}
}
