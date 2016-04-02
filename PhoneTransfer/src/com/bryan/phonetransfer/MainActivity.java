package com.bryan.phonetransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	//start button from view
		private Button startButton;
		
		//end button from view
		private Button stopButton;
		
		//exit button from view
		private Button exitButton;
		
		//timer check box
		private CheckBox timerCheckBox;
		
		//editText for phoneNumber
		private EditText phoneNumber;
		
		//get value from editable field
		private String phoneNumberText;
		
		//get status of timerCheckBox
		private boolean isTimerStart;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startButton = (Button) findViewById(R.id.startButton);
		stopButton = (Button) findViewById(R.id.stopButton);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		timerCheckBox = (CheckBox) findViewById(R.id.timerCheck);
		exitButton = (Button) findViewById(R.id.exitButton);
		
		
		
		//get state change of check box
		timerCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					isTimerStart = true;
				}
				else{
					isTimerStart = false;
				}
			}
		});
		
		
		startButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentPhone = new Intent(MainActivity.this, PhoneListnerService.class);
				
				//get phone number in app
				phoneNumberText = phoneNumber.getText().toString().trim(); 
				
				if (!phoneNumberText.equals("")) {
					intentPhone.putExtra("targetPhone", phoneNumberText);
					intentPhone.putExtra("isTimerStart", isTimerStart);
					startService(intentPhone);
					Toast.makeText(getApplicationContext(), "Service Started", 5*1000).show();
				}
				
			}
		});
		
		stopButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,PhoneListnerService.class);
				stopService(intent);
				Toast.makeText(getApplicationContext(), "Service Stopped", 5*1000).show();
			}
			
		});
		
		exitButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
			
		});
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
