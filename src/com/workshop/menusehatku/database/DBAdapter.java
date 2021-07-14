package com.workshop.menusehatku.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper {

	private static String DB_PATH = "";
	private static String DB_NAME = "menu.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private static DBAdapter mDBConnection;
	
	 public DBAdapter(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		DB_PATH = "/data/data/" + context.getApplicationContext().getPackageName() + "/databases/";
	}

	public static synchronized DBAdapter getDBAdapterInstance(Context context) {
		if (mDBConnection == null) {
			mDBConnection = new DBAdapter(context);
		}
		return mDBConnection;
	}


	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			copyDataBase();
		} else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}
	
	private boolean checkDataBase() {
		    File dbFile = new File(DB_PATH + DB_NAME);
		    return dbFile.exists();
	}


	public void copyDataBase() throws IOException {
		    // Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		    // Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		    // Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		    // transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		    // Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
 
	/**
	 * Open the database
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);		
	}

	/**
	 * Close the database if exist
	 */
	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	/**
	 * Call on creating data base for example for creating tables at run time
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	/**
	 * can used for drop tables then call onCreate(db) function to create tables again - upgrade
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}


	public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
			String whereClase, String whereArgs[], String groupBy,
			String having, String orderBy) {
		return myDataBase.query(tableName, tableColumns, whereClase, whereArgs,
				groupBy, having, orderBy);
	}
	

	public ArrayList<ArrayList<String>> selectRecordsFromDBList(String tableName, String[] tableColumns,
			String whereClase, String whereArgs[], String groupBy,
			String having, String orderBy) {		
		
		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
	      ArrayList<String> list = new ArrayList<String>();
	      Cursor cursor = myDataBase.query(tableName, tableColumns, whereClase, whereArgs,
					groupBy, having, orderBy);        
	      if (cursor.moveToFirst()) {
	         do {
	        	 list = new ArrayList<String>();
	        	 for(int i=0; i<cursor.getColumnCount(); i++){	        		 
	        		 list.add( cursor.getString(i) );
	        	 }	 
	        	 retList.add(list);
	         } while (cursor.moveToNext());
	      }
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	      return retList;

	}	


	public long insertRecordsInDB(String tableName, String nullColumnHack,
			ContentValues initialValues) {
		return myDataBase.insert(tableName, nullColumnHack, initialValues);
	}

	/**
	 * This function used to update the Record in DB.
	 * @param tableName
	 * @param initialValues
	 * @param whereClause
	 * @param whereArgs
	 * @return true / false on updating one or more records
	 */
	public boolean updateRecordInDB(String tableName,
			ContentValues initialValues, String whereClause, String whereArgs[]) {
		return myDataBase.update(tableName, initialValues, whereClause,
				whereArgs) > 0;				
	}
	
	/**
	 * This function used to update the Record in DB.
	 * @param tableName
	 * @param initialValues
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are updated
	 */
	public int updateRecordsInDB(String tableName,
			ContentValues initialValues, String whereClause, String whereArgs[]) {
		return myDataBase.update(tableName, initialValues, whereClause, whereArgs);		
	}

	/**
	 * This function used to delete the Record in DB.
	 * @param tableName
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are deleted.
	 */
	public int deleteRecordInDB(String tableName, String whereClause,
			String[] whereArgs) {
		return myDataBase.delete(tableName, whereClause, whereArgs);
	}

	// --------------------- Select Raw Query Functions ---------------------
	

	public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
		return myDataBase.rawQuery(query, selectionArgs);		
	}
	

	public ArrayList<ArrayList<String>> selectRecordsFromDBList(String query, String[] selectionArgs) {	      
	      ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
	      ArrayList<String> list = new ArrayList<String>();
	      Cursor cursor = myDataBase.rawQuery(query, selectionArgs);	        
	      if (cursor.moveToFirst()) {
	         do {
	        	 list = new ArrayList<String>();
	        	 for(int i=0; i<cursor.getColumnCount(); i++){	        		 
	        		 list.add( cursor.getString(i) );
	        	 }	 
	        	 retList.add(list);
	         } while (cursor.moveToNext());
	      }
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	      return retList;
	   }

	public Cursor getMaxColumnData() {
		// TODO Auto-generated method stub
		return null;
	}

} 