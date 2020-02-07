package com.rrcc.ubifarm01;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class Identificarse extends Activity {

    //declaramnos las variables
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    //declaramos los botones
    private Button btnIniciarSesion;
    private Button btnRegistrarse;
    //progress
    private ProgressBar loginProgress;

    //declarar Firebase atenticacion
    private FirebaseAuth mAuth;

    //seleccio de rol
    String rol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificarse);

        textInputEmail = findViewById(R.id.identificarseEmail);
        textInputPassword = findViewById(R.id.identificarseContraseña);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnIdenRegistrarse);
        loginProgress = findViewById(R.id.ideProgressBar);

        loginProgress.setVisibility(View.INVISIBLE);
        //inicializo firebase autentificacion
        mAuth = FirebaseAuth.getInstance();
        //datos del anterior activity seleccionar roles
        getIncomingIntent();

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                btnIniciarSesion.setVisibility(View.INVISIBLE);
                btnRegistrarse.setVisibility(View.INVISIBLE);

                final String mail = textInputEmail.getEditText().getText().toString();
                final String password = textInputPassword.getEditText().getText().toString();

                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage("Porfavor, llene todos los campos");
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnIniciarSesion.setVisibility(View.VISIBLE);
                    btnRegistrarse.setVisibility(View.VISIBLE);
                    return;
                }
                else
                {
                    iniciarsesion(mail,password);
                }
            }
        });
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol.equals("Farmacia")){
                    Intent intent = new Intent(Identificarse.this, RegistraFarmaciaActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Identificarse.this, Registrarse.class);
                    startActivity(intent);
                }
            }
        });
    }
    //opcion de roles
    private void getIncomingIntent() {
        if(getIntent().hasExtra("invitado")) {
            rol = getIntent().getStringExtra("invitado");
        }
        if(getIntent().hasExtra("usuario")) {
            rol = getIntent().getStringExtra("usuario");
            btnRegistrarse.setVisibility(View.VISIBLE);
        }
        if(getIntent().hasExtra("Farmacia")) {
            rol = getIntent().getStringExtra("Farmacia");
            btnRegistrarse.setVisibility(View.VISIBLE);
        }
        //showMessage("rol1: "+rol);
    }
    //metodo para mostrar mesajes (Toast)
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            //usuario esta conectado redireccionamos al activity del inicio
            cargarMain();
        }
    }
    private void iniciarsesion(String mail, String password) {
        //Identificamos al usuario con firebase autenticacion
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnIniciarSesion.setVisibility(View.VISIBLE);
                    btnRegistrarse.setVisibility(View.VISIBLE);
                    cargarMain();
                    Toast.makeText(getApplicationContext(),"Usuario autentificado con exito",Toast.LENGTH_SHORT).show();
                }
                else {
                    //showMessage(task.getException().getMessage());
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    switch (errorCode) {
                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(Identificarse.this, "La dirección de correo electrónico está mal escrita", Toast.LENGTH_LONG).show();
                            textInputEmail.setError("The email address is badly formatted.");
                            textInputEmail.requestFocus();
                            break;

                        case "ERROR_WRONG_PASSWORD":
                            Toast.makeText(Identificarse.this, "La contraseña no es válida o el usuario no tiene una contraseña.", Toast.LENGTH_LONG).show();
                            textInputPassword.setError("password is incorrect ");
                            textInputPassword.requestFocus();
                            textInputPassword.setHint("");
                            break;
                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            Toast.makeText(Identificarse.this, "El correo electronico ya esta en uso por otro usuario", Toast.LENGTH_LONG).show();
                            textInputEmail.setError("The email address is already in use by another account.");
                            textInputEmail.requestFocus();
                            break;
                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(Identificarse.this, "No hay registro del usuario. El usuario puedo haber sido eliminado.", Toast.LENGTH_LONG).show();
                            break;
                    }
                    btnIniciarSesion.setVisibility(View.VISIBLE);
                    btnRegistrarse.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    private void cargarMain() {
        Intent intent= new Intent(Identificarse.this, MainActivity.class);
        if(rol.equals("usuario")){
            String usuario="usuario";
            intent.putExtra("usuario",usuario);
        }if(rol.equals("Farmacia")){
            String usuario="Farmacia";
            intent.putExtra("Farmacia",usuario);
        }
        startActivity(intent);
        Toast.makeText(this, "se encuentra identificado", Toast.LENGTH_LONG).show();
        finish();
    }
}
