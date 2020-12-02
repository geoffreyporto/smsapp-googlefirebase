package com.daa.jsmsapp.sms;

import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.daa.jsmsapp.BuildConfig;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.gson.Gson;

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
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Sms {

    String nombre;
    String telOrigen;
    String telDestino;
    String msg;

    public static final String ACCOUNT_SID = "AC6e67a409e87e40a18b0c45b8fcf93fe3";
    public static final String AUTH_TOKEN = "f356b3b154abf723e97e3d9ab46a1f9b";
    public static final String FROM_NUMBER = "+17126616182"; //Numero virtual
    public static final String URI_TWILIO_API ="https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/SMS/Messages";
    public static final String TO_NUMBER = "+528114216460";
    public static final String MSG_SMS = "Mensaje enviado de Android";

    public static final Integer DATA_EVENTS= 1; //app_eventos
    public static final Integer DATA_ERRORS= 2; //app_errors
    public static final Integer DATA_APPOINTMENT= 3; //app_appointment
    public static final Integer DATA_PATIENT= 4; //patient
    public static final Integer DATA_DOCTOR= 5; //doctor


    private static final String TAG = Sms.class.getSimpleName();

    //1. Solución más optimo
    public  Integer getCode(){

        try {
            Log.d(TAG, " ******** Evento getCode() *******");
            return (100);
        } catch (Exception e){
            Log.i(TAG, "******** Evento Error: "+e.getMessage());
            return Integer.valueOf(0);
        }
    }

    //Contructor con los parametros iniciales
    public Sms(String nombre, String telOrigen, String telDestino, String msg) {
        this.nombre = nombre;
        this.telOrigen = "+1"+telOrigen;
        this.telDestino = "+52"+telDestino;
        this.msg = msg;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelOrigen() {
        return telOrigen;
    }

    public void setTelOrigen(String telOrigen) {
        this.telOrigen = telOrigen;
    }

    public String getTelDestino() {
        return telDestino;
    }

    public void setTelDestino(String telDestino) {
        this.telDestino = telDestino;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    /*
   Usando el la libreria de OkHttp
   doc: https://square.github.io/okhttp/
    */
    public void sendSmsOkHttp(Sms sms){
        final String METODO = "\n"+"via OkHttp";

        OkHttpClient client = new OkHttpClient();
        String url = "https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/SMS/Messages";
        String base64EncodedCredentials = "Basic " + Base64.encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP);
        RequestBody body = new FormBody.Builder()
                .add("From", sms.telOrigen)
                .add("To", sms.telDestino)
                .add("Body", sms.msg+METODO)
                .build();
        Request request = new Request.Builder()
                .url(url) .post(body)
                .header("Authorization", base64EncodedCredentials)
                .build();
        Log.d("TAG", "Enviando de: "+sms.telOrigen);
        Log.d("TAG", "Para: "+sms.telDestino);
        Log.d("TAG", "Mensaje: "+sms.msg+METODO);

        try {
            Response response = client.newCall(request).execute();
            Log.d(TAG, "sendSms: "+ response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Usando la libreria de Retrofit (MÁS OPTIMIZADO Y RECOMENDADO)
    Doc: https://square.github.io/retrofit/
    Github: https://github.com/square/retrofit
     */

    public void sendSmsRetrofit(Sms sms) {

        Trace codeTracking = FirebasePerformance.getInstance().newTrace("sendSmsRetrofit");
        // Update scenario.
        codeTracking.putAttribute("experimento ", "sendSmsRetrofit");

        codeTracking.start();


        // Leeyendo scenario.
        String experimentValue = codeTracking.getAttribute("experimento");

        // Eliminando scenario.
        //codeTracking.removeAttribute("experimento");

        // Leyendo atributo.
        Map<String, String> traceAttributes = codeTracking.getAttributes();


        // Define la estructura de mensajes via interprette de JSONs llamado gson
        Gson gson = new Gson();
        final String METODO = "\n"+"via Retrofit+OKHttp Optimizado";

        //Creando un interceptor de logs para monitoreo
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG){
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }else{
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        //OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Log.d("TAG", "Enviando de: "+sms.telOrigen);
        Log.d("TAG", "Para: "+sms.telDestino);
        Log.d("TAG", "Mensaje: "+sms.msg+METODO);


        String base64EncodedCredentials = "Basic " + Base64.encodeToString( (ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP );
        Map<String, String> data = new HashMap<>();
        data.put("From", sms.telOrigen);
        data.put("To", sms.telDestino);
        data.put("Body", sms.msg+METODO);


        //Sin el interceptor de logs
        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twilio.com/2010-04-01/")
                .build();*/


        //Con el interceptor de logs
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twilio.com/2010-04-01/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //nuevo


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
                if (response.isSuccessful()) {

                    Log.d("TAG", "onResponse->success");

                    //Registrando en Firestore el envio de SMS
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // Creando el scenario de grabación de eventos.
                    Trace codeTrackingfirebaseAgregarEventos = FirebasePerformance.getInstance().newTrace("fireBaseAgregarEventos");
                    codeTrackingfirebaseAgregarEventos.putAttribute("experimento ", "db.collection.create()");
                    codeTrackingfirebaseAgregarEventos.start();

                    Map<String, Object> app_eventos = new HashMap<>();
                    app_eventos.put("IDType", "1002"); //ID de envio SMS
                    app_eventos.put("Status", "Exitoso"); //Exitoso
                    app_eventos.put("UserID", "JSMSApp");
                    app_eventos.put("Descripcion", "Evento de envio SMS");
                    app_eventos.put("FechaHora",  Timestamp.now());

                    //guardando eventos en Google Fiebase Firestore
                    saveEvents(db,DATA_EVENTS,app_eventos);

                    //parando de monitorear eventos de performance
                    codeTrackingfirebaseAgregarEventos.stop();

                } else {
                    Log.d("TAG", "onResponse->failure");

                    //Registrando en Firestore el envio de SMS
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // Creando el scenario de grabación de eventos.
                    Trace codeTrackingfirebaseAgregarEventos = FirebasePerformance.getInstance().newTrace("fireBaseAgregarEventos");
                    codeTrackingfirebaseAgregarEventos.putAttribute("experimento ", "db.collection.create()");
                    codeTrackingfirebaseAgregarEventos.start();

                    Map<String, Object> app_errors = new HashMap<>();
                    app_errors.put("IDType", "1002"); //ID de envio SMS
                    app_errors.put("Status", "Falló"); //Fallo
                    app_errors.put("UserID", "JSMSApp");
                    app_errors.put("Descripcion", "Evento de envio SMS");
                    app_errors.put("FechaHora",  Timestamp.now());

                    //guardando eventos en Google Fiebase Firestore
                    saveEvents(db,DATA_ERRORS,app_errors);

                    //parando de monitorear eventos de performance
                    codeTrackingfirebaseAgregarEventos.stop();

                }
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

                    //Registrando en Firestore el envio de SMS
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // Creando el scenario de grabación de eventos.
                    Trace codeTrackingfirebaseAgregarEventos = FirebasePerformance.getInstance().newTrace("fireBaseAgregarEventos");
                    codeTrackingfirebaseAgregarEventos.putAttribute("experimento ", "db.collection.create()");
                    codeTrackingfirebaseAgregarEventos.start();

                    Map<String, Object> app_errors = new HashMap<>();
                    app_errors.put("IDType", "1002"); //ID de envio SMS
                    app_errors.put("Status", "Falló"); //Fallo
                    app_errors.put("UserID", "JSMSApp");
                    app_errors.put("Descripcion", call.toString() +"::"+error.getMessage().toString() );
                    app_errors.put("FechaHora",  Timestamp.now());

                    //guardando eventos en Google Fiebase Firestore
                    saveEvents(db,DATA_ERRORS,app_errors);

                    //parando de monitorear eventos de performance
                    codeTrackingfirebaseAgregarEventos.stop();

                }
                else if (error instanceof IOException)
                {
                    // "Timeout";
                    Log.d("TAG", "onFailure: "+call.toString()+ "msg: " + error.getMessage().toString());
                    //Registrando en Firestore el envio de SMS
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // Creando el scenario de grabación de eventos.
                    Trace codeTrackingfirebaseAgregarEventos = FirebasePerformance.getInstance().newTrace("fireBaseAgregarEventos");
                    codeTrackingfirebaseAgregarEventos.putAttribute("experimento ", "db.collection.create()");
                    codeTrackingfirebaseAgregarEventos.start();

                    Map<String, Object> app_errors = new HashMap<>();
                    app_errors.put("IDType", "1002"); //ID de envio SMS
                    app_errors.put("Status", "Falló"); //Fallo
                    app_errors.put("UserID", "JSMSApp");
                    app_errors.put("Descripcion", call.toString() +"::"+error.getMessage().toString() );
                    app_errors.put("FechaHora",  Timestamp.now());

                    //guardando eventos en Google Fiebase Firestore
                    saveEvents(db,DATA_ERRORS,app_errors);

                    //parando de monitorear eventos de performance
                    codeTrackingfirebaseAgregarEventos.stop();

                }
                else
                {
                    //Llamada fue cancelado por el usuario
                    if(call.isCanceled())
                    {
                        System.out.println("Call fue cancelada brutalmente");
                    }
                    else
                    {
                        //Generic error handling
                        System.out.println("Error de Red :: " + error.getLocalizedMessage());
                        //Registrando en Firestore el envio de SMS
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        // Creando el scenario de grabación de eventos.
                        Trace codeTrackingfirebaseAgregarEventos = FirebasePerformance.getInstance().newTrace("fireBaseAgregarEventos");
                        codeTrackingfirebaseAgregarEventos.putAttribute("experimento ", "db.collection.create()");
                        codeTrackingfirebaseAgregarEventos.start();

                        Map<String, Object> app_errors = new HashMap<>();
                        app_errors.put("IDType", "1002"); //ID de envio SMS
                        app_errors.put("Status", "Falló"); //Fallo
                        app_errors.put("UserID", "JSMSApp");
                        app_errors.put("Descripcion", error.getLocalizedMessage() );
                        app_errors.put("FechaHora",  Timestamp.now());

                        //guardando eventos en Google Fiebase Firestore
                        saveEvents(db,DATA_ERRORS,app_errors);

                        //parando de monitorear eventos de performance
                        codeTrackingfirebaseAgregarEventos.stop();
                    }
                }

                Log.d("TAG", "onFailure: "+call.toString()+ "msg: " + error.getMessage().toString());

            }

        });


        codeTracking.stop();


    }

    //guardar datos en Google Firebase firestore

    public void saveEvents( FirebaseFirestore db, Integer Datatype, Map<String, Object>  data ) {
        if (Datatype==1 && data != null) {
            db.collection("app_eventos")
                    .add(data)
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

        } else if (Datatype==2 && data != null) {
            db.collection("app_errors")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Documento Errors añadido con ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error al agregar documentoo", e);
                        }
                    });

        }
    }

    //Envio por medio de WhatsApp
    public Intent sendMsgWhatsApp(Sms sms)
    {
        final String METODO = "\n"+"via WhatsApp";
        // Creando nuevo intento
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");

        // Definiendo el mensaje
        intent.putExtra(Intent.EXTRA_TEXT, sms.msg+METODO);

        // Checando si está instalado Whatsapp
        /*if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "Por favor instale whatsapp primero.", Toast.LENGTH_SHORT).show();
            return;
        }*/

        // iniciando Whatsapp
        return intent;
    }

    //Envio de SMS directo via servicio de mensajeria por default
    public Intent sendSMSApp1(String telDestino, String msg) {
        final String METODO = "\n"+"via SMSApp1";
        //create un intento implicito para ACTION_SENDTO, que iniciará actividad para la intent: esto iniciará la aplicación de mensajería predeterminada
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:+52"+telDestino));
        intent.putExtra("sms_body", msg+METODO);
        return intent;
    }

    //Envio de SMS selecionado en el menu de servicios de mensajeria: Facebook Messenger, SMS Nativo, signal, etc..
    public Intent sendSMSApp2(String telDestino, String msg) {
        final String METODO = "\n"+"via SMSApp2";
        //Elegir de que forma puedes enviar: Facebook Messenger, SMS Nativo, signal, etc..
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("smsto:"));
        //intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("address"  , new String ("+52"+telDestino));
        intent.putExtra("sms_body"  , msg+METODO);
        return intent;
    }

    //Usando el metodo HttpPost
    public void sendSmsHttpPost(Sms sms) {
        //Funcionalidad para envío de datos via Http
        HttpClient httpclient = new DefaultHttpClient();
        final String METODO = "\n"+"via HttpPost";

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

        Log.d("TAG", "Enviando de: "+sms.telOrigen);
        Log.d("TAG", "Para: "+sms.telDestino);
        Log.d("TAG", "Mensaje: "+sms.msg+METODO);

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("From",
                    sms.telOrigen));
            nameValuePairs.add(new BasicNameValuePair("To",
                    sms.telDestino));
            nameValuePairs.add(new BasicNameValuePair("Body",
                    sms.msg+METODO));
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


}