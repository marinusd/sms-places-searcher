package com.plesba.SMSsearcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSreceiver extends BroadcastReceiver {
	private final String DEBUG_TAG = getClass().getSimpleName().toString();
	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	//private Context mContext;
	private Intent mIntent;

	// Retrieve SMS
	public void onReceive(Context context, Intent intent) {
	//	mContext = context;
		mIntent = intent;

		String action = intent.getAction();
		if(action.equals(ACTION_SMS_RECEIVED)){
		  //  Log.i(DEBUG_TAG, "receiver starting");
			String address = "", str = "";
			SmsMessage[] msgs = getMessagesFromIntent(mIntent);
			if (msgs != null) {
				for (int i = 0; i < msgs.length; i++) {
					address = msgs[i].getOriginatingAddress();
					str += msgs[i].getMessageBody().toString();
					str += "\n";
				}
			}   
		  //  Log.i(DEBUG_TAG, "sms message from " + address);
			Intent replyWanted = new Intent(context, SMSreplier.class);
			replyWanted.putExtra("sms", str);
			replyWanted.putExtra("address", address);
			context.startService(replyWanted);
		  //  Log.i(DEBUG_TAG, "intent was sent for " + str);
		}
	}

	private SmsMessage[] getMessagesFromIntent(Intent intent) {
	  //  Log.i(DEBUG_TAG, "reading messages");
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}
}
