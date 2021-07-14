package com.workshop.menusehatku;

import com.workshop.menusehatku.database.DBAdapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class KasusBaru2 extends Activity {
	private EditText inputNama, inputTinggi, inputBerat, inputUsia;
	
	private RadioButton Ada, Tidak, gejalaBtn;
	private Button Proses;
	private String solusimenu, inputmenu, LIST_GEJALA;
	private RadioGroup gejala;
	
	public Intent i;
	private double BEE, EnergiKkal;
	private Cursor cursor, cursor2;
	private ListAdapter adapter;
	private TextView hasil_cbr;
	private String solusi = null;
	private float treshold = (float) 0.6;
	private float T_max;
	private int jml_kalori, IdKode;
	private Spinner input_menu, input_olahraga;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kasus_baru);
		
		input_menu = (Spinner) findViewById(R.id.input_menu);			
		SharedPreferences sharedPreferences = getSharedPreferences(
			"pindahActivity", 0);
		EnergiKkal = Float.parseFloat(sharedPreferences.getString(
			"EnergiKkal", ""));
		LIST_GEJALA= sharedPreferences.getString("list_gejala","");		

		if(EnergiKkal <= 1250) {			
			jml_kalori = 1;
		}else if( (EnergiKkal >= 1250)  && (EnergiKkal <= 1450) ) {			
			jml_kalori = 2;
		}else if( (EnergiKkal >= 1451)  && (EnergiKkal <=1650) ) {			
			jml_kalori = 3;
		}else if( (EnergiKkal >= 1651)  && (EnergiKkal <= 1850) ) {			
			jml_kalori = 4;
		}else if( (EnergiKkal >= 1851)  && (EnergiKkal <= 2050) ) {			
			jml_kalori = 5;
		}else if(EnergiKkal >= 2051){			
			jml_kalori = 6;
		}
		
	    Button next = (Button) findViewById(R.id.menu2);
	    next.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	            Intent myIntent = new Intent(view.getContext(), MainActivity.class);
	            startActivity(myIntent);
	        }
	    });
	    
	   input_menu.setOnItemSelectedListener(new OnItemSelectedListener() {
	   	   //ketika memilih salah satu item
	   	      @Override
	   	      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	   	       //ambil nilai dari item spinner yang dipilih lalu disimpan pada variabel jk
	   	          inputmenu = String.valueOf(parentView.getSelectedItem());
	   	          	   	          
	   	      }
	   	      @Override
	   	      public void onNothingSelected(AdapterView<?> parentView) {
	   	          // your code here
	   	      }
	   	  });	    
	}

	public void SimpanKasus1(View view){
		try {
			//set semua variabel agar dapat dibaca isinya			
				DBAdapter db = DBAdapter.getDBAdapterInstance(KasusBaru2.this);
				db.openDataBase();
			 	String query ="select max(Kode) as Kode from Tabel_kasus";
			 	Cursor selectTabel = db.selectRecordsFromDB(query,null);
								
				//kasus baru 
				String gejala = LIST_GEJALA.toString().trim();
				
				String[] list_gjl = gejala.split(","); 	
				String pesan = "";

				int m,n,n_max, n_row;
				int kode = 0;
				float T;
				
					for (selectTabel.moveToFirst(); !selectTabel.isAfterLast(); selectTabel.moveToNext()) {
						kode = selectTabel.getInt(selectTabel.getColumnIndex("Kode"))+1;
					}					
					for (int i = 0; i < list_gjl.length; i++) {
						
						ContentValues initialValues = new ContentValues();
							initialValues.put("Solusi_menu",inputmenu);
							initialValues.put("IdKalori",jml_kalori);	
							initialValues.put("Nilai_kemiripan","0");
							initialValues.put("T","0");
							initialValues.put("IdGejala",list_gjl[i]);
							initialValues.put("Kode",kode);												
						 	db.insertRecordsInDB("Tabel_kasus",null,initialValues);				        
					}

					String query2 ="SELECT Kode, Solusi_menu FROM view_gejala_kasus WHERE Kode IN ('"+kode+"') GROUP BY Solusi_menu";
					Cursor selectMenu = db.selectRecordsFromDB(query2,null);
					for (selectMenu.moveToFirst(); !selectMenu.isAfterLast(); selectMenu.moveToNext()) {
						solusi = selectMenu.getString(selectMenu.getColumnIndex("Solusi_menu")).toString();
						IdKode = selectMenu.getInt(selectMenu.getColumnIndex("Kode"));
						pesan = "Data kasus anda berhasil ditambahkan.\n\nRekomendasi menu diet adalah: " + solusi + ".\n\n*)pilih lanjut untuk mengetahui menu makanan anda.";
						Toast.makeText(this,"kasus "+IdKode, Toast.LENGTH_SHORT).show();					
					}
				selectTabel.close();
				db.close();				

		}catch(Exception e){
			e.printStackTrace();
		}           
		     //jalankan activity result					
		Intent b1 = new Intent (this, RekomendasiMakananPagiUmum.class);
		b1.putExtra("PAKETSOLUSI", solusi);
	    startActivity(b1);
		finish();		
	}	
}
