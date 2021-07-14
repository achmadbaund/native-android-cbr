package com.workshop.menusehatku;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.workshop.menusehatku.database.DBAdapter;
import com.workshop.menusehatku.database.GejalaAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

	public class Edit_kasus extends Activity implements OnClickListener {
		public DBAdapter dbhelper;
		private EditText inputGejala;
		private ListAdapter adapter;
		private ListView aktifitasList;
		private ArrayList<NamaGejala> mListUsers;
		public SharedPreferences settings, checkbox, shared;
		public SharedPreferences.Editor editor2, editor3,editor;
		public CheckBox chk;
		public NamaGejala gejala;
		public String nilai;
		public int count;
		public Intent i;
		EditText t; 
		
		@Override
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.edit_kasus);
			inputGejala = (EditText) findViewById(R.id.inputGejala);
			this.aktifitasList = (ListView) findViewById(R.id.listGejala);
			 View button1Button = findViewById(R.id.button1);
		        button1Button.setOnClickListener(this);
			checkbox = getSharedPreferences("Check", 0);
			editor2 = checkbox.edit();
			editor2.clear();
			editor2.commit();
			shared = getSharedPreferences("pindahActivitylg", 0);
			editor3 = shared.edit();
			editor3.clear();
			
			t = (EditText)findViewById(R.id.inputKode);
			
			aktifitasList.setOnItemClickListener(new OnItemClickListener() {
		           
			@Override
			public void onItemClick( AdapterView<?> parent, View item, int position, long id) {
					chk = (CheckBox) item.findViewById(R.id.checkbox);
					gejala = (NamaGejala) parent.getItemAtPosition(position);
				
					if (gejala.isSelected()) {
						gejala.setSelected(false);
						chk.setChecked(false);
						save(gejala.isSelected(), gejala.getId());
					} else {
						gejala.setSelected(true);
						chk.setChecked(true);
						save(gejala.isSelected(), gejala.getId());
					}
					Log.v("checkbox", "chk = " + gejala.getId());
					save(gejala.isSelected(), gejala.getId());
					Log.v("MenusehatKu", "save state " + gejala.getId() + " " + gejala.getGejala() + " = " + gejala.isSelected());	
					Log.v("MenusehatKu", "nilai gejala " + gejala.getGejala());	
				}
			});
	    }
		
		public ArrayList<NamaGejala> getActivities(){
			DBAdapter db = DBAdapter.getDBAdapterInstance(this);
			db.openDataBase();
		 	String query="select * from Tabel_gejala where gejala LIKE '%"+inputGejala.getText().toString()+"%' ";
		 	ArrayList<ArrayList<String>> stringList = db.selectRecordsFromDBList(query, null);
			db.close();
			
			
		 	ArrayList<NamaGejala> usersList = new ArrayList<NamaGejala>();
		 	for (int i = 0; i < stringList.size(); i++) {
				ArrayList<String> list = stringList.get(i);
				NamaGejala user = new NamaGejala();
				try {
					user.setId(Integer.parseInt(list.get(0)));
					user.setGejala(list.get(1));
				} catch (Exception e) {
					Log.i("***" + Gejala.class.toString(), e.getMessage());
				}
				usersList.add(user);
			}
			return usersList;
		}
		
		public void SimpanKasus(View view){
				//DBAdapter dbhelper = DBAdapter.getDBAdapterInstance(this);
				//dbhelper.openDataBase();
				//SQLiteDatabase db1 = dbhelper.getReadableDatabase();
				//Cursor c = db1.rawQuery("select * from Tabel_kasus1",null);
				
				DBAdapter db = DBAdapter.getDBAdapterInstance(Edit_kasus.this);
				db.openDataBase();
			 	String query="select count(*) as n from Tabel_kasus1";
			 	Cursor c = db.selectRecordsFromDB(query,null);
				
			    mListUsers = getActivities();
		        EditText kod = (EditText)findViewById(R.id.inputKode);
		        EditText sl = (EditText)findViewById(R.id.inputSolusi);
		        ContentValues initialValues = new ContentValues();
		        
		        if(c.moveToFirst())
		        {
		        	//initialValues.put("Kode","P"+(c.getInt(c.getColumnIndex("n")) +1));
		        }
		        initialValues.put("Paket_solusi",sl.getText().toString().trim());
		        //initialValues.put("Kode",kod.getText().toString().trim());
		        
		        for (int i = 0; i < mListUsers.size(); i++){
		        	gejala.setSelected(load(mListUsers.get(i).getId()));
		 			if(gejala.isSelected() == true){
		 				initialValues.put("G"+mListUsers.get(i).getId(),"1");
		 			}else{
		 				initialValues.put("G"+mListUsers.get(i).getId(),"0");
			 		}
		        }
		        
		        DBAdapter db1 = DBAdapter.getDBAdapterInstance(Edit_kasus.this);
				db1.openDataBase();
				db1.updateRecordInDB("Tabel_kasus1", initialValues, "Kode" + "='" + kod.getText().toString().trim() + "'", null);
				db1.updateRecordInDB("Tabel_kasus1", initialValues, "Paket_solusi" + "='" + kod.getText().toString().trim() + "'", null);
		        c.close(); 
		        db.close();
		        db1.close();
		        
			 	//dbhelper.close();
				
	 			shared = getSharedPreferences("pindahActivity",0);
				editor = shared.edit();
				//finish();
				editor.putString("list_gejala",nilai);
				editor.commit();
				//i = new Intent(KasusBaru.this, Menu_utama.class);
				//startActivity(i);
				//finish();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(Edit_kasus.this);
				builder.setMessage("Apakah Anda Ingin menyimpan? ").setCancelable(false)
				.setPositiveButton("Ya",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						i = new Intent(Edit_kasus.this, Edit_kasus.class);
						startActivity(i);
						finish();
					} 
				})
				.setNegativeButton("Tidak, kembali ke menu utama",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id)
					{
						i = new Intent(Edit_kasus.this, MainActivity.class);
						startActivity(i);
						finish();
					}
				}
				).show();
				
		}
		
		public void but_SearchClick(View view){
	    	try{
	            mListUsers = getActivities();               
			 	adapter = new GejalaAdapter(Edit_kasus.this, mListUsers);
	    		aktifitasList.setAdapter(adapter);
	    		for (int i = 0; i < mListUsers.size(); i++){
	    			mListUsers.get(i).setSelected(load(mListUsers.get(i).getId()));
	    		}
	    	}catch(Exception e){
	    		e.printStackTrace();
	        }
		}

		private void save(final boolean isChecked, final int position) {
		    editor2.putBoolean("chk" + position, isChecked);
		    editor2.commit();
		}

		private boolean load(final int position) { 
		    return checkbox.getBoolean("chk" + position, false);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
		case R.id.button1:
			Intent a = new Intent (this, MainActivity.class);
			startActivity(a);
			finish();
			
			}
		}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		     if (keyCode == KeyEvent.KEYCODE_BACK) {
		     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
		     return true;
		     }
		     return super.onKeyDown(keyCode, event);    
		}
	}
