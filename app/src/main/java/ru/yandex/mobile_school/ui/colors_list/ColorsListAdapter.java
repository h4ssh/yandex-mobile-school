package ru.yandex.mobile_school.ui.colors_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.ui.views.ColorView;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.data.ColorItem;

public class ColorsListAdapter extends BaseAdapter {
	private final ArrayList<ColorItem> mColors;
	private final WeakReference<Context> mWeakContext;

	ColorsListAdapter(Context context, ArrayList<ColorItem> items) {
		mWeakContext = new WeakReference<>(context);
		mColors = items;
	}

	static class ViewHolder {
		@BindView(R.id.colors_list_item_color_view)
		ColorView mColorView;
		@BindView(R.id.colors_list_item_color_title)
		TextView mTitleView;
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
		ColorsListAdapter.ViewHolder viewHolder;

		if (convertView == null){
			LayoutInflater inflater = (LayoutInflater) mWeakContext.get()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.colors_list_item, parent, false);
			viewHolder = new ColorsListAdapter.ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ColorsListAdapter.ViewHolder) convertView.getTag();
		}

		ColorItem colorItem = (ColorItem) getItem(position);
		viewHolder.mColorView.setCurrentColor(colorItem.getColor());
		viewHolder.mTitleView.setText(colorItem.getTitle());
		viewHolder.mDescriptionView.setText(colorItem.getDescription());

		return convertView;
	}
}
