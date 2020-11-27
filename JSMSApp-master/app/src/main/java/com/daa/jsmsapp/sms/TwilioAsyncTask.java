package com.daa.jsmsapp.sms;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.daa.jsmsapp.MainActivity.ACCOUNT_SID;

public abstract class TwilioAsyncTask extends AsyncTask {

    /*Context context;
    ProgressDialog progressDialog;


    public TwilioAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    public String processInBackground() {

        final String ACCOUNT_SID = "AC6e67a409e87e40a18b0c45b8fcf93fe3";
        final String AUTH_TOKEN = "2703a5e3dc7212e4a7bc5653f4614f5b";
        final String URI_TWILIO_API ="https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/SMS/Messages";
        final String FROM_NUMBER = "+17126616182";
        final String TO_NUMBER = "+528114216460";
        final String MSG_SMS = "Mensaje enviado de Android";

        final String TAG = Sms.class.getSimpleName();

        //
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(
                "https://api.twilio.com/2010-04-01/Accounts/AC_yourACCOUNT_SID_9b/SMS/Messages");
        String base64EncodedCredentials = "Basic "
                + Base64.encodeToString(
                (ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(),
                Base64.NO_WRAP);

        httppost.setHeader("Authorization",
                base64EncodedCredentials);
        try {

            int randomPIN = (int) (Math.random() * 9000) + 1000;
            String randomVeriValue = "" + randomPIN;
            // these are for control in other anctivity used sharepreference
            //editorTwilio.putString("twilio_veri_no", randomVeriValue);
            //editorTwilio.commit();

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("From",
                    "+148******")); // what number they gave you
            nameValuePairs.add(new BasicNameValuePair("To",
                    "+90" + phoneNo)); // your phone or our customers
            nameValuePairs.add(new BasicNameValuePair("Body",
                    "Your verification number is : " + randomVeriValue));

            httppost.setEntity(new UrlEncodedFormEntity(
                    nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            System.out.println("Entity post is: "
                    + EntityUtils.toString(entity));
            // Util.showMessage(mParentAct, "Welcome");


        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }

        //
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        //progressDialog.dismiss();
    }


    @Override
    protected void onPreExecute() {

        progressDialog = ProgressDialog.show(context, "", " Wait for ");

    }


    @Override
    protected void onProgressUpdate(String... text) {
        // Things to be done while execution of long running operation is in
        // progress. For example updating ProgessDialog
    }

}*/

}
