package ru.yandex.mobile_school.views.notes_list;

import android.os.AsyncTask;

import javax.inject.Inject;

import ru.yandex.mobile_school.App;
import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.model.NotesGenerator;
import ru.yandex.mobile_school.model.StorageModel;


public class NotesListAsyncActor {

	@Inject
	StorageModel storage;

	public interface NotesListAsyncActorListener {
		void onItemsAddFinish();
		void onItemsAddProgress(int percent);
	}

	private NotesListAsyncActorListener mListener;

    public NotesListAsyncActor() {
        App.getComponent().inject(this);
    }

	public void setListener(NotesListAsyncActorListener listener) {
		mListener = listener;
	}

	public void generateItems(final int quantity) {
		AsyncTask task = new AsyncTask<Object, Integer, Void>() {

			private int oldProgress = 0;
			private int newProgress = 0;

			@Override
			protected Void doInBackground(Object... params) {
				for (int i = 0; i < quantity; i++) {
					Note note = NotesGenerator.generate();
					storage.addNote(note);
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
}
