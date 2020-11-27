package com.daa.jsmsapp.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;

public class SMSReceiver extends BroadcastReceiver {

    TextView msgTView;

    public SMSReceiver(TextView tv){
        super();
        msgTView = tv; //TextView enviado desde MainActivity
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String text = "Received msg: ";

        if (bundle != null) { //we have something to pull out

            String format = bundle.getString("format");

            Object[] pdus = (Object[]) bundle.get("pdus");

            if (pdus != null) {
                msgs = new SmsMessage[pdus.length];

                for (int i = 0; i < msgs.length; i++) {

                    // Check the Android version.
                    boolean isVersionM =
                            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
                    // Check Android version and use appropriate createFromPdu.
                    if (isVersionM) {
                        // If Android version M or newer:
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        // If Android version L or older:
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }

                    text += "From " + msgs[i].getOriginatingAddress() +
                            " : " +
                            msgs[i].getMessageBody().toString() +
                            "\n";
                }

            }


        }

        msgTView.setText(msgTView.getText().toString() + "\n" + text);

    }
}