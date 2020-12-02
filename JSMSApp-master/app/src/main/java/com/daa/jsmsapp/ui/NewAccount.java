package com.daa.jsmsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daa.jsmsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

//Librerias para manejo de Google Firebase
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class NewAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = NewAccount.class.getSimpleName();
    EditText edtName, edtEmail, edtCelular, edtPassword, edtPassword2;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        btnSave = findViewById(R.id.btnSave);

        //Campos
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtCelular = findViewById(R.id.edtCelular);
        edtPassword = findViewById(R.id.edtPassword);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(edtEmail.getText().toString(), edtPassword.getText().toString());
            }

        });


    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {

        Log.d(TAG, "createAccount:" + email);
        /*if (!validateForm()) {
            return;
        }

        showProgressBar();*/

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(NewAccount.this, "Felicideas! La cuenta fue creada exitosamente.", Toast.LENGTH_SHORT).show();

                            //updateUI(user);
                            enviarEmailVerificacion();
                            Toast.makeText(NewAccount.this, "Checa tu email para verificar tu cuenta.", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:falló", task.getException());

                            Toast.makeText(NewAccount.this, "Error en la creación de la cuenta.", Toast.LENGTH_SHORT).show();

                            //updateUI(null);
                        }


                    }
                });

    }


    private void enviarEmailVerificacion() {
        // Disable button
        //mBinding.verifyEmailButton.setEnabled(false);

        // Enviandp verificacion de email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        //mBinding.verifyEmailButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(NewAccount.this,
                                    "Verificacion de email enviado para " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(NewAccount.this,
                                    "Falla al enviar la verificación de email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }



}