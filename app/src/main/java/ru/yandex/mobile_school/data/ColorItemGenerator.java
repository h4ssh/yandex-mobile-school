package ru.yandex.mobile_school.data;


import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ColorItemGenerator {

	private static Random RANDOM = new Random();

	private static List<String> TITLES = Arrays.asList(
			"Indian red", "Light coral", "Salmon", "Dark salmon", "Light salmon",
			"Crimson", "Red", "Fire brick", "Dark red", "Pink",
			"Light pink", "Hot pink",	"Deep pink",	"Medium violet red", "Pale violet red",
			"Light salmon", "Coral", "Tomato", "Orange red", "Dark orange",
			"Orange", "Gold", "Yellow", "Light yellow", "Lemon chiffon",
			"Light goldenrod yellow", "Papaya whip", "Moccasin", "Peach puff", "Pale goldenrod",
			"Khaki", "Dark khaki"
	);

	public static ColorItem generate() {
		int red = RANDOM.nextInt(255);
		int green = RANDOM.nextInt(255);
		int blue = RANDOM.nextInt(255);
		int color = Color.argb(255, red, green, blue);
		String title = TITLES.get(RANDOM.nextInt(TITLES.size()));
		String description = String.format("Description of %s color", title.toLowerCase());
		return new ColorItem(color, title, description);
	}

}
