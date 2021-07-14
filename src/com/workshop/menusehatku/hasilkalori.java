package com.workshop.menusehatku;

import com.workshop.menusehatku.database.DBAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import java.io.IOException;


public class hasilkalori extends Activity implements OnClickListener {
		
	private TextView HasilKalori;
	private float EnergiKkal, usia, beratBadan, tinggi;
	private String nama, kategoriBB, jenisKelamin;
	public DBAdapter dbhelper;
	public SQLiteDatabase db;	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hasilkalori);
       
        View prosesButton = findViewById(R.id.next);
        prosesButton.setOnClickListener(this);
        
        View proses1Button = findViewById(R.id.back);
        proses1Button.setOnClickListener(this);
        
        //copy file database ke sistem
        dbhelper = new DBAdapter(this);
		db = dbhelper.getReadableDatabase();
		try {
			dbhelper.copyDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		} db.close();
		
		String hasil = "";
        
        SharedPreferences sharedPreferences = getSharedPreferences("pindahActivity", 0);
        HasilKalori = (TextView)findViewById(R.id.txtHasilKalori);
        
        //mengambil value dan digunakan kembali dari memory tadi
        nama = sharedPreferences.getString("nama", "");        
        jenisKelamin = sharedPreferences.getString("jeniskelamin", "");
        usia = Float.parseFloat(sharedPreferences.getString("usia", ""));
        beratBadan = Float.parseFloat(sharedPreferences.getString("berat", ""));
        tinggi = Float.parseFloat(sharedPreferences.getString("tinggi", ""));
        kategoriBB = sharedPreferences.getString("kategoriBB", "");
        EnergiKkal = Float.parseFloat(sharedPreferences.getString("EnergiKkal", ""));
        
        if (jenisKelamin == "Perempuan") {
        	hasil = "Ny. " + nama + ", berdasarkan data yang anda masukkan didapatkan,\n\nJenis Kelamin " + jenisKelamin + "\nGolongan Berat Badan : " + kategoriBB + "\nKebutuhan Kalori : " + EnergiKkal + " Kkal/hari\n\n*)pilih lanjut untuk mengetahui rekomendasi diet anda";
		}else{
			hasil = "Tn. " + nama + ", berdasarkan data yang anda masukkan didapatkan,\n\nJenis Kelamin " + jenisKelamin + "\nGolongan Berat Badan : " + kategoriBB + "\nKebutuhan Kalori : " + EnergiKkal + " Kkal/hari\n\n*)pilih lanjut untuk mengetahui rekomendasi diet anda";
		}
        HasilKalori.setText(hasil);
        
    }

   	@Override
	public void onClick(View v) {
   		
   	//jika dia obesitas masukkan gejala
	if (kategoriBB == "Obesitas") {		
	   		switch (v.getId()) {			
			case R.id.back:
				Intent a = new Intent (this, MainActivity.class);
				startActivity(a);
				finish();
				break;
				
			case R.id.next:
				Intent k = new Intent (this, Gejala.class);
				startActivity(k);
				finish();
				break;
			
				}
	   }else{
	   		switch (v.getId()) {			
			case R.id.back:
				Intent b = new Intent (this, MainActivity.class);
				startActivity(b);
				finish();
				break;
				
			case R.id.next:
				Intent b1 = new Intent (this, RekomendasiMakananPagiUmum.class);
				b1.putExtra("PAKETSOLUSI", "");
	        	startActivity(b1);
				finish();
				break;
			
				}
	   }
	}
    
}
