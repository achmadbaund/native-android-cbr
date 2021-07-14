package com.workshop.menusehatku;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.workshop.menusehatku.database.DBAdapter;
import android.os.Bundle;
import android.R.string;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class tesListView2 extends Activity implements OnClickListener {

	
	private TextView pesan, sarapan, beratsarapan, cemilpagi, beratcemilpagi,
	makansiang, beratsiang, cemilsiang, beratcemilsiang, makanmalam,
	beratmalam, cemilmalam, beratcemilmalam, utama;
	private ListView listMenu;
	private String saur, beratsaur, _id, jarak, total_kalori, nama, makansg,
	beratmakansg, makanml, beratmakanml, tinggi, beratBadan, usia,
	nama_paket, cemilpg, brtcmilpg, cemilsg, brtcmilsg,
	cemilml, brtcmilml, waktu_makan, nama_makanan, delimiter, LIST_GEJALA;
	private float EnergiKkal, MakanPagi, MakanSiang, MakanMalam,
	CemilanPagi, CemilanSiang, CemilanMalam;
	private ArrayList<NameMakanan> mListSarapan, mListMakanSiang,
	mListmakanMalam;
	private double berat, kalori;
	private Cursor cursor, cursor2;
	private ListAdapter adapter;
	private TextView hasil_cbr;
	private String solusi = null;
	private String nilaiT = null;
	private float treshold = (float) 0.6;
	private float T_max = 0;	
	private int jml_kalori;
	public int getMaxColumnData() {
		DBAdapter db = DBAdapter.getDBAdapterInstance(tesListView2.this);
		db.openDataBase();
        SQLiteDatabase mDb = db.getReadableDatabase();
	    final SQLiteStatement stmt = mDb
	            .compileStatement("SELECT MAX(T) FROM view_gejala_kasus");

	    return (int) stmt.simpleQueryForLong();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perhitungan_cbr);
		SharedPreferences sharedPreferences = getSharedPreferences(
			"pindahActivity", 0);
		nama = sharedPreferences.getString("nama", "");
		tinggi = sharedPreferences.getString("tinggi", "");
		beratBadan = sharedPreferences.getString("berat", "");
		usia = sharedPreferences.getString("usia", "");		
		EnergiKkal = Float.parseFloat(sharedPreferences.getString(
			"EnergiKkal", ""));
		LIST_GEJALA= sharedPreferences.getString("list_gejala","");
		
		
		hasil_cbr = (TextView)findViewById(R.id.hasil_cbr);

		View prosesButton = findViewById(R.id.rekomendasi);
		prosesButton.setOnClickListener(this);

		View proses1Button = findViewById(R.id.menu);
		proses1Button.setOnClickListener(this);	
		
		if(EnergiKkal <= 1250) {
			jml_kalori = 1;
		}else if( (EnergiKkal >= 1251)  && (EnergiKkal <= 1450) ) {
			jml_kalori = 2;
		}else if( (EnergiKkal >= 1451)  && (EnergiKkal <=1650) ) {
			jml_kalori = 3;
		}else if( (EnergiKkal >=1651)  && (EnergiKkal <=1850) ) {
			jml_kalori = 4;
		}else if( (EnergiKkal >=1851)  && (EnergiKkal <=2050) ) {
			jml_kalori = 5;
		}else if(EnergiKkal >= 2051){
			jml_kalori = 6;
		}	
		
		kode_kasus();

	}
	
	public void kode_kasus(){
		try {
			DBAdapter db = DBAdapter.getDBAdapterInstance(tesListView2.this);
				db.openDataBase();	
				SQLiteDatabase db1 = db.getReadableDatabase();
				Cursor semuaData = db1.rawQuery("SELECT * FROM Tabel_kasus",null);
				ContentValues initialValues = new ContentValues();
				
				//kasus baru 
				
				String gejala = LIST_GEJALA.toString().trim();
				
				String[] list_gjl = gejala.split(",");				
				String pesan = "";
				int no = 0;
				int m,n,n_max, n_row, nk, nAkhir, nKasus;
				float T;
				
				for(int i=1; i<=semuaData.getCount(); i++){			
					initialValues.put("Nilai_kemiripan","0");
					initialValues.put("T","0");
					initialValues.put("Nilai_kasus","0");
					db.updateRecordInDB("Tabel_kasus", initialValues, "IdKasus" + "='" +i+ "'", null);
				}							 

				for ( String item : list_gjl ) {
					String query ="SELECT * FROM view_gejala_kasus WHERE IdGejala IN ('"+item+"') ";
					Cursor selectGejala = db.selectRecordsFromDB(query,null);
					for (selectGejala.moveToFirst(); !selectGejala.isAfterLast(); selectGejala.moveToNext()) {

						int bobot2 = selectGejala.getInt(selectGejala.getColumnIndex("Bobot_gejala"));
						
						initialValues.put("Nilai_kemiripan",bobot2);
						initialValues.put("Nilai_kasus","1");
						db.updateRecordInDB("Tabel_kasus", initialValues, "IdGejala" + "='" + item + "'", null);						
					}
				}								
					
				String query2 ="SELECT SUM(Nilai_kemiripan) as Nilai_all, IdKasus, Kode, SUM(Bobot_gejala) as Bobot_all, SUM(Nilai_kasus) as Kasus_all FROM  view_gejala_kasus GROUP BY Kode";
				Cursor selectNilai = db.selectRecordsFromDB(query2,null);						
				for (selectNilai.moveToFirst(); !selectNilai.isAfterLast(); selectNilai.moveToNext()) {
					n = selectNilai.getInt(selectNilai.getColumnIndex("Nilai_all"));
					nk = selectNilai.getInt(selectNilai.getColumnIndex("Kasus_all"));
					n_max = selectNilai.getInt(selectNilai.getColumnIndex("Bobot_all"));
					int kode = selectNilai.getColumnIndex("Kode");
					nKasus = list_gjl.length - nk;
					nAkhir = n_max + nKasus;
					T = (float) n / nAkhir;
					initialValues.put("T",T);
	//				Toast.makeText(tesListView2.this,"nilainya "+n+" / "+n_max+" T "+T, Toast.LENGTH_SHORT).show();
					db.updateRecordInDB("Tabel_kasus", initialValues, "Kode" + "='" + selectNilai.getInt(kode) + "'", null);					
				}
				
				String query5 ="SELECT * FROM view_gejala_kasus";
				Cursor selectSum = db.selectRecordsFromDB(query5,null);
				for (selectSum.moveToFirst(); !selectSum.isAfterLast(); selectSum.moveToNext()) {
					
					int idkasus = selectSum.getInt(selectSum.getColumnIndex("IdKasus"));
					int kode = selectSum.getInt(selectSum.getColumnIndex("Kode"));
					int idgejala = selectSum.getInt(selectSum.getColumnIndex("IdGejala"));
					int nlkmrpn = selectSum.getInt(selectSum.getColumnIndex("Nilai_kemiripan"));
					int bobot = selectSum.getInt(selectSum.getColumnIndex("Bobot_gejala"));

					String  sumtotal = Float.toString((float)selectSum.getFloat(selectSum.getColumnIndex("T")));  
					
				}

				String query3 ="SELECT max(T) as Total, Solusi_menu FROM Tabel_kasus";
				Cursor selectMaxT = db.selectRecordsFromDB(query3,null);
			       if(selectMaxT.moveToFirst()) {
			           T_max = selectMaxT.getFloat(0);			           
			       }
			       String  sumtotal2 = Float.toString((float)T_max); 
			       if (T_max >= treshold) {
						for (selectMaxT.moveToFirst(); !selectMaxT.isAfterLast(); selectMaxT.moveToNext()) {							
							solusi = selectMaxT.getString(selectMaxT.getColumnIndex("Solusi_menu"));													
							pesan = "Berdasarkan data gejala dan kalori yang anda masukkan ditemukan,\n\nRekomendasi menu diet adalah : " + solusi + ".\n\n*)pilih lanjut untuk mengetahui menu makanan anda.";
						}
			    	   Toast.makeText(tesListView2.this,"nilainya "+list_gjl.length+" Tmax "+T_max+" solusi "+solusi, Toast.LENGTH_SHORT).show();	     
			       }else{
			    	   pesan = "Berdasarkan data gejala dan kalori yang anda masukkan, \nTidak ditemukan solusi menu yang cocok untuk anda.\n\n*)Pilih lanjut untuk menambahkan kasus baru.";
			    	   Toast.makeText(tesListView2.this,"tidak ada nilai ", Toast.LENGTH_SHORT).show();
			       }
			       
			    hasil_cbr.setText(pesan);
				semuaData.close();  				
				db.close();
				
 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu
		getMenuInflater().inflate(R.menu.activity_perhitungan_cbr, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {

	if (T_max >= treshold) {	
	   		switch (v.getId()) {			
			case R.id.menu:
				Intent b = new Intent (this, MainActivity.class);
				startActivity(b);
				finish();
				break;
				
			case R.id.rekomendasi:
				Intent b1 = new Intent (this, RekomendasiMakananPagiUmum.class);
				b1.putExtra("PAKETSOLUSI", solusi);
	        	startActivity(b1);
				finish();
				break;
			
				}
	   }else{
	   		switch (v.getId()) {			
			case R.id.menu:
				Intent a = new Intent (this, MainActivity.class);
				startActivity(a);
				finish();
				break;
				
			case R.id.rekomendasi:
				Intent k = new Intent (this, KasusBaru2.class);
				startActivity(k);
				finish();
				break;
			
				}	   		
	   }	

	}
}