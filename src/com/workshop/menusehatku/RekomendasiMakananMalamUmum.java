package com.workshop.menusehatku;

import java.util.ArrayList;

import com.workshop.menusehatku.database.DBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RekomendasiMakananMalamUmum extends Activity implements OnClickListener{
	private TextView pesan, sarapan, beratsarapan, cemilpagi, beratcemilpagi,
			makansiang, beratsiang, cemilsiang, beratcemilsiang, makanmalam,
			beratmalam, cemilmalam, beratcemilmalam, utama;
	private ListView listMenu;
	private String saur, beratsaur, _id, jarak, total_kalori, nama, makansg,
			beratmakansg, makanml, beratmakanml, tinggi, beratBadan, usia,
			nama_paket, cemilpg, brtcmilpg, cemilsg, brtcmilsg,
			cemilml, brtcmilml, waktu_makan, nama_makanan, delimiter, LIST_GEJALA;
	Float EnergiKkal;
	private float MakanPagi, MakanSiang, MakanMalam,
			CemilanPagi, CemilanSiang, CemilanMalam;
	private ArrayList<NameMakanan> mListSarapan, mListMakanSiang,
			mListmakanMalam;
	private double berat, kalori;
	DBAdapter dbHelper;
	private Cursor cursor, cursor2;
	private ListAdapter adapter;
	private TextView hasil_cbr;
	private String solusi = null;
	private Intent i;
	private String SOLUSINYA,MENUNYA;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		i = getIntent();
        SOLUSINYA = i.getStringExtra("PAKETSOLUSI");        
		        
		setContentView( R.layout.rekomendasi_makan_malam);
		pesan = (TextView) findViewById(R.id.pesan);
		makanmalam = (TextView) findViewById(R.id.menumalam);
		beratmalam = (TextView) findViewById(R.id.beratmalam);
		cemilmalam = (TextView) findViewById(R.id.menucemilanmalam);
		beratcemilmalam = (TextView) findViewById(R.id.beratcemilanmalam);
		
		View Buttonpagi = findViewById(R.id.btnpagi1);
        Buttonpagi.setOnClickListener(this);
		
		View Buttonsiang = findViewById(R.id.btnsiang2);
        Buttonsiang.setOnClickListener(this);

		View home = findViewById(R.id.home3);
        home.setOnClickListener(this); 

		SharedPreferences sharedPreferences = getSharedPreferences(
				"pindahActivity", 0);
		nama = sharedPreferences.getString("nama", "");
		tinggi = sharedPreferences.getString("tinggi", "");
		beratBadan = sharedPreferences.getString("berat", "");
		usia = sharedPreferences.getString("usia", "");		
		EnergiKkal = Float.parseFloat(sharedPreferences.getString(
				"EnergiKkal", ""));
		LIST_GEJALA= sharedPreferences.getString("list_gejala","");						
		
		dbHelper = new DBAdapter(this);
		delimiter = ",";		
				
		solusi = SOLUSINYA;
		
		Toast.makeText(this, EnergiKkal+"&"+solusi, Toast.LENGTH_SHORT).show();
		
		//Toast.makeText(this, LIST_GEJALA, Toast.LENGTH_SHORT).show();
		
		makanan();
		
		/*
		AlertDialog alert1 = new AlertDialog.Builder(this).create();
		alert1.setMessage(tinggi+" "+beratBadan+" "+usiaIbu+" "+usiaKandungan);
		alert1.show();
		 */
	}
	
	public void makanan(){
		try {
			float sarapMalam = (EnergiKkal * 20) / 100;
			float MakanSiang = (EnergiKkal * 30) / 100;
			float cemilan = (EnergiKkal * 10) / 100;
			Log.d("hahahaha", "hohohoho" + EnergiKkal + "|" + sarapMalam);
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			// method untuk makan malam
			Cursor selectedMenu5 = db
					.rawQuery(
							"select IdSolusi, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ MakanMalam
									+ ") as distance from View_menu_solusi WHERE waktu_makan='Malam' AND solusi_menu='"+solusi+"' order by distance limit 1",
							null);
			selectedMenu5.moveToFirst();
			makanml = selectedMenu5.getString(selectedMenu5
					.getColumnIndex("nama_makanan"));
			String[] temp5;
			temp5 = makanml.split(delimiter);
			StringBuffer sb5 = new StringBuffer();
			for (int i = 0; i < temp5.length; i++) {
				sb5.append(temp5[i] + "\n");
			}
			makanmalam.setText(sb5);
			Log.d("menu menu", "opo iki?? " + sarapan);

			beratmakanml = selectedMenu5.getString(selectedMenu5
					.getColumnIndex("berat"));
			String[] tempbs5;
			tempbs5 = beratmakanml.split(delimiter);
			StringBuffer sbbs5 = new StringBuffer();
			for (int i = 0; i < tempbs5.length; i++) {
				sbbs5.append(tempbs5[i] + "\n");
			}
			beratmalam.setText(sbbs5);
			
			// method untuk cemilan malam
			Cursor selectedMenu6 = db
					.rawQuery(
							"select IdSolusi, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ CemilanMalam
									+ ") as distance from View_menu_solusi WHERE waktu_makan='Camilan Malam'  AND solusi_menu='"+solusi+"' order by distance limit 1",
							null);
			
			selectedMenu6.moveToFirst();
			cemilml = selectedMenu6.getString(selectedMenu6
					.getColumnIndex("nama_makanan"));
			String[] temp6;
			temp6 = cemilml.split(delimiter);
			StringBuffer sb6 = new StringBuffer();
			for (int i = 0; i < temp6.length; i++) {
				sb6.append(temp6[i] + "\n");
			}
			cemilmalam.setText(sb6);
			Log.d("menu menu", "opo iki?? " + sarapan);

			brtcmilml = selectedMenu6.getString(selectedMenu6
					.getColumnIndex("berat"));
			String[] tempbs6;
			tempbs6 = brtcmilml.split(delimiter);
			StringBuffer sbbs6 = new StringBuffer();
			for (int i = 0; i < tempbs6.length; i++) {
				sbbs6.append(tempbs6[i] + "\n");
			}
			beratcemilmalam.setText(sbbs6);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnpagi1:
			Intent a = new Intent (this, RekomendasiMakananPagiUmum.class);
			a.putExtra("PAKETSOLUSI", solusi);			
			startActivity(a);
			finish();
			break;
		case R.id.btnsiang2:
			Intent a1 = new Intent (this, RekomendasiMakananSiangUmum.class);
			a1.putExtra("PAKETSOLUSI", solusi);			
      		startActivity(a1);
			break;
		case R.id.home3:
			Intent a2 = new Intent (this, MainActivity.class);			
      		startActivity(a2);
			break;			
			}
	}
}