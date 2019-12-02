package com.example.tictactoe.userInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.tictactoe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView etEmail, etPassword;
    private Button btnLogin;
    private Button btnRegistro;
    private ScrollView formLogin;
    private ProgressBar pbLogin;
    private FirebaseAuth firebaseAuth;
    String email, password;
    boolean tryLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        btnRegistro= findViewById(R.id.buttonRegistro1);
        formLogin = findViewById(R.id.formLogin);
        pbLogin = findViewById(R.id.progressBarLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        //changeloginFormVisibility(true);
        eventos();
    }

    private void eventos() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
               password = etPassword.getText().toString();

                if(email.isEmpty()){
                    etEmail.setError("El email es Obligatorio");
                } else if(password.isEmpty()){
                    etPassword.setError("La contrasela es obligatorio");
                }else {
                    // Realizamos el login
                     changeloginFormVisibility(false);
                     loginUser();
                }

            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(i);
                //finish();
            }
        });
    }

    private void loginUser() {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        tryLogin = true;
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        }else {
                            Log.w("tag", "singInError: ", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user!= null){
            // Almacenar la informacion del usuario en FireStore


            // Navegar hacia la siguiente pantalla de la aplicacion
            Intent i = new Intent(LoginActivity.this, FindGameActivity.class);
            startActivity(i);
        }else {
            changeloginFormVisibility(true);
            if(tryLogin){
                etPassword.setError("Nombre, Email y/o contraseña incorrecto");
                etPassword.requestFocus();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Comprobamos si previamente el usuario ya ha iniciado sesion en este dispositivo
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void changeloginFormVisibility(boolean showForm) {
        pbLogin.setVisibility(showForm ? View.GONE: View.VISIBLE);
        formLogin.setVisibility(showForm ? View.VISIBLE: View.GONE);
    }
}
