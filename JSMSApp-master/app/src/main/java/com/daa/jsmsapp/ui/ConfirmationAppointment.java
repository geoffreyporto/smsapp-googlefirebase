package com.daa.jsmsapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.daa.jsmsapp.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.Timestamp;

public class ConfirmationAppointment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NOMBRE = "Nombre";
    private static final String ARG_DOCTOR = "Doctor";
    private static final String ARG_HOSPITAL = "Hospital";
    private static final String ARG_ESPECIALIDAD = "Especialidad";
    private static final String ARG_DIA = "Dia";
    private static final String ARG_HORARIO = "Horario";

    // TODO: Rename and change types of parameters
    private String argNombre, argDoctor, argHopsital, argEspecialidad, argDia, argHorario;

    private TextView resultado_doctor, resultado_hospital, resultado_especialidad, resultado_dia, resultado_horario;
    private Button confirmar;

    private static final String TAG = "ConfirmationAppointment: ";

    public ConfirmationAppointment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_confirmation_appointment);

        if (getArguments() != null) {
            argNombre = getArguments().getString(ARG_NOMBRE);
            argDoctor = getArguments().getString(ARG_DOCTOR);
            argHopsital = getArguments().getString(ARG_HOSPITAL);
            argEspecialidad = getArguments().getString(ARG_ESPECIALIDAD);
            argDia = getArguments().getString(ARG_DIA);
            argHorario = getArguments().getString(ARG_HORARIO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_confirmation_appointment, container, false);

        resultado_doctor  = v.findViewById(R.id.result_nombre_doctor);
        resultado_hospital  = v.findViewById(R.id.result_hospital);
        resultado_especialidad = v.findViewById(R.id.result_especialidad);
        resultado_dia = v.findViewById(R.id.result_dia);
        resultado_horario = v.findViewById(R.id.result_horario);

        resultado_doctor.setText(argDoctor);
        resultado_hospital.setText(argHopsital);
        resultado_especialidad.setText(argEspecialidad);
        resultado_dia.setText(argDia);
        resultado_horario.setText(argHorario);

        confirmar = v.findViewById(R.id.boton_confirmar);

        /*confirmar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_regresar_inicio);
            }
        });*/

        return v;
    }
}