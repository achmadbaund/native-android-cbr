
package com.workshop.menusehatku;
import java.util.ArrayList;
import java.util.Arrays;

import com.workshop.menusehatku.database.DBAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class tesListView extends ListActivity{
	

	DBAdapter dbHelper;
	SQLiteDatabase db;
	
	private TextView pesan, sarapan, beratsarapan, cemilpagi, beratcemilpagi,
	makansiang, beratsiang, cemilsiang, beratcemilsiang, makanmalam,
	beratmalam, cemilmalam, beratcemilmalam, utama;
	private ListView listMenu;
	private String saur, beratsaur, _id, jarak, total_kalori, nama, makansg,
		beratmakansg, makanml, beratmakanml, tinggi, beratBadan, usia,
		usiaKandungan, nama_paket, cemilpg, brtcmilpg, cemilsg, brtcmilsg,
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
	private float treshold = (float) 0.5;
	private int jml_kalori;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 
		this.listMenu = (ListView) this.findViewById(R.id.list_group_makanan);
		SharedPreferences sharedPreferences = getSharedPreferences(
				"pindahActivity", 0);
		nama = sharedPreferences.getString("nama", "");
		tinggi = sharedPreferences.getString("tinggi", "");
		beratBadan = sharedPreferences.getString("berat", "");
		usia = sharedPreferences.getString("usia", "");
		usiaKandungan = sharedPreferences.getString("usia", "");
		EnergiKkal = Float.parseFloat(sharedPreferences.getString(
				"EnergiKkal", ""));
		LIST_GEJALA= sharedPreferences.getString("list_gejala","");
		
		
    	dbHelper = new DBAdapter(this);
     	db = dbHelper.getReadableDatabase();
     	//String[] nama = new String [] {"diksa","khanoli","radiksa"};
     	//dbHelper = new DBAdapter(this);
     	
     	//SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor s = db.rawQuery("select *,(select sum(total_kalori ) from view_menu_solusi M where  M.paket_solusi=T.Paket_solusi ) as total_kalori_all from Tabel_kasus1 T",null);
		
		String[] f = new String[s.getCount()];
     	
     	f = kode_menu_makanan();
     	
     	String[] nama = new String[s.getCount()];
     	//Arrays.fill(nama, null);
     	//nul kan 
     	
     	for(int i= 0; i < s.getCount(); i++){
			nama[i] ="";
     	}
     	
     	int j=0;
     	for(int i= 0; i < s.getCount(); i++){
     		//float threshold = (float) 0.5;
			if(f[i] != null ) {     			
     		    boolean sama=false;
     		    for(int kk = 0;kk<nama.length;kk++){
					//Toast.makeText(this, nama[kk]+""+String.valueOf(f[i]), Toast.LENGTH_SHORT).show();
     				if(String.valueOf(f[i].trim()).equals(nama[kk].trim())){
         		    	sama=true;
         		    }
     		    }
     			if(sama==false){
					nama[j] = String.valueOf(f[i]).trim();
	     			j++;
	     		}
			}
     	}
     	
     	String[] nama_list = new String[j];
     	int k=1;
     	

     	if(j<1){
	   		 Intent ii = new Intent(tesListView.this, Konfirmasi_solusi.class);
	   		 startActivityForResult(ii,0);
	   		 finish();
     	}
     	
     	
     	for(int i= 0; i < j; i++){
     		nama_list[i] = "Menu "+k + " (" + nama[i]+ ")";
     		k++;
     	}
     	
        // Binding Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.review_detail, R.id.label, nama_list));
        ListView lv = getListView();

        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
          @Override
		public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
        	 
        	 // String id_user = ((TextView) view.findViewById(R.id.label)).getText().toString();
        	  // selected item 
        	  String SOLUSINYA = ((TextView) view).getText().toString();
        	  
        	  // Launching new Activity on selecting single List Item
        	  Intent i = new Intent(getApplicationContext(), RekomendasiMakananPagiUmum.class);
        	  // sending data to new activity
        	  char[] c_arr = SOLUSINYA.toCharArray();
        	  
        	  //Toast.makeText(tesListView.this, ""+c_arr[c_arr.length-2], Toast.LENGTH_SHORT).show();
      		
        	  String solusi = ""+c_arr[c_arr.length-2];
        	  
        	  String mn="";
        	  int posisi1 = 0,posisi2=0;
        	  for(int k=0;k<c_arr.length;k++){
        		  if(c_arr[k]=='(') posisi1=k;
        		  if(c_arr[k]==')') posisi2=k;
        	  }
        	  
        	  for(int j=posisi1+1;j<c_arr.length-1;j++){
        		  mn = mn +""+ c_arr[j];
        	  }
        	  
        	  Cursor pkt = db.rawQuery("select Paket_solusi,(select sum(total_kalori ) from view_menu_solusi M where  M.paket_solusi=T.Paket_solusi ) as total_kalori_all from Tabel_kasus1 T where trim(total_kalori_all)='"+mn.trim()+"'",null);
        	  String p = "";
        	  if(pkt.moveToFirst()){
      			for(; !pkt.isAfterLast();pkt.moveToNext()){
      				p = pkt.getString(pkt.getColumnIndex("Paket_solusi")).toString();
      			}
        	  }
      		
        	  i.putExtra("list_gejala", LIST_GEJALA);
        	  i.putExtra("SOLUSINYA", solusi);
        	  i.putExtra("MENUNYA", mn);
        	  i.putExtra("PAKET", p);
        	  
        	  startActivity(i);
        	  finish();
        	 // startActivity(i);
        	
          }
        } );
	}
	
	
	/*
	 * menu makanan yg di atas treshold
	 */
	public String[] kode_menu_makanan(){
		//hasil_cbr = (TextView)findViewById(R.id.hasil_cbr);
		
		//dbHelper = new DBAdapter(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor selectedMenu = db.rawQuery("select *,(select sum(total_kalori ) from view_menu_solusi M where  M.paket_solusi=T.Paket_solusi ) as total_kalori_all from Tabel_kasus1 T",null);
		
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
		n_row= selectedMenu.getCount();
		
		//kasus baru 
		int TB = Integer.parseInt(tinggi.replace(".0", ""));
		int BB = Integer.parseInt(beratBadan.replace(".0", ""));
		int UIH =Integer.parseInt(usia.replace(".0", ""));
		int UK = Integer.parseInt(usiaKandungan.replace(".0", ""));
		String gejala = LIST_GEJALA.toString().trim();
		
		String[] list_gjl = gejala.split(","); 
		
		float[] T = new float[n_row];
		String[] T_max = new String[n_row];
		
		String[] ST = new String[n_row];

		float threshold = (float) 0.5;
		
		int i=0;
		
		float nilai_max =0;
		
		int j =0;
		
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
				
				//T_max[j]= selectedMenu.getString(selectedMenu.getColumnIndex("total_kalori_all")).toString(); 
				
				if(T[i] >= threshold){
					T_max[j]= selectedMenu.getString(selectedMenu.getColumnIndex("total_kalori_all")).toString(); 
					//" : " +selectedMenu.getString(selectedMenu.getColumnIndex("paket_solusi")).toString() + ":" + T[i];
					j = j+1;
				}
				
				
				if(nilai_max<T[i] && threshold < T[i] ){
					nilai_max= T[i];
					solusi = selectedMenu.getString(selectedMenu.getColumnIndex("Paket_solusi")).toString();
					Toast.makeText(this, solusi, Toast.LENGTH_SHORT).show();
				}
				
				
				i = i +1;
				
			}
		}
		
		return T_max;
	
	}

	
	}

