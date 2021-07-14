package com.workshop.menusehatku;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class Konfirmasi_solusi extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_konfirmasi_solusi);
		
        View btnAda = findViewById(R.id.MenuUtama2);
        btnAda.setOnClickListener(this);
        
        View btnTidakAda = findViewById(R.id.tambahKasus);
        btnTidakAda.setOnClickListener(this);
        
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.MenuUtama2:
				Intent b = new Intent (this, MainActivity.class);
				startActivity(b);
				finish();
				break;
			case R.id.tambahKasus:
				Intent b1 = new Intent (this, KasusBaru2.class);
				b1.putExtra("SOLUSINYA", "");
	        	startActivity(b1);
				finish();
				break;
		}
	
	}

}
