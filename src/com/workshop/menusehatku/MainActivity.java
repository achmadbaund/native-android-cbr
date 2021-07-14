package com.workshop.menusehatku;

import java.io.IOException;
import com.workshop.menusehatku.database.DBAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	public DBAdapter dbhelper;
	public SQLiteDatabase db;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        View HitungKaloriButton = findViewById(R.id.hitungkalori);
        HitungKaloriButton.setOnClickListener(this);
        
        
        View AboutButton = findViewById(R.id.about);
        AboutButton.setOnClickListener(this);
        
        //copy file database ke sistem
        dbhelper = new DBAdapter(this);
		db = dbhelper.getReadableDatabase();
		try {
			dbhelper.copyDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		} db.close();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//getmenuInflater adalah aktivity untuk menampilkan menu
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.hitungkalori:
			Intent a = new Intent (this, Hitungkalori.class);
			startActivity(a);
			finish();
			break;
		
		case R.id.about:
			Intent d = new Intent (this, About.class);
			startActivity(d);
			finish();
			break;
		
		}
	}
    
}
