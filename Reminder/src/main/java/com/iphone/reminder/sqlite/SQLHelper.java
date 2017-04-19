package com.iphone.reminder.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iphone.reminder.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLHelper {

	private static String SQL_NAME = "reminder_db_test2";
	private static String TABLE_NAME_ONE = "table_one";
	private static String TABLE_NAME_TWO = "table_two";
    private static String TABLE_NAME_THREE = "table_three";
    private static String TABLE_NAME_FOUR = "table_four";
    private static String TABLE_NAME = TABLE_NAME_ONE;

    /**
     * 创建数据库
     */
    public static void createSql(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, SQL_NAME);
        @SuppressWarnings("unused")
        SQLiteDatabase db = dbHelper.getReadableDatabase();
    }

	// 插入数据
    public static void insertSqlite(Context context, String title, String time,
                                    String location, String note, int tableCount, int repeatNum, int isCheck) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("time", time);
        values.put("location", location);
        values.put("note", note);
        values.put("repeat", repeatNum);
        values.put("is_check", isCheck);
        DatabaseHelper dbHelper = new DatabaseHelper(context, SQL_NAME);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(getTableName(tableCount), null, values);
        db.close();
	}

	// 插入单项数据
	public static void insertNumber(Context context, String title,int tableCount ) {
		ContentValues values = new ContentValues();
		values.put("title", title);
		DatabaseHelper dbHelper = new DatabaseHelper(context, SQL_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(getTableName(tableCount), null, values);
		db.close();
	}

    // 修改是否选中
    public static void updateIsCheckDetail(Context context, String id,int tableCount, int isCheck) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, SQL_NAME);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_check", isCheck);
        db.update(getTableName(tableCount), values, "_id=?", new String[]{id});
        db.close();
    }

    // 修改
    public static void updateNewDetail(Context context, String id, String newTitle, String newTime, String newLocation, String newNote, int tableCount, int newRepeatNum, int isCheck) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, SQL_NAME);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", newTitle);
        values.put("time", newTime);
        values.put("location", newLocation);
        values.put("note", newNote);
        values.put("repeat", newRepeatNum);
        values.put("is_check", isCheck);
        db.update(getTableName(tableCount), values, "_id=?", new String[]{id});
        db.close();
    }

	  /**  
     * 查，查询表中所有的数据  
     */  
    public static List<Map<String, String>> queryAllMessage(Context context,int tableCount) {  
    	
    	List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
    	DatabaseHelper dbHelper = new DatabaseHelper(context, SQL_NAME);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(getTableName(tableCount), null, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String _id = cursor.getString(cursor.getColumnIndex("_id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String repeat = cursor.getString(cursor.getColumnIndex("repeat"));
                String isCheck = cursor.getString(cursor.getColumnIndex("is_check"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ID", _id);
                map.put("TITLE", title);
                map.put("TIME", time);
                map.put("LOCATION", location);
                map.put("NOTE", note);
                map.put("REPEAT", repeat);
                map.put("ISCHECK", isCheck);
                tempList.add(map);
                
            }  
            db.close();
        }  
        return tempList;
    }  
    
    /**
     * 在数据库中搜索关键字
     */
    public static List<Map<String, String>> queryAllTitle(Context context,
                                                          String keywords) {
        List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
        DatabaseHelper dbHelper = new DatabaseHelper(context, SQL_NAME);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursorOne = db.query(TABLE_NAME_ONE, null, null, null, null,
                null, null);
        Cursor cursorTwo = db.query(TABLE_NAME_TWO, null, null, null, null,
                null, null);

        Cursor cursorThree = db.query(TABLE_NAME_THREE, null, null, null, null,
                null, null);

        Cursor cursorFour = db.query(TABLE_NAME_FOUR, null, null, null, null,
                null, null);
        while (cursorOne.moveToNext()) {// 表一
            String _id = cursorOne.getString(cursorOne.getColumnIndex("_id"));
            String title = cursorOne.getString(cursorOne.getColumnIndex("title"));
            String time = cursorOne.getString(cursorOne.getColumnIndex("time"));
            String location = cursorOne.getString(cursorOne.getColumnIndex("location"));
            String note = cursorOne.getString(cursorOne.getColumnIndex("note"));
            String repeat = cursorOne.getString(cursorOne.getColumnIndex("repeat"));
            String isCheck = cursorOne.getString(cursorOne.getColumnIndex("is_check"));
            if (Utils.iskeywordsSearched(title, keywords)) {// 判斷是否符合搜索
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ID", _id);
                map.put("TITLE", title);
                map.put("TIME", time);
                map.put("LOCATION", location);
                map.put("NOTE", note);
                map.put("REPEAT", repeat);
                map.put("ISCHECK", isCheck);
                tempList.add(map);
            } else {

            }
        }

        while (cursorTwo.moveToNext()) {// 表二
            String _id = cursorTwo.getString(cursorTwo.getColumnIndex("_id"));
            String title = cursorTwo.getString(cursorTwo
                    .getColumnIndex("title"));
            String time = cursorTwo.getString(cursorTwo.getColumnIndex("time"));
            String location = cursorTwo.getString(cursorTwo
                    .getColumnIndex("location"));
            String note = cursorTwo.getString(cursorTwo.getColumnIndex("note"));
            String repeat = cursorTwo.getString(cursorTwo.getColumnIndex("repeat"));
            String isCheck = cursorTwo.getString(cursorTwo.getColumnIndex("is_check"));
            if (Utils.iskeywordsSearched(title, keywords)) {// 判斷是否符合搜索
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ID", _id);
                map.put("TITLE", title);
                map.put("TIME", time);
                map.put("LOCATION", location);
                map.put("NOTE", note);
                map.put("REPEAT", repeat);
                map.put("ISCHECK", isCheck);
                tempList.add(map);
            } else {

            }
        }

        while (cursorThree.moveToNext()) {// 表三
            String _id = cursorThree.getString(cursorThree.getColumnIndex("_id"));
            String title = cursorThree.getString(cursorThree
                    .getColumnIndex("title"));
            String time = cursorThree.getString(cursorThree.getColumnIndex("time"));
            String location = cursorThree.getString(cursorThree
                    .getColumnIndex("location"));
            String note = cursorThree.getString(cursorThree.getColumnIndex("note"));
            String repeat = cursorThree.getString(cursorThree.getColumnIndex("repeat"));
            String isCheck = cursorThree.getString(cursorThree.getColumnIndex("is_check"));
            if (Utils.iskeywordsSearched(title, keywords)) {// 判斷是否符合搜索
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ID", _id);
                map.put("TITLE", title);
                map.put("TIME", time);
                map.put("LOCATION", location);
                map.put("NOTE", note);
                map.put("REPEAT", repeat);
                map.put("ISCHECK", isCheck);
                tempList.add(map);
            } else {

            }
        }


        while (cursorFour.moveToNext()) {// 表四
            String _id = cursorFour.getString(cursorFour.getColumnIndex("_id"));
            String title = cursorFour.getString(cursorFour
                    .getColumnIndex("title"));
            String time = cursorFour.getString(cursorFour.getColumnIndex("time"));
            String location = cursorFour.getString(cursorFour
                    .getColumnIndex("location"));
            String note = cursorFour.getString(cursorFour.getColumnIndex("note"));
            String repeat = cursorFour.getString(cursorFour.getColumnIndex("repeat"));
            String isCheck = cursorFour.getString(cursorFour.getColumnIndex("is_check"));
            if (Utils.iskeywordsSearched(title, keywords)) {// 判斷是否符合搜索
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ID", _id);
                map.put("TITLE", title);
                map.put("TIME", time);
                map.put("LOCATION", location);
                map.put("NOTE", note);
                map.put("REPEAT", repeat);
                map.put("ISCHECK", isCheck);
                tempList.add(map);
            } else {

            }
        }

        db.close();
        return tempList;
    }


    /**
     * 全部4张表数据
     */
    public static List<Map<String, String>> queryAllData(Context context) {
        List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
        DatabaseHelper dbHelper = new DatabaseHelper(context, SQL_NAME);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursorOne = db.query(TABLE_NAME_ONE, null, null, null, null,
                null, null);
        Cursor cursorTwo = db.query(TABLE_NAME_TWO, null, null, null, null,
                null, null);

        Cursor cursorThree = db.query(TABLE_NAME_THREE, null, null, null, null,
                null, null);

        Cursor cursorFour = db.query(TABLE_NAME_FOUR, null, null, null, null,
                null, null);

        while (cursorTwo.moveToNext()) {// 表二
            String _id = cursorTwo.getString(cursorTwo.getColumnIndex("_id"));
            String title = cursorTwo.getString(cursorTwo
                    .getColumnIndex("title"));
            String time = cursorTwo.getString(cursorTwo.getColumnIndex("time"));
            String location = cursorTwo.getString(cursorTwo
                    .getColumnIndex("location"));
            String note = cursorTwo.getString(cursorTwo.getColumnIndex("note"));
            String repeat = cursorTwo.getString(cursorTwo.getColumnIndex("repeat"));
            String isCheck = cursorTwo.getString(cursorTwo.getColumnIndex("is_check"));
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ID", _id);
            map.put("TITLE", title);
            map.put("TIME", time);
            map.put("LOCATION", location);
            map.put("NOTE", note);
            map.put("REPEAT", repeat);
            map.put("ISCHECK", isCheck);

            tempList.add(map);
        }

        while (cursorThree.moveToNext()) {// 表三
            String _id = cursorThree.getString(cursorThree.getColumnIndex("_id"));
            String title = cursorThree.getString(cursorThree
                    .getColumnIndex("title"));
            String time = cursorThree.getString(cursorThree.getColumnIndex("time"));
            String location = cursorThree.getString(cursorThree
                    .getColumnIndex("location"));
            String note = cursorThree.getString(cursorThree.getColumnIndex("note"));
            String repeat = cursorThree.getString(cursorThree.getColumnIndex("repeat"));
            String isCheck = cursorThree.getString(cursorThree.getColumnIndex("is_check"));

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ID", _id);
            map.put("TITLE", title);
            map.put("TIME", time);
            map.put("LOCATION", location);
            map.put("NOTE", note);
            map.put("REPEAT", repeat);
            map.put("ISCHECK", isCheck);

            tempList.add(map);

        }


        while (cursorFour.moveToNext()) {// 表四
            String _id = cursorFour.getString(cursorFour.getColumnIndex("_id"));
            String title = cursorFour.getString(cursorFour
                    .getColumnIndex("title"));
            String time = cursorFour.getString(cursorFour.getColumnIndex("time"));
            String location = cursorFour.getString(cursorFour
                    .getColumnIndex("location"));
            String note = cursorFour.getString(cursorFour.getColumnIndex("note"));
            String repeat = cursorFour.getString(cursorFour.getColumnIndex("repeat"));
            String isCheck = cursorFour.getString(cursorFour.getColumnIndex("is_check"));

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ID", _id);
            map.put("TITLE", title);
            map.put("TIME", time);
            map.put("LOCATION", location);
            map.put("NOTE", note);
            map.put("REPEAT", repeat);
            map.put("ISCHECK", isCheck);

            tempList.add(map);
        }

        while (cursorOne.moveToNext()) {// 表一放最后保证总表添加数据在最后
            String _id = cursorOne.getString(cursorOne.getColumnIndex("_id"));
            String title = cursorOne.getString(cursorOne.getColumnIndex("title"));
            String time = cursorOne.getString(cursorOne.getColumnIndex("time"));
            String location = cursorOne.getString(cursorOne.getColumnIndex("location"));
            String note = cursorOne.getString(cursorOne.getColumnIndex("note"));
            String repeat = cursorOne.getString(cursorOne.getColumnIndex("repeat"));
            String isCheck = cursorOne.getString(cursorOne.getColumnIndex("is_check"));

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ID", _id);
            map.put("TITLE", title);
            map.put("TIME", time);
            map.put("LOCATION", location);
            map.put("NOTE", note);
            map.put("REPEAT", repeat);
            map.put("ISCHECK", isCheck);

            tempList.add(map);
        }

        db.close();
        return tempList;
    }

    // 删除某条消息
    public static void deleteDetailDate(Context context, String id, int tableCount) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, SQL_NAME);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(getTableName(tableCount), "_id=?", new String[]{id});
        db.close();
    }

    public static void deleteAll(Context context, int tableCount) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, SQL_NAME);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(getTableName(tableCount), null, null);
        db.close();
    }

    public static String getTableName(int tableCount) {
        switch (tableCount) {
            case 1:
                TABLE_NAME = TABLE_NAME_ONE;
                break;
            case 2:
                TABLE_NAME = TABLE_NAME_TWO;
                break;
            case 3:
                TABLE_NAME = TABLE_NAME_THREE;
                break;
            case 4:
                TABLE_NAME = TABLE_NAME_FOUR;
                break;
        }
        return TABLE_NAME;
    }
}