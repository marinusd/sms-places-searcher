package com.plesba.SMSsearcher;

import java.util.ArrayList;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SMSreplier extends IntentService {
	//private final String DEBUG_TAG = getClass().getSimpleName().toString();
	public SMSreplier() {
		super("SMSreplier");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String msg = intent.getStringExtra("sms");
		String address = intent.getStringExtra("address");
		// Log.i(DEBUG_TAG, "service got intent from " + address);
		ResultsFetcher rf = new ResultsFetcher();
		String results = rf.getSearchResults(msg);
		sendSMS(address, results);
		stopSelf();
	}

	private static String SENT = "SMS_SENT";
	private static String DELIVERED = "SMS_DELIVERED";
	private static int MAX_SMS_MESSAGE_LENGTH = 160;

	// ---sends an SMS message to another device---
	public void sendSMS(String phoneNumber, String message) {
		// Log.i(DEBUG_TAG, "sendSMS starting");
		Context mContext = getBaseContext();
		PendingIntent piSent = PendingIntent.getBroadcast(mContext, 0,
				new Intent(SENT), 0);
		PendingIntent piDelivered = PendingIntent.getBroadcast(mContext, 0,
				new Intent(DELIVERED), 0);
		SmsManager smsManager = SmsManager.getDefault();
		int length = message.length();
		if (length == 0) {
			//Log.i(DEBUG_TAG, "empty message for " + phoneNumber);
		} else {
			if (length > MAX_SMS_MESSAGE_LENGTH) {
				ArrayList<String> messagelist = smsManager
						.divideMessage(message);
				smsManager.sendMultipartTextMessage(phoneNumber, null,
						messagelist, null, null);
			} else {
				smsManager.sendTextMessage(phoneNumber, null, message, piSent,
						piDelivered);
			}
			// Log.i(DEBUG_TAG, "sent '" + message + "' to " + phoneNumber);
		}
	}
}
