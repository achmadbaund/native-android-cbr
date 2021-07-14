package com.workshop.menusehatku;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.workshop.menusehatku.database.DBAdapter;
import com.workshop.menusehatku.database.GejalaAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Gejala extends Activity implements OnClickListener {
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

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputgejala);
		inputGejala = (EditText) findViewById(R.id.inputGejala);
		this.aktifitasList = (ListView) findViewById(R.id.listGejala);
		checkbox = getSharedPreferences("Check", 0);
		editor2 = checkbox.edit();
		editor2.clear();
		editor2.commit();
		shared = getSharedPreferences("pindahActivitylg", 0);
		editor3 = shared.edit();
		editor3.clear();
		
	    Button next = (Button) findViewById(R.id.menuUtama1);
	    next.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	            Intent myIntent = new Intent(view.getContext(), MainActivity.class);
	            startActivity(myIntent);
	        }
	    });	
		
		aktifitasList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick( AdapterView<?> parent, View item, int position, long id) {
				chk = (CheckBox) item.findViewById(R.id.checkbox);
				gejala = (NamaGejala) parent.getItemAtPosition(position);

				if (gejala.isSelected()){
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
		String query = "select * from Tabel_gejala where Nama_gejala LIKE '%"+inputGejala.getText().toString()+"%' ";
		ArrayList<ArrayList<String>> stringList = db.selectRecordsFromDBList(query, null);
		db.close();

		ArrayList<NamaGejala> userList = new ArrayList<NamaGejala>();
		for (int i = 0; i< stringList.size(); i++){
			ArrayList<String> list = stringList.get(i);
			NamaGejala user = new NamaGejala();
			try {
				user.setId(Integer.parseInt(list.get(0)));
				user.setGejala(list.get(1));
			} catch (Exception e) {
				Log.i("***" + Gejala.class.toString(), e.getMessage());
			}
			userList.add(user);
		}	
		return userList;
	}

	public void hitungKalori (View view){
		mListUsers = getActivities();
			nilai = "";
			for (int i = 0; i < mListUsers.size(); i++){
				gejala.setSelected(load(mListUsers.get(i).getId()));
				if(gejala.isSelected() == true){
					if(nilai==""){
						nilai = ""+mListUsers.get(i).getId();
					}else{
						nilai = nilai+","+mListUsers.get(i).getId();
					}
				}
			}
			shared = getSharedPreferences("pindahActivity", 0);
			editor = shared.edit();
			finish();
			editor.putString("list_gejala",nilai);
			editor.commit();
			i = new Intent(this, tesListView2.class);
			startActivity(i);
			finish();

	}

	public void hitungKalori1(View view){
		try{
			mListUsers = getActivities();
			count = 0;
			//nilai = 0;
			for (int i = 0; i < mListUsers.size(); i++){
				gejala.setSelected(load(mListUsers.get(i).getId()));
				SharedPreferences shared = getSharedPreferences("pindahActivity", 0);
				if(gejala.isSelected() == true){
					count++;
					if (count > 0){
						if (shared.getInt("status", 3) == 1){
							Log.v("nilaiGejala", "nilaiGejala = "+ mListUsers.get(i).getGejala());
							nilai = nilai + mListUsers.get(i).getGejala();
						}else if (shared.getInt("status", 3) == 2){
							Log.v("nilaiGejala", "nilaiGejala = " + mListUsers.get(i).getGejala());
							nilai = nilai + mListUsers.get(i).getGejala();
						}
					}
				}
			}
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			//double nilaiRata = nilai/count;
			Log.v("nilaiAktifitas", "nilai total= " + twoDForm.format(nilai));
			Log.v("nilaiAktifitas", "selected = " + count);
			SharedPreferences editor = getSharedPreferences("pindahActivity", 0);
			String kall = editor.getString("kalori","");
			Log.d("share input", "perkiraan kalori "+kall);
			double bee = Double.parseDouble(kall);
		}catch(Exception e){
			Log.v("***" + Gejala.class.toString(), e.getMessage());
		}

		// Pindah ke Activity RekomendasiMakanan
		 Intent intent = new Intent(view.getContext(), tesListView.class);
		 startActivityForResult(intent,0);
		 finish();
	}

	public void but_SearchClick(View view){
		try{
			mListUsers = getActivities();
			adapter = new GejalaAdapter(Gejala.this, mListUsers);
			aktifitasList.setAdapter(adapter);
			for (int i = 0; i< mListUsers.size(); i++){
				mListUsers.get(i).setSelected(load(mListUsers.get(i).getId()));				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void save (final boolean isChecked, final int position){
		editor2.putBoolean("chk" + position, isChecked);
		editor2.commit();
	}

	private boolean load(final int position){
		return checkbox.getBoolean("chk" +  position, false);
	}

	@Override
	public void onClick(View arg0){

	}

	@Override
	public boolean onKeyDown(int KeyCode, KeyEvent event){
		if(KeyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(KeyCode, event);
	}
}