package ru.yandex.mobile_school.views.notes_list;

import android.os.AsyncTask;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.views.custom.ColorView;
import ru.yandex.mobile_school.R;
import ru.yandex.mobile_school.utils.DateFilter;
import ru.yandex.mobile_school.utils.DateUtils;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder>
		implements Filterable {

	public interface AdapterAsyncActionsListener {
		void onSortFinish();
		void onFilterFinish();
	}

	public interface AdapterOnClickListener {
		void onClick(int position, Note note);
		void onLongClick(int position, Note note);
	}

	static final String SORT_PARAM_TITLE = "sort_param_title";
	static final String SORT_PARAM_CREATED = "sort_param_created";
	static final String SORT_PARAM_EDITED = "sort_param_edited";
	static final String SORT_PARAM_VIEWED = "sort_param_viewed";

	static final String FILTER_PARAM_CREATED = "filter_param_created";
	static final String FILTER_PARAM_EDITED = "filter_param_edited";
	static final String FILTER_PARAM_VIEWED = "filter_param_viewed";

	private ArrayList<Note> mNotes;
	private ArrayList<Note> mFiltered;
	private ItemFilter mItemFilter = new ItemFilter();
	private String mSortParam;
	private boolean mSortAscending;
	private AdapterAsyncActionsListener mListener;
	private AdapterOnClickListener mClickListener;

	NotesListAdapter(ArrayList<Note> items) {
		mNotes = items;
		mFiltered = items;
	}

	public void setAdapterSortListener(AdapterAsyncActionsListener listener) {
		mListener = listener;
	}

	public void setAdapterOnClickListener(AdapterOnClickListener listener) {
		mClickListener = listener;
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.notes_list_item_color_view) ColorView mColorView;
		@BindView(R.id.notes_list_item_note_title) TextView mTitleView;
		@BindView(R.id.notes_list_item_note_description) TextView mDescriptionView;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);

			if (mClickListener != null) {
				itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int position = getAdapterPosition();
						Note item = getColorItem(position);
						mClickListener.onClick(position, item);
					}
				});
				itemView.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						int position = getAdapterPosition();
						Note item = getColorItem(position);
						mClickListener.onLongClick(position, item);
						return true;
					}
				});
			}
		}
	}

	/*
	ListView's Base Adapter with ViewHolder

	@Override
	public int getCount() {
		return mFiltered.size();
	}

	@Override
	public Object getItem(int position) {
		return mFiltered.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NotesListAdapter.ViewHolder viewHolder;

		if (convertView == null){
			LayoutInflater inflater = (LayoutInflater) mWeakContext.get()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.notes_list_item, parent, false);
			viewHolder = new NotesListAdapter.ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (NotesListAdapter.ViewHolder) convertView.getTag();
		}

		Note colorItem = (Note) getItem(position);
		viewHolder.mColorView.setCurrentColor(colorItem.getColor());
		viewHolder.mTitleView.setText(colorItem.getTitle());
		viewHolder.mDescriptionView.setText(colorItem.getDescription());

		return convertView;
	}
*/
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.notes_list_item,	parent,	false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Note note = mFiltered.get(position);
		holder.mColorView.setCurrentColor(note.getColor());
		holder.mTitleView.setText(note.getTitle());
		holder.mDescriptionView.setText(note.getDescription());
		ViewCompat.setTransitionName(holder.mColorView, note.getId().toString());
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return mFiltered.size();
	}

	public void sortBy(final String sortParam, final boolean ascending) {
		mSortParam = sortParam;
		mSortAscending = ascending;
		AsyncTask task = new AsyncTask<Object, Float, Void>() {

			@Override
			protected Void doInBackground(Object[] params) {
				Collections.sort(mFiltered, new Comparator<Note>() {
					@Override
					public int compare(Note c1, Note c2) {
						switch (sortParam) {
							case SORT_PARAM_TITLE:
								if (ascending) {
									return c1.getTitle().compareToIgnoreCase(c2.getTitle());
								} else {
									return c2.getTitle().compareToIgnoreCase(c1.getTitle());
								}
							case SORT_PARAM_CREATED:
								if (ascending) {
									return c1.getCreatedDate().compareTo(c2.getCreatedDate());
								} else {
									return  c2.getCreatedDate().compareTo(c1.getCreatedDate());
								}
							case SORT_PARAM_EDITED:
								if (ascending) {
									return c1.getEditedDate().compareTo(c2.getEditedDate());
								} else {
									return c2.getEditedDate().compareTo(c1.getEditedDate());
								}
							case SORT_PARAM_VIEWED:
								if (ascending) {
									return c1.getViewedDate().compareTo(c2.getViewedDate());
								} else {
									return c2.getViewedDate().compareTo(c1.getViewedDate());
								}
							default:
								return 0;
						}

					}
				});
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				notifyItemRangeChanged(0, mFiltered.size());
				if (mListener != null) {
					mListener.onSortFinish();
				}
			}
		};
		task.execute();
	}

	public void resort() {
		if (mSortParam != null) {
			sortBy(mSortParam, mSortAscending);
		} else  if (mListener != null) {
			mListener.onSortFinish();
		}
	}

	public void search(String query) {
		String lowerQuery = query.toLowerCase(Locale.getDefault());
		ArrayList<Note> filtered = new ArrayList<>();
		for (int i = 0; i < mNotes.size(); i++) {
			Note item = mNotes.get(i);
			if (item.getTitle().toLowerCase(Locale.getDefault()).contains(lowerQuery) ||
					item.getDescription().toLowerCase(Locale.getDefault()).contains(lowerQuery)) {
				filtered.add(item);
			}
		}
		mFiltered = filtered;
		notifyDataSetChanged();
	}

	@Override
	public Filter getFilter() {
		return mItemFilter;
	}

	public void addItem(Note item) {
		mNotes.add(item);
		if (mFiltered != mNotes) {
			mFiltered.add(item);
		}
		notifyDataSetChanged();
	}

	public void changeData(ArrayList<Note> items) {
		mNotes = items;
		mFiltered = items;
		notifyDataSetChanged();
	}

	public void deleteItem(Note item) {
		mNotes.remove(item);
		mFiltered.remove(item);
		notifyDataSetChanged();
	}

	public void resetFilters() {
		mFiltered = mNotes;
		notifyDataSetChanged();
	}

	public Note getColorItem(int position) {
		return mFiltered.get(position);
	}

	public Note getColorItem(UUID id) {
		for (Note item: mNotes) {
			if (item.getId().equals(id)) {
				return item;
			}
		}
		return null;
	}

	public 	void filter(String paramName, Date startDate, Date endDate) {
		AsyncTask task = new AsyncTask<String, Void, Void>() {
			@Override
			protected Void doInBackground(String... params) {
				getFilter().filter(params[0]);
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				if (mListener != null) {
					mListener.onFilterFinish();
				}
			}
		};
		task.execute(new String[] {DateUtils.getFilterString(paramName, startDate, endDate)});
	}

	private class ItemFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();

			DateFilter filter = DateUtils.getDateFilter(constraint.toString());
			ArrayList<Note> filtered = new ArrayList<>();
			for (int i = 0; i < mNotes.size(); i++) {
				switch (filter.getParamName()) {
					case FILTER_PARAM_CREATED:
						if (filter.match(mNotes.get(i).getCreatedDate())) {
							filtered.add(mNotes.get(i));
						}
						break;
					case FILTER_PARAM_EDITED:
						if (filter.match(mNotes.get(i).getEditedDate())) {
							filtered.add(mNotes.get(i));
						}
						break;
					case FILTER_PARAM_VIEWED:
						if (filter.match(mNotes.get(i).getViewedDate())) {
							filtered.add(mNotes.get(i));
						}
						break;
				}
			}
			results.count = filtered.size();
			results.values = filtered;
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			mFiltered = (ArrayList<Note>) results.values;
			notifyDataSetChanged();
		}
	}
}
