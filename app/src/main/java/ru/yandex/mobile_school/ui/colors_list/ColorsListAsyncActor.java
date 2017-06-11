package ru.yandex.mobile_school.ui.colors_list;

import android.content.Context;
import android.os.AsyncTask;

import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.model.NotesGenerator;
import ru.yandex.mobile_school.model.StorageModel;


public class ColorsListAsyncActor {

	public interface ColorsListAsyncActorListener {
		void onItemsAddFinish();
		void onItemsAddProgress(int percent);
//		void onItemsExportFinish(boolean result);
//		void onItemsImportFinish(boolean result);
	}

	private ColorsListAsyncActorListener mListener;

	private StorageModel mStorage;

	public ColorsListAsyncActor(Context context) {
		mStorage = StorageModel.get(context);
	}

	public void setListener(ColorsListAsyncActorListener listener) {
		mListener = listener;
	}

	public void generateItems(final int quantity) {
		AsyncTask task = new AsyncTask<Object, Integer, Void>() {

			private int oldProgress = 0;
			private int newProgress = 0;

			@Override
			protected Void doInBackground(Object... params) {
				for (int i = 0; i < quantity; i++) {
					Note item = NotesGenerator.generate();
					mStorage.addColorItem(item);
					newProgress = 100 * i / quantity;
					if (newProgress > oldProgress) {
						oldProgress = newProgress;
						publishProgress(newProgress);
					}
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
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

//	public void exportItems(final String destination) {
//		AsyncTask task = new AsyncTask<Object, Float, Boolean>() {
//			@Override
//			protected Boolean doInBackground(Object... params) {
//				return mStorage.exportColorItems(destination);
//			}
//
//			@Override
//			protected void onPostExecute(Boolean aBoolean) {
//				if (mListener != null) {
//					mListener.onItemsExportFinish(aBoolean);
//				}
//			}
//		};
//		task.execute();
//	}
//
//	public void importItems(final String source) {
//		AsyncTask<Void, Float, Boolean> task = new AsyncTask<Void, Float, Boolean>() {
//			@Override
//			protected Boolean doInBackground(Void... params) {
//				return mStorage.importColorItems(source);
//			}
//
//			@Override
//			protected void onPostExecute(Boolean aBoolean) {
//				if (mListener != null) {
//					mListener.onItemsImportFinish(aBoolean);
//				}
//			}
//		};
//		task.execute();
//	}


}
