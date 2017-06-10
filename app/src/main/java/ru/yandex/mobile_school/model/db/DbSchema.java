package ru.yandex.mobile_school.model.db;

public class DbSchema {
	public static final class ColorsTable {
		public static final String NAME = "colors";

		public static final class Cols {
			public static final String ID = "uuid";
			public static final String TITLE = "title";
			public static final String DESCRIPTION = "description";
			public static final String COLOR = "color";
			public static final String CREATED = "created";
			public static final String EDITED = "edited";
			public static final String VIEWED = "viewed";
			public static final String SERVER_ID = "server_id";
		}
	}

}
