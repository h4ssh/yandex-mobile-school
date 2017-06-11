package ru.yandex.mobile_school.views.notes_list;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import ru.yandex.mobile_school.model.StorageModel;

public class NotesListLooperThread extends HandlerThread {

	public static final int WHAT_EXPORT = 1;
	public static final int WHAT_IMPORT = 2;

	private Handler mWorkerHandler;
	private Handler mResponseHandler;
	private static final String TAG = NotesListLooperThread.class.getSimpleName();
	private Callback mCallback;
	private Context mContext;

	public interface Callback {
		void onColorsExported(boolean result);
		void onColorsImported(boolean result);
	}

	public NotesListLooperThread(Context context, Handler responseHandler, Callback callback) {
		super(TAG);
		mResponseHandler = responseHandler;
		mCallback = callback;
		mContext = context;
	}

	public void queueTask(String path, int what) {
		mWorkerHandler.obtainMessage(what, path)
				.sendToTarget();
	}

	public void prepareHandler() {
		mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				String path = (String) msg.obj;
				handleRequest(path, msg.what);
				return true;
			}
		});
	}

	private void handleRequest(final String path, final int what) {
		final boolean result;
		if (what == WHAT_EXPORT) {
			result = StorageModel.get(mContext).exportColorItems(path);
		} else {
			result = what == WHAT_IMPORT && StorageModel.get(mContext).importColorItems(path);
		}

		mResponseHandler.post(new Runnable() {
			@Override
			public void run() {
				if (what == WHAT_EXPORT) {
					mCallback.onColorsExported(result);
				} else if (what == WHAT_IMPORT) {
					mCallback.onColorsImported(result);
				}
			}
		});
	}
}
