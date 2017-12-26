package com.example.administrator.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.administrator.note.bean.NoteBean;
import java.util.List;

public class NoteSaveDao {
	private NoteOpenHelper noteopenhelper;
	private SQLiteDatabase dbReader;
	private SQLiteDatabase dbWriter;
	//BlackNumberDao单例模式
	//1,私有化构造方法
	private NoteSaveDao(Context context){
		//创建数据库已经其表机构
		noteopenhelper = new NoteOpenHelper(context);
		//创建打开一个数据库
		dbReader = noteopenhelper.getReadableDatabase();
		dbWriter = noteopenhelper.getWritableDatabase();
	}
	//2,声明一个当前类的对象
	private static NoteSaveDao noteSaveDao = null;
	//3,提供一个静态方法,如果当前类的对象为空,创建一个新的
	public static NoteSaveDao getInstance(Context context){
		if(noteSaveDao == null){
			noteSaveDao = new NoteSaveDao(context);
		}
		return noteSaveDao;
	}

	public void deleteAll(){
		SQLiteDatabase db = noteopenhelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("SELECT time  FROM note WHERE  time != 'sqlite_sequence'", null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					db.execSQL("DROP TABLE " + cursor.getString(0));
				}
			}
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}

	}
	

	public void insert(String time,String content){
		ContentValues values = new ContentValues();
		values.put(NoteOpenHelper.TIME, time);
		values.put(NoteOpenHelper.CONTENT, content);
		dbWriter.insert(NoteOpenHelper.TABLE_NAME, null, values);
	}
	

	public void delete(int noteID){
		dbWriter.delete(NoteOpenHelper.TABLE_NAME,"_id = ?" ,new String[]{noteID + ""});
	}


	public void update(int noteID,String content,String time){
		ContentValues cv = new ContentValues();
		cv.put(NoteOpenHelper.ID,noteID);
		cv.put(NoteOpenHelper.CONTENT, content);
		cv.put(NoteOpenHelper.TIME, time);
		dbWriter.update(NoteOpenHelper.TABLE_NAME,cv,"_id = ?",new String[]{noteID + ""});
	}
	
	public void findAll(List<NoteBean> noteList){

		Cursor cursor = dbReader.query(NoteOpenHelper.TABLE_NAME,null, null, null, null, null,"_id desc");
		try{
			while (cursor.moveToNext()){
				NoteBean note = new NoteBean();
				note.setId(cursor.getInt(cursor.getColumnIndex(NoteOpenHelper.ID)));
				note.setContent(cursor.getString(cursor.getColumnIndex(NoteOpenHelper.CONTENT)));
				note.setTime(cursor.getString(cursor.getColumnIndex(NoteOpenHelper.TIME)));
				noteList.add(note);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public NoteBean getWenben(int noteId){
		Cursor cursor = dbReader.rawQuery("SELECT * FROM note WHERE _id = ?", new String[]{noteId + ""});
		cursor.moveToFirst();
		NoteBean note = new NoteBean();
		note.setId(cursor.getInt(cursor.getColumnIndex(NoteOpenHelper.ID)));
		note.setContent(cursor.getString(cursor.getColumnIndex(NoteOpenHelper.CONTENT)));
		return note;
	}


}
