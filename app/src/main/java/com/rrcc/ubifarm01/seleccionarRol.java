package com.rrcc.ubifarm01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class seleccionarRol extends Activity {

    String usuario;
    //declaramos los botones
    private Button btnInvitado;
    private Button btnUsuario;
    private Button btnAdministrador;

    //declarar Firebase atenticacion
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_rol);

        btnInvitado = findViewById(R.id.btninvitado);
        btnUsuario = findViewById(R.id.btnUsuario);
        btnAdministrador = findViewById(R.id.btnAdministrador);
        mAuth = FirebaseAuth.getInstance();
        //cada boton realizara una accion
        btnInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword("ubifarm@gmail.com","123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent = new Intent(seleccionarRol.this, MainActivity.class);
                        usuario="invitado";
                        //putExtra envia informacion con el intent a la siguiente activity
                        intent.putExtra("invitado",usuario);
                        startActivity(intent);
                    }
                });
            }
        });
        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(seleccionarRol.this, Identificarse.class);
                usuario="usuario";
                intent.putExtra("usuario",usuario);
                startActivity(intent);
            }
        });
        btnAdministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(seleccionarRol.this, Identificarse.class);
                usuario="Farmacia";
                intent.putExtra("Farmacia",usuario);
                startActivity(intent);
            }
        });
    }
    //onStart metodo de cada activity *** si el actyvity esta en uso este metodo se inicia
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            //usuario esta conectado redireccionamos al activity del inicio
            cargarMain();
        }
    }
    private void cargarMain() {
        Intent intent= new Intent(seleccionarRol.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "se encuentra identificado", Toast.LENGTH_LONG).show();
        finish();
    }
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
}
