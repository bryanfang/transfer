package com.bryan.phonetransfer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * monitor all incoming call
 * @author I312473
 *
 */
public class PhoneListnerBroadcast extends BroadcastReceiver {

	private String targetNumber;
	
	
	public String getTargetNumber() {
		return targetNumber;
	}

	public void setTargetNumber(String targetNumber) {
		this.targetNumber = targetNumber;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		TelephonyManager tManager = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
		tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		this.setTargetNumber(intent.getStringExtra("targetNumber"));
	}
	
	PhoneStateListener listener = new PhoneStateListener(){
		@Override
		public void onCallStateChanged(int state, String incomingNumber){
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
			{
				//ringing handle, send out message if target number isn't empty
				
				break;
			}
				
				

			default:
				break;
			}
		}
	};

}
