package ru.yandex.mobile_school.model;

import android.graphics.Color;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.UUID;

import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.model.dto.NoteDTO;

public class NoteJsonAdapter {
	@FromJson
	Note noteFromJson(NoteDTO json) {
		return new Note(
				json.getExtra(),
				Color.parseColor(json.getColor()),
				json.getTitle(),
				json.getDescription(),
				json.getCreated(),
				json.getEdited(),
				json.getViewed(),
				json.getId());
	}

	@ToJson
	NoteDTO noteToJson(Note note) {
		NoteDTO noteDTO = new NoteDTO();
        noteDTO.setExtra(note.getId().toString());
		noteDTO.setColor(note.getColorAsHexString());
		noteDTO.setTitle(note.getTitle());
		noteDTO.setDescription(note.getDescription());
		noteDTO.setCreated(note.getCreated());
		noteDTO.setEdited(note.getEdited());
		noteDTO.setViewed(note.getViewed());
		noteDTO.setId(note.getServerId());
		return noteDTO;
	}
}
