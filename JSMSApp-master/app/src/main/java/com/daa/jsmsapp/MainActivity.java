package com.daa.jsmsapp;

//Librerias estandares de Android
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//librerias legacy NO-Optimizadas de Android para SMS
import com.daa.jsmsapp.sms.Sms;

//Gestor de fragmentos
import com.daa.jsmsapp.utils.Fragmento;

//Usando HttPost para servicios http NO-OPTIMIZADO

//librerias para funcionalidad general
import java.util.HashMap;
import java.util.Map;


//Usando OkHttp para servicios http OPTIMIZADO


//Usando Retrofit para servicios http MÁS-OPTIMIZADO


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.perf.metrics.Trace;

// Clases de Google Firebase para Performance y Monitoreo
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_SEND_SMS =0 ;
    public static final String ACCOUNT_SID = "AC6e67a409e87e40a18b0c45b8fcf93fe3";
    public static final String AUTH_TOKEN = "f356b3b154abf723e97e3d9ab46a1f9b";
    public static final String FROM_NUMBER = "+17126616182"; //Numero virtual

    public static final String URI_TWILIO_API ="https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/SMS/Messages";
    public static final String TO_NUMBER = "+528114216460";
    public static final String MSG_SMS = "Mensaje enviado de Android";

    private static final String TAG = MainActivity.class.getSimpleName();

    EditText edtName, edtSenderNumber, edtReceiverNumber, edtMessage;
    TextView tvRecibido;
    Button btnSend;
    Sms smssend;

    @Override
    @AddTrace(name = "onCreate", enabled = true /* optional */)
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

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Campos
        edtName = findViewById(R.id.edtName);
        edtSenderNumber = findViewById(R.id.edtSenderNumber);
        edtReceiverNumber =findViewById(R.id.edtReceiverNumber);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        tvRecibido = findViewById(R.id.tvRecibido);


        //Cargando los botones
        /*btnUserRegister = findViewById(R.id.btnChat);
        btnContacto = findViewById(R.id.btnContacto);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Refatoring 3
                //Crear una clase excluisava para cargar fragmenttos, para que cumple totalmente con la (S) del principio de SOLID de buenas prácticas de desarrollo a nivel clases


                //Usando una funcion genérica
                fragment = new ChatsFragment();
                fragmentManager = getSupportFragmentManager();
                Fragmento fragmento = new Fragmento(fragment,fragmentManager);
                fragmento.cargarFragmento();

            }
        });*/


        ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, PackageManager.PERMISSION_GRANTED);
        Log.i(TAG, "********* Se solicitó permiso para mandar SMS");



        Trace codeTracking = FirebasePerformance.getInstance().newTrace("sendSmsRetrofit");
        // Update scenario.
        codeTracking.putAttribute("experimento ", "Twilio.Message.creator.create()");

        /***

        El crash se desribe abajo:
        2020-11-30 08:05:59.539 22807-22807/com.daa.jsmsapp E/AndroidRuntime: FATAL EXCEPTION: main
        Process: com.daa.jsmsapp, PID: 22807
        java.lang.NoSuchFieldError: No field INSTANCE of type Lorg/apache/http/conn/ssl/AllowAllHostnameVerifier; in class Lorg/apache/http/conn/ssl/AllowAllHostnameVerifier; or its superclasses (declaration of 'org.apache.http.conn.ssl.AllowAllHostnameVerifier' appears in /system/framework/framework.jar:classes2.dex)

         REQUISITOS: Instalaciónn del ADB Debug en su sistema:

         Paso 1: Descompacta el .zip mueve para dentro de un nueva un carpeta "android"

         sudo mkdir android
         sudo unzip eabcd8b4b7ab518c6af9c941af8494072f17ec4b.platform-tools_r30.0.5-darwin.zip
         sudo mv /Users/hack/Downloads/platform-tools /usr/local/android

         Paso 2: Configura tu variable de entorno para localizar y ejecutar la herramienta adb
            sudo nano /etc/profile
                export ADB_HOME="/usr/local/android/"
                export PATH="$PATH:$ADB_HOME/platform-tool"
            source /etc/profile
         Paso 2: sudo adb shell setprop log.tag.FirebaseCrashlytics DEBUG
         Paso 3: sudo adb logcat -s FirebaseCrashlytics


         References:
         https://developer.android.com/studio/releases/platform-tools

         */

        codeTracking.start();



        // Definiendo una key tipo string para la falla.
        FirebaseCrashlytics.getInstance().setCustomKey("nombre", "twilio.message.create");

        //  Definiendo una key tipo bool para la falla.
        FirebaseCrashlytics.getInstance().setCustomKey("inicio", true);

        //  Definiendo una key tipo entero para la falla.
        FirebaseCrashlytics.getInstance().setCustomKey("codigo", 1001);

        // Definiendo una key tipo entero long para la falla.
        /*FirebaseCrashlytics.getInstance().setCustomKey("int_key", 1L);

        //  Definiendo una key tipo float para la falla.
         FirebaseCrashlytics.getInstance().setCustomKey("float_key", 1.0f);

        //  Definiendo una key tipo string para la falla.
         FirebaseCrashlytics.getInstance().setCustomKey("double_key", 1.0);*/


        try {


            //Conecta a la BD de Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Creando el scenario de grabación de eventos.
            Trace codeTrackingfirebaseAgregarEventos = FirebasePerformance.getInstance().newTrace("fireBaseAgregarEventos");
            codeTrackingfirebaseAgregarEventos.putAttribute("experimento ", "db.collection.create()");
            codeTrackingfirebaseAgregarEventos.start();

            //Estructura de Datos para registro de evento de Login
            Map<String, Object> app_eventos = new HashMap<>();
            app_eventos.put("IDType", "1001"); //ID de Login
            app_eventos.put("UserID", "JSMSApp");
            app_eventos.put("Descripcion", "Evento de login");
            app_eventos.put("FechaHora",  Timestamp.now());

            db.collection("app_eventos")
                    .add(app_eventos)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Documento Eventos añadido con ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error al agregar documentoo", e);
                        }
                    });

            codeTrackingfirebaseAgregarEventos.stop();


            /*INI: Causa un crash a proposito en la App y lo registra en Google Firebase Crashlytics*/

            /*Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message.creator(
                    new PhoneNumber("+528114216460"), //tu cel
                    new PhoneNumber("+17126616182"), //virtual number
                    MSG_SMS).create();*/

            /*FIN: Causa un crash a proposito en la App*/

            //throw new RuntimeException("Forzar un Crash");


        } catch ( Exception e) {

            //DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

            //Estructura de Datos para registro de evento de error
            Map<String, Object> app_errors = new HashMap<>();
            app_errors.put("errorMessage", e.getMessage()); //ErrorMessage
            app_errors.put("causeMessage", e.getCause().getMessage().toString());
            app_errors.put("stackMessage", e.getStackTrace().toString());
            app_errors.put("classMessage", e.getClass().getName());
            app_errors.put("FechaHora", Timestamp.now());

            Log.i("Message Error:", e.getMessage());
            Log.i("Causa del Error:", e.getCause().getMessage().toString());
            Log.i("Stack trace del Error:", e.getStackTrace().toString());
            Log.i("Clase java del Error:", e.getClass().getName());

            /*CrashlyticsReport.Session.Event.Application.Execution.Exception ex = new CrashlyticsReport.Session.Event.Application.Execution.Exception();
            Log.i("Tipo del Error:", e.getType());
            Log.i("Razón del Error:", e.getReason());
            Log.i("Descripcion del Error:", e.toString());
            Log.i("Causa del Error:", e.getCausedBy().toString());*/
            //Report(e);
            //Throwable


            //Firebase Firestore
            //Nombre collecion: app_reports

            Trace codeTrackingfirebaseAgregarError = FirebasePerformance.getInstance().newTrace("fireBaseAgregarCrashes");
            // Creando el scenario de grabación de datos.
            codeTrackingfirebaseAgregarError.putAttribute("experimento ", "twilio.message.create()");
            codeTrackingfirebaseAgregarError.start();


            //Registrando el crash de la App
            FirebaseCrashlytics.getInstance().log("twilio.message.create"); //ReportID
            FirebaseCrashlytics.getInstance().setUserId("JSMSApp"); //UserID
            FirebaseCrashlytics.getInstance().recordException(e);

            //Conecta a la BD de Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("app_crashes")
                    .add(app_errors)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Documento Crashes añadido con ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error al agregar documentoo", e);
                        }
                    });

            codeTrackingfirebaseAgregarError.stop();


        }




        codeTracking.stop();

        /*References:
         https://firebase.google.com/docs/perf-mon/custom-code-traces?platform=android
         https://firebase.google.com/docs/crashlytics/customize-crash-reports?authuser=0&platform=android
         */

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