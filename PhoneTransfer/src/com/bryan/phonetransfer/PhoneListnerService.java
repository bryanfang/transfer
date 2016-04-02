package com.bryan.phonetransfer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneListnerService extends Service {

	private static final String TAG = "PhoneListnerService";
	
	private String targetNumber;
	
	private boolean isTimerStart;
	
	private StringBuilder message;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras(); 
		targetNumber = bundle.getString("targetPhone"); //obtain target number
		isTimerStart = bundle.getBoolean("isTimerStart");
		Log.i(TAG, "PhoneListnerService - target Number:"+targetNumber+"isTimerStart:"+isTimerStart);
		return null;
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "PhoneListnerService - onDestroy");
		super.onDestroy();
	};
	
	@Override
	public void onCreate(){
		Log.i(TAG, "PhoneListnerService - onCreate");
		super.onCreate();
		//get phone service
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		PhoneStateListener listener = new PhoneStateListener(){
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onCallStateChanged(int state, String incomingNumber){
				switch(state){
				case TelephonyManager.CALL_STATE_IDLE:
					break;
					
				case TelephonyManager.CALL_STATE_RINGING: //Call incoming
					{
						if (isTimerStart) {
							message.append("ALM");
						}
						if (!targetNumber.equals("")) {
							message.append("+").append(wrappedNumber(incomingNumber));
						}
						
						//get current date and time in the system
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						Date curDate = new Date(System.currentTimeMillis()); 
						String date = format.format(curDate);
						message.append("---").append(date);
						//send message to targetNumber
						sendSMS(targetNumber, message.toString());
					}
					break;
					
				default: 
					break;
				}
				
				
			}
		};
		
		//monitor call state
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	//send message to target number
	private void sendSMS(String phoneNumber, String message){
		Log.i(TAG, "PhoneListnerService - send message to target");
		if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
			SmsManager smsManager = SmsManager.getDefault();
			if (message.length()>70) {
				List<String> contents = smsManager.divideMessage(message);
				for(String smString : contents){
					smsManager.sendTextMessage(phoneNumber, null, smString, null, null);
				}
			}
			else{
				smsManager.sendTextMessage(phoneNumber, null, message, null, null);
			}
		}
	}

	//wrap incoming number for sending message
	@SuppressWarnings("null")
	private String wrappedNumber(String number){
		StringBuilder wrappedPhoneNumber = null;
		for(int i=0;i<number.length();i++){
			char n = number.charAt(i);
			switch (n) {
			case '0':
				wrappedPhoneNumber.append("+Z+");
				break;
			case '1':
				wrappedPhoneNumber.append("+O+");
				break;
			case '2':
				wrappedPhoneNumber.append("+TW+");
				break;
			case '3':
				wrappedPhoneNumber.append("+TR+");
				break;
			case '4':
				wrappedPhoneNumber.append("+FO+");
				break;
			case '5':
				wrappedPhoneNumber.append("+FV+");
				break;
			case '6':
				wrappedPhoneNumber.append("+SX+");
				break;
			case '7':
				wrappedPhoneNumber.append("+SV+");
				break;
			case '8':
				wrappedPhoneNumber.append("+E+");
				break;
			case '9':
				wrappedPhoneNumber.append("+N+");
				break;

			default:
				break;
			}
		}
		
		return wrappedPhoneNumber.toString();
	}
}
