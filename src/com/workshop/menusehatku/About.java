package com.workshop.menusehatku;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class About extends Activity implements OnClickListener {
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
       
        View kembaliButton = findViewById(R.id.back);
        kembaliButton.setOnClickListener(this);
        
        
        
    }

   	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.back:
			Intent a = new Intent (this, MainActivity.class);
			startActivity(a);
			finish();
			break;
		
			}
	}
    
}
