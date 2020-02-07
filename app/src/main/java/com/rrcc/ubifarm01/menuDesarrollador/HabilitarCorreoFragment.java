package com.rrcc.ubifarm01.menuDesarrollador;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rrcc.ubifarm01.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HabilitarCorreoFragment extends Fragment {

    TextInputLayout correoHabilitar;
    Button agregarCorreo;

    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    FirebaseUser currentUser ;
    //progress
    private ProgressBar loadingProgress;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_habilitar_correo, container, false);

        correoHabilitar = fragmentView.findViewById(R.id.correoParaHabilitar);
        agregarCorreo = fragmentView.findViewById(R.id.btnAñadirCorreo);

        //inicializo firebase autentificacion y database
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        loadingProgress = fragmentView.findViewById(R.id.regProgressBarAgregarCorreo);
        agregarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarCorreo.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);

                final String correoH = correoHabilitar.getEditText().getText().toString();

                if (correoH.isEmpty()) {
                    showMessage("Porfavor, llene todos los campos");
                    loadingProgress.setVisibility(View.INVISIBLE);
                    agregarCorreo.setVisibility(View.VISIBLE);
                    return;
                }else{
                    agregarCorreo.setVisibility(View.INVISIBLE);
                    loadingProgress.setVisibility(View.VISIBLE);
                    //crear mapa de Sucursal
                    Map<String,Object> correo = new HashMap<>();
                    correo.put("correoFarmacia",correoH);

                    //definir localizacion de datos en firebase
                    mDatabaseReference.child("CorreosHabilitados").push().setValue(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) { }
                    });
                    loadingProgress.setVisibility(View.INVISIBLE);
                    agregarCorreo.setVisibility(View.VISIBLE);
                    showMessage("Correo agregado correctamente");
                    limpiarDatosEntrantes();
                }
            }
        });

        return fragmentView;
    }

    private void limpiarDatosEntrantes(){
        correoHabilitar.getEditText().setText("");
    }

    // simple metodo para mostrar un Toast general
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
