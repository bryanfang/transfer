package com.bryan.phonetransfer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.telephony.SmsMessage;
import android.util.Log;

public class ReceiveMessageBroadcast extends BroadcastReceiver {

	private static final String TAG = "Receive Message";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "On receive message");
		Bundle bundle = intent.getExtras();
		SmsMessage message = null;
		if (bundle != null) {
			Object[] smsObj = (Object[]) bundle.get("pdus");
			for (Object object : smsObj) {
				message = SmsMessage.createFromPdu((byte[]) object);
				messageHandler(context, message);
			}
		}
	}

	// message handler
	@SuppressLint("InlinedApi")
	/**
	 * @param: message,
	 *             the message object from receiver
	 * @return: null
	 * @author I312473
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void messageHandler(Context context, SmsMessage message) {
		String messageBody = message.getDisplayMessageBody(); // obtain message
																// body
		if (messageBody.startsWith("ALM")) {
			// start timer
			Log.i(TAG, "Receive Message - Start Alarm");
			Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
			intent.putExtra(AlarmClock.EXTRA_MESSAGE, "WAKE UP!NOW!");
			context.startActivity(intent);
		}
		// deal with other case

	}

}
