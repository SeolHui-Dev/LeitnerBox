package SeolHui.leitnerbox;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import SeolHui.leitnerbox.Data.Box;
import SeolHui.leitnerbox.Data.DataItem;
import SeolHui.leitnerbox.Data.Word;
import SeolHui.leitnerbox.Data.WordView;


public class DBManager extends SQLiteOpenHelper {


    public static final String dbName = "d1.db";
    public static int dbVersion = 1; // 데이터베이스 버전
    boolean isCreating = false;
    SQLiteDatabase currentDB = null;
    public static  Context context;

    public DBManager(Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
        makeTable();
        Init();
    }

    public DBManager(Context context, int dbVersion) {
        super(context, dbName, null, dbVersion);
        this.context = context;
        makeTable();
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Word(INCNO INTEGER PRIMARY KEY AUTOINCREMENT, data TEXT,box_INCNO INTEGER,seqno INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Box(INCNO INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,wordView_INCNO INTEGER );");
        db.execSQL("CREATE TABLE IF NOT EXISTS WordView(INCNO INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,seqno INTEGER);");

    }


    void makeTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS Word(INCNO INTEGER PRIMARY KEY AUTOINCREMENT, data TEXT,box_INCNO INTEGER,seqno INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Box(INCNO INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,wordView_INCNO INTEGER );");
        db.execSQL("CREATE TABLE IF NOT EXISTS WordView(INCNO INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,seqno INTEGER);");
        db.close();

    }

    void Init(){
/*        int count = getCount("복습주기리스트");
        if(count <= 0){
            Insert(new 복습주기리스트(1, "기본"));
        }
        count = getCount("복습주기");
        if(count <= 0){
            Insert(new 복습주기(1, "기본", "3/6/13"));
        }
        count = getCount("테마");
        if(count <= 0){
            InsertColor("pupple");
        }*/
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        // TODO Auto-generated method stub
        if (isCreating && currentDB != null) {
            return currentDB;
        }
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        // TODO Auto-generated method stub
        if (isCreating && currentDB != null) {
            return currentDB;
        }
        return super.getReadableDatabase();
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        currentDB = null;
        isCreating = false;
    }

    public void allDrop() {
        SQLiteDatabase db = getWritableDatabase();
// query to obtain the names of all tables in your database
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();

// iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            tables.add(c.getString(0));
        }

// call DROP TABLE on every table name
        for (String table : tables) {
            if (table.compareTo("sqlite_sequence") == 0 || table.compareTo("android_metadata") == 0) {

            } else {
                String dropQuery = "DROP TABLE IF EXISTS " + table;
                db.execSQL(dropQuery);
            }
        }
        onCreate(db);
    }


    String getTableName(Object Data){
        if(Data instanceof Word)
            return "Word";
        else if(Data instanceof Box)
            return "Box";
        else if(Data instanceof WordView)
            return "WordView";
        return null;
    }

