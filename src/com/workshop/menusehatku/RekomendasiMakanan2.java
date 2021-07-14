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

public class RekomendasiMakanan2 extends Activity implements OnClickListener{
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
        SOLUSINYA = i.getStringExtra("PAKET");
        MENUNYA = i.getStringExtra("MENUNYA");
        

		this.listMenu = (ListView) this.findViewById(R.id.list_group_makanan);
		        
		setContentView( R.layout.rekomendasi_makanan_obes);
		pesan = (TextView) findViewById(R.id.pesan);
		sarapan = (TextView) findViewById(R.id.menusarapan);
		beratsarapan = (TextView) findViewById(R.id.beratsarapan);
		cemilpagi = (TextView) findViewById(R.id.menucemilanpagi);
		beratcemilpagi = (TextView) findViewById(R.id.beratcemilanpagi);
		makansiang = (TextView) findViewById(R.id.menusiang);
		beratsiang = (TextView) findViewById(R.id.beratsiang);
		cemilsiang = (TextView) findViewById(R.id.menucemilansiang);
		beratcemilsiang = (TextView) findViewById(R.id.beratcamilansiang);
		makanmalam = (TextView) findViewById(R.id.menumalam);
		beratmalam = (TextView) findViewById(R.id.beratmalam);
		cemilmalam = (TextView) findViewById(R.id.menucemilanmalam);
		beratcemilmalam = (TextView) findViewById(R.id.beratcemilanmalam);
		
		View kembaliButton = findViewById(R.id.MenuUtama);
        kembaliButton.setOnClickListener(this);

		View rs = findViewById(R.id.btnRes);
		rs.setOnClickListener(this);
        
        
		this.listMenu = (ListView) this.findViewById(R.id.list_group_makanan);
		SharedPreferences sharedPreferences = getSharedPreferences(
				"pindahActivity", 0);
		nama = sharedPreferences.getString("nama", "");
		tinggi = sharedPreferences.getString("tinggi", "");
		beratBadan = sharedPreferences.getString("berat", "");
		usia = sharedPreferences.getString("usia", "");		
		EnergiKkal = Float.parseFloat(sharedPreferences.getString(
				"EnergiKkal", ""));
		LIST_GEJALA= sharedPreferences.getString("list_gejala","");
		
		Toast.makeText(this, EnergiKkal+"", Toast.LENGTH_SHORT).show();
		
		
		dbHelper = new DBAdapter(this);
		delimiter = ",";
		
		solusi = "";
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
		
		pesan.setText("Ibu " + nama + "\n Kebutuhan kalori anda " + EnergiKkal + " Kkal " );
		
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
							"select _id, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ sarapMalam
									+ ") as distance from view_menu_solusi WHERE waktu_makan='Pagi' AND paket_solusi='"+solusi+"' order by distance limit 1",
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
							"select _id, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ CemilanPagi
									+ ") as distance from view_menu_solusi WHERE waktu_makan='Camilan pagi' AND paket_solusi='"+solusi+"' order by distance limit 1",
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
			beratsiang.setText(sb2);
			
		  // method untuk makan siang
			Cursor selectedMenu3 = db
					.rawQuery(
							"select _id, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ MakanSiang
									+ ") as distance from view_menu_solusi WHERE waktu_makan='Siang' AND paket_solusi='"+solusi+"' order by distance limit 1",
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
							"select _id, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ CemilanSiang
									+ ") as distance from view_menu_solusi WHERE waktu_makan='Camilan Siang' AND paket_solusi='"+solusi+"' order by distance limit 1",
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
			
			// method untuk makan malam
			Cursor selectedMenu5 = db
					.rawQuery(
							"select _id, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ MakanMalam
									+ ") as distance from view_menu_solusi WHERE waktu_makan='Malam' AND paket_solusi='"+solusi+"' order by distance limit 1",
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
							"select _id, waktu_makan,nama_makanan, berat, total_kalori, abs(total_kalori - "
									+ CemilanMalam
									+ ") as distance from view_menu_solusi WHERE waktu_makan='Camilan Malam'  AND paket_solusi='"+solusi+"' order by distance limit 1",
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
		
		case R.id.MenuUtama:
			Intent a = new Intent (this, MainActivity.class);
			startActivity(a);
			finish();
			break;
		case R.id.btnRes:
			Intent a1 = new Intent (this, PerhitunganCBR.class);
			a1.putExtra("list_gejala", LIST_GEJALA);
      		startActivity(a1);
			break;
		
			}
	}
}