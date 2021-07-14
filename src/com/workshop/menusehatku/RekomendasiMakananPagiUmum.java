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

public class RekomendasiMakananPagiUmum extends Activity implements OnClickListener{
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
		        
		setContentView( R.layout.rekomendasi_makan_pagi);
		pesan = (TextView) findViewById(R.id.pesan);
		sarapan = (TextView) findViewById(R.id.menusarapan);
		beratsarapan = (TextView) findViewById(R.id.beratsarapan);
		cemilpagi = (TextView) findViewById(R.id.menucemilanpagi);
		beratcemilpagi = (TextView) findViewById(R.id.beratcemilanpagi);
		
		View Buttonsiang = findViewById(R.id.btnsiang1);
        Buttonsiang.setOnClickListener(this);
		
		View buttonmalam = findViewById(R.id.btnmalam1);
        buttonmalam.setOnClickListener(this);

		View home = findViewById(R.id.home1);
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
				
		if(!SOLUSINYA.equals("")){
			solusi = SOLUSINYA;
		}else {
			if(EnergiKkal <= 1250) {
				solusi = "A";
			}else if( (EnergiKkal >= 1250)  && (EnergiKkal <= 1450) ) {
				solusi = "B";
			}else if( (EnergiKkal >= 1451)  && (EnergiKkal <=1650) ) {
				solusi = "C";
			}else if( (EnergiKkal >= 1651)  && (EnergiKkal <= 1850) ) {
					solusi = "D";
			}else if( (EnergiKkal >= 1851)  && (EnergiKkal <= 2050) ) {
				solusi = "E";
			}else if(EnergiKkal >= 2051){
				solusi = "F";
			}			
		}

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

			// method untuk Sarapan
			Cursor selectedMenu = db
					.rawQuery(
							"select IdSolusi, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ sarapMalam
									+ ") as distance from View_menu_solusi WHERE waktu_makan='Pagi' AND solusi_menu='"+solusi+"' order by distance limit 1",
							null);
			
			selectedMenu.moveToFirst();
			saur = selectedMenu.getString(selectedMenu
					.getColumnIndex("nama_makanan"));
			String[] temp;
			temp = saur.split(delimiter);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < temp.length; i++) {
				sb.append(temp[i] + "\n");
			}
			sarapan.setText(sb);
			Log.d("menu menu", "opo iki?? " + sarapan);

			beratsaur = selectedMenu.getString(selectedMenu
					.getColumnIndex("berat"));
			String[] tempbs;
			tempbs = beratsaur.split(delimiter);
			StringBuffer sbbs = new StringBuffer();
			for (int i = 0; i < tempbs.length; i++) {
				sbbs.append(tempbs[i] + "\n");
			}
			beratsarapan.setText(sbbs);
			
			// method untuk cemilan pagi 
			Cursor selectedMenu1 = db
					.rawQuery(
							"select IdSolusi, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ CemilanPagi
									+ ") as distance from View_menu_solusi WHERE waktu_makan='Camilan pagi' AND solusi_menu='"+solusi+"' order by distance limit 1",
							null);
			selectedMenu1.moveToFirst();
			cemilpg = selectedMenu1.getString(selectedMenu1
					.getColumnIndex("nama_makanan"));
			String[] temp1;
			temp1 = cemilpg.split(delimiter);
			StringBuffer sb1 = new StringBuffer();
			for (int i = 0; i < temp1.length; i++) {
				sb1.append(temp1[i] + "\n");
			}
			cemilpagi.setText(sb1);
			Log.d("menu menu", "opo iki?? " + CemilanPagi);

			brtcmilpg = selectedMenu1.getString(selectedMenu1
					.getColumnIndex("berat"));
			String[] temp2;
			temp2 = brtcmilpg.split(delimiter);
			StringBuffer sb2 = new StringBuffer();
			for (int i = 0; i < temp2.length; i++) {
				sb2.append(temp2[i] + "\n");
			}
			beratcemilpagi.setText(sb2);				

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnsiang1:
			Intent a = new Intent (this, RekomendasiMakananSiangUmum.class);
			a.putExtra("PAKETSOLUSI", solusi);
			startActivity(a);
			finish();
			break;
		case R.id.btnmalam1:
			Intent a1 = new Intent (this, RekomendasiMakananMalamUmum.class);
			a1.putExtra("PAKETSOLUSI", solusi);
      		startActivity(a1);
			break;
		case R.id.home1:
			Intent a2 = new Intent (this, MainActivity.class);			
      		startActivity(a2);
			break;			
		
			}
	}
}