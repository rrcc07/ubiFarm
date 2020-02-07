package com.rrcc.ubifarm01.menuUsuario;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rrcc.ubifarm01.R;
import com.rrcc.ubifarm01.Registrarse;

import java.util.HashMap;
import java.util.Map;


public class SugerirFarmaciaFragment extends android.support.v4.app.Fragment {

    TextInputLayout nombrefarmaciaS, telefonoSu, correoSugerenciaFa, direccionSu;
    Button btnSugerirFa;

    ProgressBar regProgressSugerirFarmacia;

    //declaro firebase autetificacion y database
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    FirebaseUser currentUser ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewFragment = inflater.inflate(R.layout.fragment_sugerir_farmacia, container, false);
        
        nombrefarmaciaS = viewFragment.findViewById(R.id.nombrefarmaciaSugerir);
        telefonoSu = viewFragment.findViewById(R.id.telefonoSugerir);
        correoSugerenciaFa = viewFragment.findViewById(R.id.correoSugerenciaFarmacia);
        direccionSu = viewFragment.findViewById(R.id.dreccionSugerencia);
        btnSugerirFa = viewFragment.findViewById(R.id.btnSugerirFarmacia);
        regProgressSugerirFarmacia = viewFragment.findViewById(R.id.regProgressSugerirFarmacia);

        //inicializo firebase autentificacion y database
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        btnSugerirFa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSugerirFa.setVisibility(View.INVISIBLE);
                regProgressSugerirFarmacia.setVisibility(View.VISIBLE);

                final String nomFarmacia = nombrefarmaciaS.getEditText().getText().toString();
                final String teleFarmacia = telefonoSu.getEditText().getText().toString();
                final String corrFarmacia = correoSugerenciaFa.getEditText().getText().toString();
                final String dirFarmacia = direccionSu.getEditText().getText().toString();

                if(nomFarmacia.isEmpty() || teleFarmacia.isEmpty() || corrFarmacia.isEmpty()
                        || dirFarmacia.isEmpty()){
                    showMessage("Porfavor, llene todos los campos");
                    regProgressSugerirFarmacia.setVisibility(View.INVISIBLE);
                    btnSugerirFa.setVisibility(View.VISIBLE);
                    return;
                }else{
                    btnSugerirFa.setVisibility(View.INVISIBLE);
                    regProgressSugerirFarmacia.setVisibility(View.VISIBLE);
                    sugerirFarmacia(nomFarmacia, teleFarmacia, corrFarmacia, dirFarmacia);
                }
            }
        });

        return viewFragment;
    }

    private void sugerirFarmacia(String nomFarmacia, String teleFarmacia, String corrFarmacia, String dirFarmacia) {
        //crear mapa de Sucursal
        Map<String,Object> sugFarmacia = new HashMap<>();
        sugFarmacia.put("correoUsuario",currentUser.getEmail());
        sugFarmacia.put("nombreFarmaciaSugerida",nomFarmacia);
        sugFarmacia.put("telefonoFarmaciaSugerida",teleFarmacia);
        sugFarmacia.put("correoFarmaciaSugerencia",corrFarmacia);
        sugFarmacia.put("direccionFarmaciaSugerida",dirFarmacia);

        mDatabaseReference.child("SugerenciaDeFarmacias").push().setValue(sugFarmacia).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                showMessage("Sugerencia de Farmacia envida");
            }
        });
        regProgressSugerirFarmacia.setVisibility(View.INVISIBLE);
        btnSugerirFa.setVisibility(View.VISIBLE);
        limpiarDatosEntrantes();
    }

    private void limpiarDatosEntrantes() {
        nombrefarmaciaS.getEditText().setText("");
        telefonoSu.getEditText().setText("");
        correoSugerenciaFa.getEditText().setText("");
        direccionSu.getEditText().setText("");
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}
