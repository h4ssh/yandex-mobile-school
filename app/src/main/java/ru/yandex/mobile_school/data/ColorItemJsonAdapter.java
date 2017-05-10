package ru.yandex.mobile_school.data;

import android.graphics.Color;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.UUID;

public class ColorItemJsonAdapter {
	@FromJson ColorItem colorItemFromJson(ColorItemJson json) {
		return new ColorItem(
				UUID.randomUUID().toString(),
				Color.parseColor(json.color),
				json.title,
				json.description,
				json.created,
				json.edited,
				json.viewed);
	}

	@ToJson ColorItemJson colorItemToJson(ColorItem colorItem) {
		ColorItemJson itemJson = new ColorItemJson();
		itemJson.color = colorItem.getColorAsHexString();
		itemJson.title = colorItem.getTitle();
		itemJson.description = colorItem.getDescription();
		itemJson.created = colorItem.getCreated();
		itemJson.edited = colorItem.getEdited();
		itemJson.viewed = colorItem.getViewed();
		return itemJson;
	}
}
