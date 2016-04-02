package com.bryan.phonetransfer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;


public class SMSContentObserver extends ContentObserver {
	private Context context;
	private Handler handler;  //update UI thread
	private String incomingNumber;

	private static final int MESSAGE_CASE = 1;
	
	public void setIncomingNumber(String incomingNumber) {
		this.incomingNumber = incomingNumber;
	}

	public SMSContentObserver(Context context, Handler handler) {
		super(handler);
		this.context = context;
		this.handler = handler;
	}
	
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		deleteSMS(context, incomingNumber);
	}

	private void deleteSMS(Context context, String incomingNumber) {
		try {
			ContentResolver contentObserver = context.getContentResolver();
			
			//query short messages
			Uri sentMessage = Uri.parse("content://sms/inbox");
			
			Cursor cursor = contentObserver.query(sentMessage, new String[]{"_id","thread_id"}, null, null, null);
			String sender = cursor.getString(cursor.getColumnIndex("address")).trim();  //get phone number of sender
			String content = cursor.getString(cursor.getColumnIndex("body")); //get received message
			if ((cursor!=null) && (cursor.moveToFirst())) {
				while (cursor.moveToNext()) {
					if (sender.equals(incomingNumber)) {
						long threadID = cursor.getLong(1);
						contentObserver.delete(Uri.parse("content://sms/conversations/"+threadID), null, null);
					}
				}
			}
			cursor.close();
			handler.obtainMessage(MESSAGE_CASE, content).sendToTarget(); //update main UI thread
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
