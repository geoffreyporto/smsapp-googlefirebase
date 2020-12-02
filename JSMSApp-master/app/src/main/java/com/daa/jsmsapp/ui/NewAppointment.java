package com.daa.jsmsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daa.jsmsapp.MainActivity;
import com.daa.jsmsapp.R;
import com.daa.jsmsapp.model.Appointment;
import com.daa.jsmsapp.utils.Fragmento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

//Librerias para manejo de Google Firebase
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class NewAppointment extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = NewAppointment.class.getSimpleName();
    public static final Integer DATA_EVENTS= 1; //app_eventos
    public static final Integer DATA_ERRORS= 2; //app_errors
    public static final Integer DATA_APPOINTMENT= 3; //app_appointment
    public static final Integer DATA_PATIENT= 4; //patient
    public static final Integer DATA_DOCTOR= 5; //doctor

    EditText edtNombre, edtNombreDoctor, edtHospital, edtEspecialidad; //edtDia
    Button btnSiguiente;
    CalendarView fechaCalendar;
    DatePicker edtDia;
    TimePicker edtHorario;
    String fechaCita;
    String tvHorario;
    int hour, minute;
    String am_pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Registrando la autenticacion de Firebase auuth
        mAuth = FirebaseAuth.getInstance();

        //Registrando en Firebase Firestore
        db = FirebaseFirestore.getInstance();

        //Button btnLogin = findViewById(R.id.btnLogin);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        //Campos
        edtNombre = findViewById(R.id.edtNombre);
        edtNombreDoctor = findViewById(R.id.edtNombreDoctor);
        edtHospital = findViewById(R.id.edtHospital);
        edtEspecialidad = findViewById(R.id.edtEspecialidad);
        String nombreDoctor = edtNombreDoctor.getText().toString();
        //tvDia = findViewById(R.id.tvDia);

        edtDia=(DatePicker)findViewById(R.id.edtDia);
        edtHorario=(TimePicker)findViewById(R.id.edtHorario);

        ((TimePicker) edtHorario).setIs24HourView(false);

        /*fechaCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String Date = dayOfMonth + "/" + (month + 1) + "/" + year;
                edtDia.setText(Date);
            }
        });*/

        edtDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvDia.setText(edtDia.getDayOfMonth()+"/"+ (edtDia.getMonth() + 1)+"/"+edtDia.getYear());
                fechaCita = edtDia.getDayOfMonth()+"/"+ (edtDia.getMonth() + 1)+"/"+edtDia.getYear();
                Log.d(TAG, "Fecha Cita: " + fechaCita);
                Toast.makeText(NewAppointment.this, "Fecha Cita: "+fechaCita, Toast.LENGTH_SHORT).show();
            }
        });


        tvHorario = String.valueOf(edtHorario.getHour()) +":"+String.valueOf(edtHorario.getMinute()) ;
        Log.d(TAG, "Horario Cita: " + tvHorario);
        //Toast.makeText(NewAppointment.this, "Horario Cita: "+tvHorario, Toast.LENGTH_SHORT).show();

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> appointmentMap = new HashMap<>();

                appointmentMap.put("id",randomUUID().toString());
                appointmentMap.put("nombre", edtNombre.getText().toString());
                appointmentMap.put("nombre_doctor",edtNombreDoctor.getText().toString());
                appointmentMap.put("hospital", edtHospital.getText().toString());
                appointmentMap.put("especialidad", edtEspecialidad.getText().toString());
                fechaCita = edtDia.getDayOfMonth()+"/"+ (edtDia.getMonth() + 1)+"/"+edtDia.getYear();
                Toast.makeText(NewAppointment.this, "Fecha Cita: "+fechaCita, Toast.LENGTH_SHORT).show();
                appointmentMap.put("fechaCita", fechaCita);
                appointmentMap.put("horario",  tvHorario);
                appointmentMap.put("fechahora",  Timestamp.now());

                Bundle param = new Bundle();
                param.putString("id", randomUUID().toString());
                param.putString("nombre", edtNombre.getText().toString());
                param.putString("nombre_doctor", edtNombreDoctor.getText().toString());
                param.putString("hospital", edtHospital.getText().toString());
                param.putString("especialidad", edtEspecialidad.getText().toString());
                param.putString("fechaCita", fechaCita);
                param.putString("horario",  tvHorario);
                param.putString ("fechahora",  Timestamp.now().toString());

                Appointment appointment = new Appointment(randomUUID(), null, null, null, null, null,  edtNombre.getText().toString(), nombreDoctor, null, 1, 10, null,null);

                saveData(db, DATA_APPOINTMENT,appointmentMap);

                //refactorizar para fragmentos
                /*Fragment fragment;
                Fragmento fragmento;
                FragmentManager fragmentManager;
                //Usando una funcion genérica
                fragment = new ConfirmationAppointment();
                fragmentManager = getSupportFragmentManager();
                fragmento = new Fragmento(fragment,fragmentManager);
                fragmento.cargarFragmento();*/


            }

        });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(NewAppointment.this, "onStart.", Toast.LENGTH_SHORT).show();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    // [END on_start_check_user]

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_new_appointment, container, false);

        //Campos
        edtNombre = v.findViewById(R.id.edtNombre);
        edtNombreDoctor = v.findViewById(R.id.edtNombreDoctor);
        edtHospital = v.findViewById(R.id.edtHospital);
        edtEspecialidad = v.findViewById(R.id.edtEspecialidad);
        edtDia = v.findViewById(R.id.edtDia);
        edtHorario = v.findViewById(R.id.edtHorario);
        String nombreDoctor = edtNombreDoctor.getText().toString();


        Bundle param = new Bundle();
        param.putString("nombre", edtNombre.getText().toString());
        param.putString("nombre_doctor", edtNombreDoctor.getText().toString());
        param.putString("hospital", edtHospital.getText().toString());
        param.putString("especialidad", edtEspecialidad.getText().toString());
        //param.putString("Dia", edtDia.getText().toString());
        //param.putString("horario",  edtDia.getText().toString());
        param.putString ("fechahora",  Timestamp.now().toString());


        //siguiente = findViewById(R.id.boton_siguiente);

        /*btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Refactorizar con fragmentos
                ir_aDestino(view, nombre_doctor.getText().toString(),
                        hospital.getText().toString(),
                        especialidad.getText().toString(),
                        dia.getText().toString(),
                        horario.getText().toString());
            }
        });*/

        return v;
    }

    public void ir_aDestino(View view, String nombreDoctor, String hospital, String especialidad, String dia, String horario){
        Bundle param = new Bundle();
        param.putString("Doctor", nombreDoctor);
        param.putString("Hospital", hospital);
        param.putString("Especialidad", especialidad);
        param.putString("Dia", dia);
        param.putString("Horario", horario);
        Navigation.findNavController(view).navigate(R.id.action_confirmar, param);
    }

    //guardar datos en Google Firebase firestore

    public void saveData( FirebaseFirestore db, Integer Datatype, Map<String, Object>  data ) {

        //appointments
        if (Datatype==3 && data != null) {
            db.collection("appointments")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Documento Appointments añadido con ID: " + documentReference.getId());
                            Toast.makeText(NewAppointment.this, "Cita fue creadoo con ID:"+documentReference.getId(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error al agregar documentoo Appointments", e);
                            Toast.makeText(NewAppointment.this, "Error en la creación de la cita.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    //Todas las operaciones de datos en Google Firebase firestore

    public void allData( FirebaseFirestore db, Integer Datatype, Map<String, Object>  data ) {

        //appointments
        if (db.getApp()!=null && Datatype==3 && data != null) {
            db.collection("appointments")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Documento Appointments añadido con ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error al agregar documentoo Appointments", e);
                        }
                    });

        //patients
        } else if (db.getApp()!=null && Datatype==4 && data != null) {
            db.collection("patients")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Documento Patients añadido con ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error al agregar documentoo Patients", e);
                        }
                    });

        } else if (db.getApp()!=null && Datatype==5 && data != null) {
        db.collection("doctors")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Documento Doctors añadido con ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error al agregar documentoo Doctors", e);
                    }
                });

        } else {
            Log.w(TAG, "Error al agregar documentoo");
            Toast.makeText(NewAppointment.this, "Datos inválidos.", Toast.LENGTH_SHORT).show();
        }

    }

}