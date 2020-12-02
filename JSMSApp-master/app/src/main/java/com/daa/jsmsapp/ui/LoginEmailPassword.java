package com.daa.jsmsapp.ui;

import android.net.Uri;
import android.os.Bundle;

import com.daa.jsmsapp.MainActivity;
import com.daa.jsmsapp.utils.Fragmento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daa.jsmsapp.R;

//Librerias de Firebase Auth
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorResolver;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class LoginEmailPassword extends AppCompatActivity  {

    private static final String TAG = "LoginEmailPassword";

    // [START declare_auth]
    private FirebaseAuth mAuth;

    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_loginemailpassword);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnNewAppointment = findViewById(R.id.btnNewAppointment);
        Button btnEnviarSMS = findViewById(R.id.btnEnviarSMS);

        EditText edtName, edtEmail, edtCelular, edtPassword, edtPassword2;
        TextView tvStatus;

        //Campos
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtCelular = findViewById(R.id.edtCelular);
        edtPassword = findViewById(R.id.edtPassword);
        tvStatus = findViewById(R.id.tvStatus);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvStatus.setText("");

                //Dispara el login con Google fiirebase authentication
                Login(edtEmail.getText().toString(), edtPassword.getText().toString());
                if (mAuth.getCurrentUser()!=null) {
                    tvStatus.setText("Logueado");
                }
            }

        });

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserRegister();
            }

        });

        btnNewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewAppointmentActivity();
            }

        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desloguear();
                if (mAuth.getCurrentUser()!=null) {
                    tvStatus.setText("Logueado");
                } else{
                    tvStatus.setText("desLogueado");
                }
            }

        });

        btnEnviarSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }

        });






    }

    private void Login(String email, String password) {

        Log.d(TAG, "signIn:" + email);

        /*if (!validateForm()) {
            return;
        }

        showProgressBar();*/

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginEmailPassword.this, "Authentication exitosa." , Toast.LENGTH_SHORT).show();

                            //updateUI(user);
                            openMainActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginEmailPassword.this, "Authentication falló." , Toast.LENGTH_SHORT).show();
                            //updateUI(null);

                            // [START_EXCLUDE]
                            //checkForMultiFactorFailure(task.getException());
                            // [END_EXCLUDE]
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mBinding.status.setText(R.string.auth_failed);
                        }
                        //hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]

    }
    private void desloguear() {
        mAuth.signOut();
        Toast.makeText(LoginEmailPassword.this, "Cuenta fue desconectada." , Toast.LENGTH_SHORT).show();
        //updateUI(null);
    }


    private void openMainActivity() {
        Toast.makeText(LoginEmailPassword.this, "Abriendo la home", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginEmailPassword.this, MainActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    private void openUserRegister() {
        Toast.makeText(LoginEmailPassword.this, "Abriendo nueva cuenta", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginEmailPassword.this, NewAccount.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private void openNewAppointmentActivity() {

        Toast.makeText(LoginEmailPassword.this, "Abriendo la home", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginEmailPassword.this, NewAppointment.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

   /* private void openUserRegister2() {
        UserRegister userRegister;
        Fragment fragment;
        Fragmento fragmento;
        FragmentManager fragmentManager;
        //Usando una funcion genérica
        fragment = new UserRegister();
        fragmentManager = getSupportFragmentManager();
        fragmento = new Fragmento(fragment,fragmentManager);
        fragmento.cargarFragmento();
    }*/


}