package com.daa.jsmsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daa.jsmsapp.sms.SMSReceiver;
import com.daa.jsmsapp.sms.Sms;
import com.daa.jsmsapp.sms.TwilioApi;


//Usando HttPost para servicios http
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
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


//Usando OkHttp para servicios http
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


//Usando Retrofit para servicios http
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

// Clases de Google Firebase para Performance y Monitoreo
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_SEND_SMS =0 ;
    public static final String ACCOUNT_SID = "AC6e67a409e87e40a18b0c45b8fcf93fe3";
    public static final String AUTH_TOKEN = "2703a5e3dc7212e4a7bc5653f4614f5b";
    public static final String URI_TWILIO_API ="https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/SMS/Messages";
    public static final String FROM_NUMBER = "+17126616182";
    public static final String TO_NUMBER = "+528114216460";
    public static final String MSG_SMS = "Mensaje enviado de Android";

    private static final String TAG = MainActivity.class.getSimpleName();

    EditText edtName, edtSenderNumber, edtReceiverNumber, edtMessage;
    TextView tvRecibido;
    Button btnSend;
    Sms smssend;

    @Override
    @AddTrace(name = "onCreateTrace", enabled = true /* optional */)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy( new
                    StrictMode.ThreadPolicy.Builder().permitAll().build() );
        }

        //Campos
        edtName = findViewById(R.id.edtName);
        edtSenderNumber = findViewById(R.id.edtSenderNumber);
        edtReceiverNumber =findViewById(R.id.edtReceiverNumber);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        tvRecibido = findViewById(R.id.tvRecibido);

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, PackageManager.PERMISSION_GRANTED);
        Log.i(TAG, "********* Se solicitó permiso para mandar SMS");
    }

    //Método para checar permisos
    public boolean checkPersmission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public String writeMessage (String name, String senderPhone, String message){
        return ("Hola, soy "+ name +"\n"+ " " + message + "\n"+ "De: "
        +senderPhone);
    }

    public void onSend(View view){
        String phone = "+52"+edtReceiverNumber.getText().toString();
        String msg = writeMessage(edtName.getText().toString(), edtSenderNumber.getText().toString(), edtMessage.getText().toString());


        //Checando si tengo el permiso en e telefono para enviar mensajes
        try {
            if(checkPersmission(Manifest.permission.SEND_SMS) &&
                    checkPersmission(Manifest.permission.READ_PHONE_STATE)){


                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.SEND_SMS)) {
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.SEND_SMS},
                                PERMISSIONS_REQUEST_SEND_SMS);
                    }
                }

                //Definiendo el objeto de SMS
                smssend = new Sms(edtName.getText().toString(), edtSenderNumber.getText().toString(), edtReceiverNumber.getText().toString(), msg);


                /****
                SMS CASO 1: Envio de SMS por meio de Applicacion nativo
                 ****/
                PendingIntent enviadoPI, entreguePI;

                enviadoPI = PendingIntent.getBroadcast(this, 0,
                        new Intent("SMS SENT"), 0);
                entreguePI = PendingIntent.getBroadcast(this, 0,
                        new Intent("SMS DELIVERED"), 0);


                //PendingIntent enviadoPI = PendingIntent.getActivity(this, 0,
                //        new Intent(this, MainActivity.class), 0);

                IntentFilter confirmSentIntentFilter = new IntentFilter("SMS SENT");
                IntentFilter confirmDeliveryIntentFilter = new IntentFilter("SMS DELIVERED");
                registerReceiver(confirmSentBR, confirmSentIntentFilter);
                registerReceiver(confirmDeliveryBR, confirmDeliveryIntentFilter);


                /*
                try {

                    final SmsManager sms = SmsManager.getDefault();

                    if (msg.length() > 160) {
                        final ArrayList<String> messageParts = sms.divideMessage(msg);
                        sms.sendMultipartTextMessage(phone, null, messageParts, null, null);
                    } else {
                        //sms.sendTextMessage(phone, null, msg, null, null);
                        sms.sendTextMessage(phone, null, msg, sentPI, deliveredPI);
                    }
                } catch (SecurityException e) {
                    Log.i(TAG, "********* NO se pudo enviar SMS "+ e);
                    // warn user? disable feature?
                }*/


                //SMS via aplicación por default
                /*try {
                    Intent smsApp1 = smssend.sendSMSApp1(phone,msg);
                    startActivity(smsApp1);
                    Log.i("Felicidades!  Se envió de SMS1 exitosamente.", "startActivity(smsApp1)");
                } catch (android.content.ActivityNotFoundException ex) {
                    Log.i("Error al enviar de SMS:", "smsApp1::android.content.ActivityNotFoundException");
                    Toast.makeText(MainActivity.this, "Por favor, intente nuevamente.", Toast.LENGTH_SHORT).show();
                }*/

                //SMS via aplicación seleccionadopor el el usuario
                /*try {
                    Intent smsApp2 = smssend.sendSMSApp2(phone,msg);
                    startActivity(smsApp2);
                    Log.i("Felicidades!  Se envió de SMS2 exitosamente.", "startActivity(smsApp2)");
                } catch (android.content.ActivityNotFoundException ex) {
                    Log.i("Error al enviar de SMS:", "smsApp2::android.content.ActivityNotFoundException");
                    Toast.makeText(MainActivity.this, "Por favor, intente nuevamente.", Toast.LENGTH_SHORT).show();
                }*/

                //Objeto que receberá SMS entrante/enviado por otra App
                /*IntentFilter incomingSmsIntentFilter =
                        new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                SMSReceiver smsReceiver = new SMSReceiver(tvRecibido);
                registerReceiver(smsReceiver,incomingSmsIntentFilter);*/

                //SMS: Envio de SMS usando el manager - no recomendado usar en producción por su simpliciad y falta de control.
                //https://developer.android.com/reference/android/telephony/SmsManager



                 /*****
                    SMS CASO 2: Envio por meio de SMS WhatsApp
                 *****/

                //startActivity(smssend.sendMsgWhatsApp(smssend));



                /*Intent whatsApp = smssend.sendMsgWhatsApp(msg);

                if (whatsApp.resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(this, "Por favor, instale su Whatsapp primero.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(whatsApp);
                }*/


                /*****
                 SMS CASO 3: Envio SMS por meio de HttPost
                 *****/

                //smssend.sendSmsHttpPost(smssend);


                /*****
                 SMS CASO 4: Envio SMS por meio de OKHttp
                 *****/

                //smssend.sendSmsOkHttp(smssend);


                /*****
                SMS CASO 5: Envio SMS optimizado por meio de Twilio via Retrofit2 + OkHttp
                 *****/
                smssend.sendSmsRetrofit(smssend);

                //SMS de verificación: Código de verificación enviado a un celular que automaricamente lo registrar en una App.
                //https://www.twilio.com/docs/verify/tutorials/android-sdk-integration-sample-backend


                Log.i(TAG, "********* Se mandó el mensaje de telefono "+phone);
            } else {
                Log.i(TAG, "********* NO se tenían permisos");
            }
        } catch (Exception e){
            Log.i(TAG, "******* Excepción: " + e.getMessage());
        }
    }


    /*
    Usando el metodo OkHttp
    doc: https://square.github.io/okhttp/
     */
    private void sendSmsOkHttp(){
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/SMS/Messages";
        String base64EncodedCredentials = "Basic " + Base64.encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP);
        RequestBody body = new FormBody.Builder()
                .add("From", FROM_NUMBER)
                .add("To", TO_NUMBER)
                .add("Body", MSG_SMS)
                .build();
        Request request = new Request.Builder()
                .url(url) .post(body)
                .header("Authorization", base64EncodedCredentials)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.d(TAG, "sendSms: "+ response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /* Retrofit:
    Doc: https://square.github.io/retrofit/
    Github: https://github.com/square/retrofit
     */

    private void sendSmsRetrofit() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        //OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        String base64EncodedCredentials = "Basic " + Base64.encodeToString( (ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP );
        Map<String, String> data = new HashMap<>();
        data.put("From", FROM_NUMBER);
        data.put("To", TO_NUMBER);
        data.put("Body", MSG_SMS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twilio.com/2010-04-01/")
                //.baseUrl("https://api.twilio.com/2010-04-01/Accounts/")
                //.client(okHttpClient)
                .build();

        TwilioApi api = retrofit.create(TwilioApi.class);
        api.sendMessage(ACCOUNT_SID, base64EncodedCredentials, data).enqueue(new Callback<ResponseBody>() {
            /**
             * Invoked for a received HTTP response.
             *
             * <p>Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful())
                    Log.d("TAG", "onResponse->success");
                else Log.d("TAG", "onResponse->failure");
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            //Manejo de respuestas de error
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable error) {

                if (error instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    Log.d("TAG", "onFailure: "+call.toString()+ "msg: " + error.getMessage().toString());
                    Toast.makeText(MainActivity.this, "Response" , Toast.LENGTH_SHORT).show();
                }
                else if (error instanceof IOException)
                {
                    // "Timeout";
                    Log.d("TAG", "onFailure: "+call.toString()+ "msg: " + error.getMessage().toString());
                }
                else
                {
                    //Call was cancelled by user
                    if(call.isCanceled())
                    {
                        System.out.println("Call fue cancelada brutalmente");
                    }
                    else
                    {
                        //Generic error handling
                        System.out.println("Error de Red :: " + error.getLocalizedMessage());
                    }
                }

                Log.d("TAG", "onFailure: "+call.toString()+ "msg: " + error.getMessage().toString());

            }

        });


    }

    //Usando el metodo HttpPost
    public void sendSmsHttpPost() {
        //Funcionalidad para envío de datos via Http
        HttpClient httpclient = new DefaultHttpClient();

        Log.i(TAG, "httpclient: " +URI_TWILIO_API);
        HttpPost httppost = new HttpPost(URI_TWILIO_API);
        Log.i(TAG, "httppost");
        String base64EncodedCredentials = "Basic "
                + Base64.encodeToString(
                (ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(),
                Base64.NO_WRAP);
        Log.i(TAG, "base64EncodedCredentials");
        httppost.setHeader("Authorization",
                base64EncodedCredentials);
        Log.i(TAG, "setHeader");
        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("From",
                    FROM_NUMBER));
            nameValuePairs.add(new BasicNameValuePair("To",
                    TO_NUMBER));
            nameValuePairs.add(new BasicNameValuePair("Body",
                    MSG_SMS));
            Log.i(TAG, "parametros");
            httppost.setEntity(new UrlEncodedFormEntity(
                    nameValuePairs));

            Log.i(TAG, "setEntity");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            Log.i(TAG, "response");
            HttpEntity entity = response.getEntity();

            System.out.println("El mensaje fue enviado fue: " + EntityUtils.toString(entity));


        } catch (ClientProtocolException e) {
            Log.i(TAG, "ClientProtocolException: "+e.toString());

        } catch (IOException e) {
            Log.i(TAG, "IOException: "+e.toString());
        }
    }

    //Checando si el telefono tiene el permiso de SMS activado
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(FROM_NUMBER, null, MSG_SMS, null, null);
                    Toast.makeText(getApplicationContext(), "Permiso de envio de SMS ACTIVADO.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Permiso de envio de SMS DESACTIVDADO.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }


    //Los escuchadores de broadcastreceiver del sms para Envio
    BroadcastReceiver confirmSentBR = new BroadcastReceiver( ) {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(),
                            "Felicidades! SMS enviado exitosamentte.", Toast.LENGTH_SHORT).show();
                    break;

                default: //all other codes are error
                    Toast.makeText(getBaseContext(),
                            "Error: SMS no se pudo enviar..", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    //Los escuchadores de broadcastreceiver del sms para Recepción
    BroadcastReceiver confirmDeliveryBR = new BroadcastReceiver( ) {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(),
                            "Felicidades! SMS fue entregue exitosamente", Toast.LENGTH_SHORT).show();
                    break;

                default: //all other codes are error
                    Toast.makeText(getBaseContext(),
                            "Error: SMS no pudo ser entregue.",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

        }

    };

}