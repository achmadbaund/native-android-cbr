package com.workshop.menusehatku;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.workshop.menusehatku.database.DBAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PerhitunganCBR extends Activity implements OnClickListener {

	DBAdapter dbHelper;
	private TextView hasil_cbr;
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
	private String solusi = null;
	private int jml_kalori;
	
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
		//usiaKandungan = sharedPreferences.getString("usiaKandungan", "");
		EnergiKkal = Float.parseFloat(sharedPreferences.getString(
				"EnergiKkal", ""));
		LIST_GEJALA= sharedPreferences.getString("list_gejala","");
		
		
		hasil_cbr = (TextView)findViewById(R.id.hasil_cbr);
		

		View kb = findViewById(R.id.button1);
		kb.setOnClickListener(this);
		
		View ke = findViewById(R.id.menu);
		ke.setOnClickListener(this);
		
		

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
		
		dbHelper = new DBAdapter(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor selectedMenu = db.rawQuery("select * from Tabel_kasus1 where jumlah_kalori='"+jml_kalori+"'",null);
		
		int m,n,n_max, n_row;
		
		n_max = 0;
		n_row= selectedMenu.getCount();
		
		if(selectedMenu.moveToFirst()){
			//while(selectedMenu.moveToNext()){
			for(; !selectedMenu.isAfterLast();selectedMenu.moveToNext()){
				n =  selectedMenu.getInt(selectedMenu.getColumnIndex("G1")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G2")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G3")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G4")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G5")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G6")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G7")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G8")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G9")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G10")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G12")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G13")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G14")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G15")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G16")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G17")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G18")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G19")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G21")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G21")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G22")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G23")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G24")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G25")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G26")) +
					 selectedMenu.getInt(selectedMenu.getColumnIndex("G27"));
				if(n_max < n ) n_max=n;
			}
		}
		
		
		//kasus baru 
		int TB = Integer.parseInt(tinggi.replace(".0", ""));
		int BB = Integer.parseInt(beratBadan.replace(".0", ""));
		int UIH =Integer.parseInt(usia.replace(".0", ""));
		//int UK = Integer.parseInt(usiaKandungan.replace(".0", ""));
		String gejala = LIST_GEJALA.toString().trim();
		
		float[] T = new float[n_row];
		String[] ST = new String[n_row];
		String[] list_gjl = gejala.split(","); 
		

		if(n_max < list_gjl.length) n_max = list_gjl.length;
		
		int i=0;
		
		if(selectedMenu.moveToFirst()){
			//while(selectedMenu.moveToNext()){
			for(; !selectedMenu.isAfterLast();selectedMenu.moveToNext()){
				m = 0;
				for(int k=0;k<list_gjl.length;k++){
					//Toast.makeText(this, list_gjl[k], Toast.LENGTH_SHORT).show();
					
					
					if(list_gjl[k].equals("1")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G1")).toString().equals("1")){
							m = m +1;
						}
						
					}else if(list_gjl[k].equals("2")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G2")).toString().equals("1")){
							m = m +1;
						}
					}else
						if(list_gjl[k].equals("3")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G3")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("4")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G4")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("5")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G5")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("6")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G6")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("7")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G7")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("8")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G8")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("9")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G9")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("10")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G10")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("11")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G11")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("12")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G12")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("13")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G13")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("14")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G14")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("15")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G15")).toString().equals("1")){
								m = m +1;
						}
					}else 
						if(list_gjl[k].equals("16")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G16")).toString().equals("1")){
								m = m +1;
						}
					}else
						if(list_gjl[k].equals("17")){
							if(selectedMenu.getString(selectedMenu.getColumnIndex("G17")).toString().equals("1")){
								m = m +1;
							}
					}else if(list_gjl[k].equals("18")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G18")).toString().equals("1")){
							m = m +1;
							}
					}else if(list_gjl[k].equals("19")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G19")).toString().equals("1")){
							m = m +1;
							}
					}else if(list_gjl[k].equals("20")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G20")).toString().equals("1")){
							m = m +1;
							}
					}else if(list_gjl[k].equals("21")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G21")).toString().equals("1")){
							m = m +1;
							}
					}else if(list_gjl[k].equals("22")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G22")).toString().equals("1")){
							m = m +1;
							}
					}else if(list_gjl[k].equals("23")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G23")).toString().equals("1")){
							m = m +1;
							}
					}else if(list_gjl[k].equals("24")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G24")).toString().equals("1")){
							m = m +1;
							}
					}else if(list_gjl[k].equals("25")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G25")).toString().equals("1")){
							m = m +1;
							}
					}else if(list_gjl[k].equals("26")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G26")).toString().equals("1")){
							m = m +1;
							}
					}else if(list_gjl[k].equals("27")){
						if(selectedMenu.getString(selectedMenu.getColumnIndex("G27")).toString().equals("1")){
							m = m +1;
							}
					}
		
				}	
				
				ST [i] ="kasus :" + selectedMenu.getString(selectedMenu.getColumnIndex("Kode")).toString()+"="+m+"/"+n_max;
				T[i]= (float) m / n_max;
				
				i = i +1;

			}
		}
		
	    String result = "";
	    
	    
	    NumberFormat nf = NumberFormat.getInstance();
	    nf.setMinimumFractionDigits(2);
	    nf.setMaximumFractionDigits(2);
	   // if(i==0){
	    	//pesan.setText("tidak ditemukan kasus yang sama" );
	   // }
	   // else {
	    for(int j=0;j < T.length; j++){
	    	String output = nf.format(T[j]);
		    result =result +"\n" +ST[j]+"="+ output ;
		   	    }
	    hasil_cbr.setText(result);
	    }
	//}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu
		getMenuInflater().inflate(R.menu.activity_perhitungan_cbr, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.menu:
			Intent a = new Intent (this, KasusBaru2.class);
			startActivity(a);
			finish();
			break;
			
		case R.id.button1:
			Intent k = new Intent (this, Edit_kasus.class);
			startActivity(k);
			finish();
			break;
		
		}
	}

}
