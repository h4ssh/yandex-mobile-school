package ru.yandex.mobile_school.model;


import android.graphics.Color;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class NotesGenerator {

	private static final int COLOR_COMPONENT_MAX = 255;
	private static Random sRandom = new Random();

	private static List<String> sTitles = Arrays.asList(
			"Indian red", "Light coral", "Salmon", "Dark salmon", "Light salmon",
			"Crimson", "Red", "Fire brick", "Dark red", "Pink",
			"Light pink", "Hot pink",	"Deep pink",	"Medium violet red", "Pale violet red",
			"Light salmon", "Coral", "Tomato", "Orange red", "Dark orange",
			"Orange", "Gold", "Yellow", "Light yellow", "Lemon chiffon",
			"Light goldenrod yellow", "Papaya whip", "Moccasin", "Peach puff", "Pale goldenrod",
			"Khaki", "Dark khaki"
	);

	public static Note generate() {
		int red = sRandom.nextInt(COLOR_COMPONENT_MAX);
		int green = sRandom.nextInt(COLOR_COMPONENT_MAX);
		int blue = sRandom.nextInt(COLOR_COMPONENT_MAX);
		int color = Color.argb(COLOR_COMPONENT_MAX, red, green, blue);
		String title = sTitles.get(sRandom.nextInt(sTitles.size()));
		String description = String.format("Description of %s color", title.toLowerCase(Locale.getDefault()));
		return new Note(color, title, description);
	}

}