    String getQuery(Object Data, boolean isModify){
        String query = null;
        if(Data instanceof Word){
            Word Word = (Word) Data;
            query = Word.getQueryColumns(isModify);
        }
        else if(Data instanceof Box){
            Box Box = (Box) Data;
            query = Box.getQueryColumns(isModify);

        }
        else if(Data instanceof WordView){
            WordView WordView = (WordView) Data;
            query = WordView.getQueryColumns(isModify);

        }
        return query;
    }
    public boolean Insert(Object object) {

        String tableName = getTableName(object);
        if(tableName == null){
            return false;
        }
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + tableName + " VALUES(null, "+getQuery(object, false) + ");");
        db.close();
        ((DataItem)object).setINCNO(getResourceLast키(tableName));
        return true;
    }
    public void InsertColor(String text) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO 테마 VALUES(null, '"+ text + "');");
        db.close();
    }

    public int getResourceLast키(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        int id = -1;

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT MAX(INCNO) FROM "+tableName+";", null);
        if (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }
    public int getCount(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        int id = -1;

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT Count(*) FROM "+tableName+";", null);
        if (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    public void update(Object object) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + getTableName(object) + " SET " + getQuery(object, true) + " WHERE INCNO = '" +  ((DataItem)object).getINCNO() + "';");
        db.close();

    }

    public void update테마(String color) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE 테마 SET color = '"+color+"';");
        db.close();

    }

    public void delete(Object object) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ getTableName(object) + " WHERE INCNO = '" +  ((DataItem)object).getINCNO() + "';");
        db.close();
    }/*
    public void delete일정Where복습주기리스트(복습주기리스트 복습주기리스트) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM 일정 WHERE 복습주기리스트_키 = '" +  복습주기리스트.get키() + "';");
        db.close();
    }
    public void delete작업Where일정(일정 일정) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM 작업 WHERE 일정_키 = '" +  일정.get키() + "';");
        db.close();
    }*/

    public ArrayList<Word> getResultWords(int box_INCNO) {
        ArrayList<Word> words = new ArrayList<Word>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Word WHERE box_INCNO = '" + box_INCNO + "' order by seqno ;", null);
        while (cursor.moveToNext()) {
            words.add(new Word(cursor.getInt(0), cursor.getString(1),  cursor.getInt(2), cursor.getInt(3)));
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return words;
    }

    public ArrayList<Box> getResultBoxs(int wordView_INCNO) {
        ArrayList<Box> words = new ArrayList<Box>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Box WHERE wordView_INCNO = '" + wordView_INCNO + "';", null);
        while (cursor.moveToNext()) {
            words.add(new Box(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return words;
    }
    public ArrayList<WordView> getResultWordViews() {
        ArrayList<WordView> wordViews = new ArrayList<WordView>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM WordView order by seqno ;", null);
        while (cursor.moveToNext()) {
            wordViews.add(new WordView(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return wordViews;
    }
/*
    public ArrayList<작업> getResult작업들(Date date) {
        Calendar start = Calendar.getInstance(), end = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.DAY_OF_MONTH, 1);
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        end.setTime(date);
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));


        ArrayList<작업> 작업들 = new ArrayList<작업>();

        SQLiteDatabase db = getReadableDatabase();//(SELECT Count(*) FROM 작업 WHERE 일정_키 = A.일정_키),  Count(A.키)
        Cursor cursor = db.rawQuery("SELECT A.*, Count(A.키), (SELECT Count(*) FROM 작업 WHERE 일정_키 = A.일정_키) FROM 작업 A LEFT JOIN 작업 B WHERE A.일정_키 = B.일정_키 AND A.작업날짜 >= B.작업날짜 AND A.작업날짜 >= '" + sdf_simple.format(start.getTime())+"' AND A.작업날짜 <= '"+ sdf_simple.format(end.getTime())  + "' GROUP BY A.키 ORDER BY A.작업날짜;", null);
//        Cursor cursor = db.rawQuery("SELECT * FROM 작업 WHERE 작업날짜 >= '" + sdf_simple.format(start.getTime())+"' AND 작업날짜 <= '"+ sdf_simple.format(end.getTime()) +"';", null);
        while (cursor.moveToNext()) {
            try {
                작업들.add(new 작업(cursor.getLong(0), cursor.getLong(1),  sdf_simple.parse(cursor.getString(2)), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 작업들;
    }

    public ArrayList<작업> getResult작업들Where일정키(long 일정_키) {
        Calendar start = Calendar.getInstance(), end = Calendar.getInstance();

        ArrayList<작업> 작업들 = new ArrayList<작업>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 작업 WHERE 일정_키 = '" + 일정_키 +"' order by 작업날짜;", null);
        while (cursor.moveToNext()) {
            try {
                작업들.add(new 작업(cursor.getLong(0), cursor.getLong(1),  sdf_simple.parse(cursor.getString(2)), cursor.getInt(3)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 작업들;
    }

    public ArrayList<작업> getResultSimple작업들Where일정(일정 일정) {
        Calendar start = Calendar.getInstance(), end = Calendar.getInstance();

        ArrayList<작업> 작업들 = new ArrayList<작업>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 작업 WHERE 일정_키 = '" + 일정.get키() +"' order by 작업날짜;", null);
        while (cursor.moveToNext()) {
            try {
                작업들.add(new 작업(cursor.getLong(0), cursor.getLong(1),  sdf_simple.parse(cursor.getString(2)), cursor.getInt(3)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 작업들;
    }

    public 작업 getResult작업(long 키) {
        작업 작업 = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 작업 WHERE 키 = '" + 키 +"';", null);
        if (cursor.moveToNext()) {
            try {
                작업 = new 작업(cursor.getLong(0), cursor.getLong(1),  sdf_simple.parse(cursor.getString(2)), cursor.getInt(3));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 작업;
    }

    public ArrayList<복습주기> getResult복습주기() {
        ArrayList<복습주기> 복습주기들 = new ArrayList<복습주기>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 복습주기;", null);
        while (cursor.moveToNext()) {
            복습주기들.add(new 복습주기(cursor.getLong(0), cursor.getString(1),  cursor.getString(2)));
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 복습주기들;
    }
    public ArrayList<복습주기리스트> getResult복습주기리스트() {
        ArrayList<복습주기리스트> 복습주기리스트들 = new ArrayList<복습주기리스트>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 복습주기리스트;", null);
        while (cursor.moveToNext()) {
            복습주기리스트들.add(new 복습주기리스트(cursor.getLong(0), cursor.getString(1)));
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 복습주기리스트들;
    }

    public 복습주기리스트 getResult복습주기리스트(long 키) {
        복습주기리스트 복습주기리스트 = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 복습주기리스트 WHERE 키 = '"+키+"';", null);
        if (cursor.moveToNext()) {
            복습주기리스트 = new 복습주기리스트(cursor.getLong(0), cursor.getString(1));
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 복습주기리스트;
    }

    public ArrayList<시간> getResult시간() {
        ArrayList<시간> 시간들 = new ArrayList<시간>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 시간;", null);
        while (cursor.moveToNext()) {
            시간들.add(new 시간(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2)));
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 시간들;
    }
    public ArrayList<일정> getResults일정() {
        ArrayList<일정> 일정들 = new ArrayList<일정>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 일정 ORDER BY 시작날짜;", null);
        while (cursor.moveToNext()) {
            try {
                일정들.add(new 일정(cursor.getLong(0),cursor.getLong(1), cursor.getString(2),cursor.getString(3), sdf_simple.parse(cursor.getString(4)), cursor.getString(5), cursor.getString(6)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 일정들;
    }

    public ArrayList<일정> getResultsSimple일정Where복습주기리스트(복습주기리스트 복습주기리스트) {
        ArrayList<일정> 일정들 = new ArrayList<일정>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 일정 WHERE 복습주기리스트_키 = '"+복습주기리스트.get키()+"';", null);
        while (cursor.moveToNext()) {
            일정들.add(new 일정(cursor.getLong(0),cursor.getLong(1)));
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 일정들;
    }

    public 일정 getResult일정(long 일정키) {
        일정 일정 = null;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 일정 WHERE 키 = '"+일정키+"';", null);
        if(cursor.moveToNext()) {
            try {
                일정 = new 일정(cursor.getLong(0),cursor.getLong(1), cursor.getString(2),cursor.getString(3), sdf_simple.parse(cursor.getString(4)), cursor.getString(5), cursor.getString(6));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();

        return 일정;
    }

    public String getColor() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 테마 ;", null);
        String color = null;
        if(cursor.moveToNext()) {
            color = cursor.getString(1);
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();
        return color;
    }*/

}
