package ru.yandex.mobile_school.ui.colors_list;

import android.content.Context;
import android.os.AsyncTask;

import ru.yandex.mobile_school.data.ColorItem;
import ru.yandex.mobile_school.data.ColorItemGenerator;
import ru.yandex.mobile_school.data.DataStorage;


public class ColorsListAsyncActor {

	private static final int GENERATOR_OBJECTS_COUNT = 1000;



	public interface ColorsListAsyncActorListener {
		void onItemsAddProgress(float percent);
		void onItemsAddFinish();
	}

	private ColorsListAsyncActorListener mListener;

	private ColorsListAdapter mListAdapter;
	private DataStorage mStorage;

	public ColorsListAsyncActor(Context context, ColorsListAdapter adapter) {
		mListAdapter = adapter;
		mStorage = DataStorage.get(context);
	}

	public void setListener(ColorsListAsyncActorListener listener) {
		mListener = listener;
	}

	public void generateItems() {
		AsyncTask task = new AsyncTask<Object, Float, Void>() {
			@Override
			protected Void doInBackground(Object... params) {
				for (int i = 0; i < GENERATOR_OBJECTS_COUNT; i++) {
					ColorItem item = ColorItemGenerator.generate();
					mStorage.addColorItem(item);
					publishProgress((float)i/GENERATOR_OBJECTS_COUNT);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Float... values) {
				if (mListener != null) {
					mListener.onItemsAddProgress(values[0]);
				}
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				if (mListener != null) {
					mListener.onItemsAddFinish();
				}
			}
		};
		task.execute();
	}

	/*
	private class AsyncBatchAdd extends AsyncTask<ArrayList<ColorItem>, Float, Boolean> {

		AsyncBatchAdd(Context context, ColorsListAdapterBatchAddListener callback) {
			mStorage = DataStorage.get(context);
			mCallback = callback;
		}

		private ColorsListAdapterBatchAddListener mCallback;
		private DataStorage mStorage;

		@Override
		protected Boolean doInBackground(ArrayList<ColorItem>... params) {
			int progress = 0;
			int total = params[0].size();
			for (ColorItem item: params[0]) {
				mStorage.addColorItem(item);
				progress++;
				publishProgress((float)progress / total);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Float... values) {
			if (mCallback != null) {
				mCallback.onAdapterBatchAddProgress(values[0]);
			}
		}

		@Override
		protected void onPostExecute(Boolean aBoolean) {
			super.onPostExecute(aBoolean);
			if (mCallback != null) {
				mCallback.onAdapterBatchAddFinish();
			}
		}
		*/
}
