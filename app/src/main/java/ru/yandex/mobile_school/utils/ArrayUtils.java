package ru.yandex.mobile_school.utils;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {
	public static <C> List<C> asList(SparseArray<C> sparseArray) {
		if (sparseArray == null) {
			return null;
		}
		List<C> arrayList = new ArrayList<>(sparseArray.size());
		for (int i = 0; i < sparseArray.size(); i++) {
			arrayList.add(sparseArray.valueAt(i));
		}
		return arrayList;
	}

	public static <T> ArrayList<T> toArrayList(List<T> list) {
		if (list == null) {
            return null;
        }
        ArrayList<T> arrayList = new ArrayList<T>(list.size());
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(list.get(i));
        }
        return arrayList;
	}
}
