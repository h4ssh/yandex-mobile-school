package ru.yandex.mobile_school;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hash on 23/04/2017.
 */

public class ColorsListAdapter extends BaseAdapter {

	private final ArrayList<ColorItem> mColors;

	ColorsListAdapter(ArrayList<ColorItem> items) {
		mColors = items;
	}

	static class ViewHolder {
		@BindView(R.id.colors_list_item_color_view) ColorView mColorView;
		@BindView(R.id.colors_list_item_color_title) TextView mTitleView;
		@BindView(R.id.colors_list_item_color_description) TextView mDescriptionView;
	}

	@Override
	public int getCount() {
		return mColors.size();
	}

	@Override
	public Object getItem(int position) {
		return mColors.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null){
			LayoutInflater inflater = (LayoutInflater) YMSApplication.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.colors_list_item, parent, false);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ColorItem colorItem = (ColorItem) getItem(position);
		viewHolder.mColorView.setCurrentColor(colorItem.getColor());
		viewHolder.mTitleView.setText(colorItem.getTitle());
		viewHolder.mDescriptionView.setText(colorItem.getDescription());

		return convertView;
	}
}
