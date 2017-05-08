package ru.yandex.mobile_school.ui.colors_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.ui.views.ColorView;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.data.ColorItem;
import ru.yandex.mobile_school.utils.DateFilter;
import ru.yandex.mobile_school.utils.DateUtils;

public class ColorsListAdapter extends BaseAdapter implements Filterable {
	private ArrayList<ColorItem> mColors;
	private ArrayList<ColorItem> mFiltered;
	private ItemFilter mItemFilter = new ItemFilter();
	private final WeakReference<Context> mWeakContext;

	static final String SORT_PARAM_TITLE = "sort_param_title";
	static final String SORT_PARAM_CREATED = "sort_param_created";
	static final String SORT_PARAM_EDITED = "sort_param_edited";
	static final String SORT_PARAM_VIEWED = "sort_param_viewed";

	static final String FILTER_PARAM_CREATED = "filter_param_created";
	static final String FILTER_PARAM_EDITED = "filter_param_edited";
	static final String FILTER_PARAM_VIEWED = "filter_param_viewed";


	ColorsListAdapter(Context context, ArrayList<ColorItem> items) {
		mWeakContext = new WeakReference<>(context);
		mColors = items;
		mFiltered = items;
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
		return mFiltered.size();
	}

	@Override
	public Object getItem(int position) {
		return mFiltered.get(position);
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

	public void sortBy(final String sortParam, final boolean ascending) {
		Collections.sort(mFiltered, new Comparator<ColorItem>() {
			@Override
			public int compare(ColorItem c1, ColorItem c2) {
				switch (sortParam) {
					case SORT_PARAM_TITLE:
						if (ascending) return c1.getTitle().compareToIgnoreCase(c2.getTitle());
						else return c2.getTitle().compareToIgnoreCase(c1.getTitle());
					case SORT_PARAM_CREATED:
						if (ascending) return c1.getCreatedDate().compareTo(c2.getCreatedDate());
						else return  c2.getCreatedDate().compareTo(c1.getCreatedDate());
					case SORT_PARAM_EDITED:
						if (ascending) return c1.getEditedDate().compareTo(c2.getEditedDate());
						else return c2.getEditedDate().compareTo(c1.getEditedDate());
					case SORT_PARAM_VIEWED:
						if (ascending) return c1.getViewedDate().compareTo(c2.getViewedDate());
						else return c2.getViewedDate().compareTo(c1.getViewedDate());
					default:
						return 0;
				}

			}
		});
		notifyDataSetChanged();
	}

	public void search(String query) {
		query = query.toLowerCase();
		ArrayList<ColorItem> filtered = new ArrayList<>();
		for (int i = 0; i < mColors.size(); i++) {
			ColorItem item = mColors.get(i);
			if (item.getTitle().toLowerCase().contains(query) ||
					item.getDescription().toLowerCase().contains(query))
				filtered.add(item);
		}
		mFiltered = filtered;
		notifyDataSetChanged();
	}

	@Override
	public Filter getFilter() {
		return mItemFilter;
	}

	public void changeData(ArrayList<ColorItem> items) {
		mColors = items;
		mFiltered = items;
		notifyDataSetChanged();
	}

	public void deleteItem(ColorItem item) {
		mColors.remove(item);
		mFiltered.remove(item);
		notifyDataSetChanged();
	}

	public void resetFilters() {
		mFiltered = mColors;
		notifyDataSetChanged();
	}

	public ColorItem getColorItem(int position) {
		return mFiltered.get(position);
	}

	private class ItemFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			DateFilter filter = DateUtils.getDateFilter(constraint.toString());
			ArrayList<ColorItem> filtered = new ArrayList<>();
			for (int i = 0; i < mColors.size(); i++) {
				if (filter.getParamName().equals(FILTER_PARAM_CREATED)) {
					if (filter.match(mColors.get(i).getCreatedDate())) {
						filtered.add(mColors.get(i));
					}
				} else
				if (filter.getParamName().equals(FILTER_PARAM_EDITED)) {
					if (filter.match(mColors.get(i).getEditedDate())) {
						filtered.add(mColors.get(i));
					}
				} else
				if (filter.getParamName().equals(FILTER_PARAM_VIEWED)) {
					if (filter.match(mColors.get(i).getViewedDate())) {
						filtered.add(mColors.get(i));
					}
				}
			}
			results.count = filtered.size();
			results.values = filtered;
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			mFiltered = (ArrayList<ColorItem>)results.values;
			notifyDataSetChanged();
		}
	}
}
