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

public class RekomendasiMakananSiangUmum extends Activity implements OnClickListener{
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
		        
		setContentView( R.layout.rekomendasi_makan_siang);
		pesan = (TextView) findViewById(R.id.pesan);
		makansiang = (TextView) findViewById(R.id.menusiang);
		beratsiang = (TextView) findViewById(R.id.beratsiang);
		cemilsiang = (TextView) findViewById(R.id.menucemilansiang);
		beratcemilsiang = (TextView) findViewById(R.id.beratcamilansiang);
		
		View Buttonpagi = findViewById(R.id.btnpagi2);
        Buttonpagi.setOnClickListener(this);
		
		View buttonmalam = findViewById(R.id.btnmalam2);
        buttonmalam.setOnClickListener(this);

		View home = findViewById(R.id.home2);
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

		  // method untuk makan siang
			Cursor selectedMenu3 = db
					.rawQuery(
							"select IdSolusi, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ MakanSiang
									+ ") as distance from View_menu_solusi WHERE waktu_makan='Siang' AND solusi_menu='"+solusi+"' order by distance limit 1",
							null);
				
			selectedMenu3.moveToFirst();
			makansg = selectedMenu3.getString(selectedMenu3
					.getColumnIndex("nama_makanan"));
			String[] temp3;
			temp3 = makansg.split(delimiter);
			StringBuffer sb3 = new StringBuffer();
			for (int i = 0; i < temp3.length; i++) {
				sb3.append(temp3[i] + "\n");
			}
			makansiang.setText(sb3);
			Log.d("menu menu", "opo iki?? " + sarapan);

			beratmakansg = selectedMenu3.getString(selectedMenu3
					.getColumnIndex("berat"));
			String[] tempbs3;
			tempbs3 = beratmakansg.split(delimiter);
			StringBuffer sbbs3 = new StringBuffer();
			for (int i = 0; i < tempbs3.length; i++) {
				sbbs3.append(tempbs3[i] + "\n");
			}
			beratsiang.setText(sbbs3);

			// method untuk cemilan siang
			Cursor selectedMenu4 = db
					.rawQuery(
							"select IdSolusi, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ CemilanSiang
									+ ") as distance from View_menu_solusi WHERE waktu_makan='Camilan Siang' AND solusi_menu='"+solusi+"' order by distance limit 1",
							null);
			selectedMenu4.moveToFirst();
			cemilsg = selectedMenu4.getString(selectedMenu4
					.getColumnIndex("nama_makanan"));
			String[] temp4;
			temp4 = cemilsg.split(delimiter);
			StringBuffer sb4 = new StringBuffer();
			for (int i = 0; i < temp4.length; i++) {
				sb4.append(temp4[i] + "\n");
			}
			cemilsiang.setText(sb4);
			Log.d("menu menu", "opo iki?? " + sarapan);

			brtcmilsg = selectedMenu4.getString(selectedMenu4
					.getColumnIndex("berat"));
			String[] tempbs4;
			tempbs4 = brtcmilsg.split(delimiter);
			StringBuffer sbbs4 = new StringBuffer();
			for (int i = 0; i < tempbs4.length; i++) {
				sbbs4.append(tempbs4[i] + "\n");
			}
			beratcemilsiang.setText(sbbs4);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnpagi2:
			Intent a = new Intent (this, RekomendasiMakananPagiUmum.class);
			a.putExtra("PAKETSOLUSI", solusi);
			startActivity(a);
			finish();
			break;
		case R.id.btnmalam2:
			Intent a1 = new Intent (this, RekomendasiMakananMalamUmum.class);
			a1.putExtra("PAKETSOLUSI", solusi);
      		startActivity(a1);
			break;
		case R.id.home2:
			Intent a2 = new Intent (this, MainActivity.class);			
      		startActivity(a2);
			break;			
			}
	}
}