package com.example.administrator.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "note";
	public static final int VERSION = 1;
	public static final String CONTENT = "content";
	public static final String TIME = "time";
	public static final String ID = "_id";
	public NoteOpenHelper(Context context) {
		super(context, "note.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建数据库中表的方法
		db.execSQL("create table note " +
				"(_id integer primary key autoincrement , time varchar(20), content text not null);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
