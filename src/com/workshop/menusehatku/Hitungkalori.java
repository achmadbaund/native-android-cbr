package com.workshop.menusehatku;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

public class Hitungkalori extends Activity {
	private EditText inputNama, inputTinggi, inputBerat, inputUsia;
	
	private RadioButton Ada, Tidak, gejalaBtn;	
	private String nama, kategoriBB, jenisKel;
	private RadioGroup gejala;
	private Spinner jenis_kelamin;
	public Intent i;
	public SharedPreferences shared;
	public SharedPreferences.Editor editor;
	private double BEE, tinggi, berat, usia, BBi, golBB, EnergiKkal;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hitungkalori);
				
		inputNama = (EditText) findViewById (R.id.input_nama);
		inputTinggi = (EditText) findViewById (R.id.input_tinggi);
		inputBerat = (EditText) findViewById (R.id.input_berat);
		inputUsia = (EditText) findViewById (R.id.input_usia);				
		gejala = (RadioGroup) findViewById (R.id.gejala);		
		shared = getSharedPreferences("pindahActivity", 0);
		editor = shared.edit();
		editor.clear();
		
		addItemsOnSpinner2();			
		
	    Button next = (Button) findViewById(R.id.kembali);
	    next.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	            Intent myIntent = new Intent(view.getContext(), MainActivity.class);
	            startActivity(myIntent);
	        }
	    });	    	    	    	    
	}
	
	  // add items into spinner dynamically
	  public void addItemsOnSpinner2() {
	 
		jenis_kelamin = (Spinner) findViewById(R.id.jenis_kelamin);
		List<String> list = new ArrayList<String>();
		list.add("Perempuan");
		list.add("Laki-laki");		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jenis_kelamin.setAdapter(dataAdapter);
	  }


	public void hitungKalori(View view){
		try {
			//set semua variabel agar dapat dibaca isinya
			nama = inputNama.getText().toString();
			jenisKel = jenis_kelamin.getSelectedItem().toString();
			tinggi = Double.parseDouble(inputTinggi.getText().toString());
			berat = Double.parseDouble(inputBerat.getText().toString());
			usia = Double.parseDouble(inputUsia.getText().toString());
			
			//AlertDialog alert1 = new AlertDialog.Builder(this).create();
			//alert1.setMessage(selectedId+"");
			//alert1.show();
			
			//cari berat badan
			BBi = 0.9*(tinggi - 100);
			golBB = (berat / BBi) * 100;
			
			if (jenisKel == "Perempuan") {
				BEE = 655 + (9.6 * BBi) +(1.7 * tinggi) -(4.7 * usia);				
			}else{
				BEE = 66 + (13.7 * BBi) +(5 * tinggi) -(6.8 * usia);
			}	
			

			// Total Energy Expenditure (TEE) 		
			if (golBB < 90) {
				
				kategoriBB = "Kurus";
				EnergiKkal = BEE * 1.4;
				
			}else if( (golBB >= 90)  && (golBB <= 110) ){
				
				kategoriBB = "Normal";
				EnergiKkal = BEE * 1.4;
				
			}else if( (golBB >= 111)  && (golBB <= 120) ){
				
				kategoriBB = "Gemuk";
				EnergiKkal = BEE * 1.3;
				
			}else if(golBB > 120){
				
				kategoriBB = "Obesitas";
				EnergiKkal = BEE * 1.2;
			}			

			//simpan ke memory
			editor.putString("nama", nama);
			editor.putString("tinggi", Double.toString(tinggi));
			editor.putString("berat", Double.toString(berat));
			editor.putString("usia", Double.toString(usia));
			editor.putString("jeniskelamin", jenisKel);
			editor.putString("BEE", Double.toString(BEE));
			editor.putString("kategoriBB", kategoriBB);
			editor.putString("EnergiKkal", Double.toString(EnergiKkal));
			editor.commit();

			//catat semua di logcat
			Log.d("MenusehatKu", "BEE : " + BEE);
			Log.d("MenusehatKu", "EnergiKkal : " + EnergiKkal + " kkal");

		}catch(Exception e){
			e.printStackTrace();
		}
		
		if ( ( !nama.equals("")) && ( !inputTinggi.getText().toString().equals("")) 
				&& ( !inputBerat.getText().toString().equals("")) 
				&& ( !inputUsia.getText().toString().equals("")) 
				&& ( !jenis_kelamin.equals("")) ){
           
           //jalankan activity result
			
			Intent i = new Intent (this, hasilkalori.class);
            startActivity(i);
            finish();
		}else{
			Toast.makeText(Hitungkalori.this, "Semua Data Harus diisi", Toast.LENGTH_LONG).show();
			
		}
	}
	
}
