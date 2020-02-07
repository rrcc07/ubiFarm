package com.rrcc.ubifarm01.menuAdministrador;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrcc.ubifarm01.ClasesDeObjetos.DireccionSucursal;
import com.rrcc.ubifarm01.MapaAgregarActivity;
import com.rrcc.ubifarm01.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarSucursalFragment extends android.support.v4.app.Fragment {

    //declaramnos las variables
    private TextInputLayout nombreDeFarmaciaSucursal, dirSucursal, telefonoSucursal, delegadoSucursal;
    private Button btnGuardarSucursal, btnAgregarEnMapa,btnAperturaTurMa,btnCierreTurMa, btnAperturaTurTa,btnCierreTurTa;
    private TextView popupHoraAperturaTurnoMañana, popupHoraCierreTurnoMañana,popupHoraAperturaTurnoTarde,popupHoraCierreTurnoTarde;
    //popoup de fecha y hora
    private TimePickerDialog.OnTimeSetListener horaAperturaTurnoMañana;
    private TimePickerDialog.OnTimeSetListener horaCierreTurnoMañana;
    private TimePickerDialog.OnTimeSetListener horaAperturaTurnoTarde;
    private TimePickerDialog.OnTimeSetListener horaCierreTurnoTarde;

    private ImageView btnMapaDone;
    //declaro firebase autetificacion y database
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    FirebaseUser currentUser ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //progress
    private ProgressBar loadingProgress;;
    TimePickerDialog tpd;
    private RadioButton radioMañanaLunes, radioMañanaMartes, radioMañanaMiercoles, radioMañanaJueves, radioMañanaViernes, radioMañanaSabado, radioMañanaDomingo;
    private RadioButton radioTardeLunes, radioTardeMartes, radioTardeMiercoles, radioTardeJueves, radioTardeViernes, radioTardeSabado, radioTardeDomingo;
    String diasMañana, diasTarde;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_agregar_sucursal, container, false);

        nombreDeFarmaciaSucursal = fragmentView.findViewById(R.id.nombrefarmaciaSucursal);
        dirSucursal = fragmentView.findViewById(R.id.farmDireccionSucursal);
        telefonoSucursal = fragmentView.findViewById(R.id.sucursalTelefono);
        delegadoSucursal = fragmentView.findViewById(R.id.sucursalDelegado);
        btnGuardarSucursal = fragmentView.findViewById(R.id.btnAñadirSucursal);
        btnAgregarEnMapa = fragmentView.findViewById(R.id.btnSelMapa);
        btnMapaDone = fragmentView.findViewById(R.id.btnDone);
        loadingProgress = fragmentView.findViewById(R.id.regProgressBarSucursal);
        loadingProgress.setVisibility(View.INVISIBLE);


        popupHoraAperturaTurnoMañana = fragmentView.findViewById(R.id.btnAperturaTurMa);
        popupHoraCierreTurnoMañana = fragmentView.findViewById(R.id.btnCierreTurMa);
        popupHoraAperturaTurnoTarde = fragmentView.findViewById(R.id.btnAperturaTurTa);
        popupHoraCierreTurnoTarde = fragmentView.findViewById(R.id.btnCierreTurTa);
        crearHorarios();

        //inicializo firebase autentificacion y database
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("DireccionesSucursales");

        btnGuardarSucursal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGuardarSucursal.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                //obteniendo datos del usuario
                final String nombreSuc = nombreDeFarmaciaSucursal.getEditText().getText().toString();
                final String dirSuc = dirSucursal.getEditText().getText().toString();
                final String telefonoSuc = telefonoSucursal.getEditText().getText().toString();
                final String delegadoSuc = delegadoSucursal.getEditText().getText().toString();

                final String horaAperturaM = popupHoraAperturaTurnoMañana.getText().toString();
                final String horaCierreM = popupHoraCierreTurnoMañana.getText().toString();
                final String horaAperturaT = popupHoraAperturaTurnoTarde.getText().toString();
                final String horaCierreT = popupHoraCierreTurnoTarde.getText().toString();

                String horasTurnoM = horaAperturaM +","+ horaCierreM;
                String horasTurnoT = horaAperturaT +","+ horaCierreT;
                evaluarRadioButtons();
                //verificar si los datos no estan vacios
                if (nombreSuc.isEmpty() || dirSuc.isEmpty() || telefonoSuc.isEmpty()
                        || delegadoSuc.isEmpty() || diasMañana.isEmpty() || diasTarde.isEmpty()
                        || horaAperturaM.isEmpty() || horaCierreM.isEmpty()
                        || horaAperturaT.isEmpty() || horaCierreT.isEmpty()) {
                    showMessage("Porfavor, llene todos los campos");
                    loadingProgress.setVisibility(View.INVISIBLE);
                    btnGuardarSucursal.setVisibility(View.VISIBLE);
                    return;
                }else{
                    btnGuardarSucursal.setVisibility(View.INVISIBLE);
                    loadingProgress.setVisibility(View.VISIBLE);
                    crearSucursal(nombreSuc, dirSuc, telefonoSuc, delegadoSuc, diasMañana, diasTarde, horasTurnoM, horasTurnoT);
                }
            }
        });
        btnAgregarEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!nombreDeFarmaciaSucursal.getEditText().getText().toString().isEmpty()){
                    Intent intent = new Intent(getActivity(), MapaAgregarActivity.class);
                    String nombreSucursal = nombreDeFarmaciaSucursal.getEditText().getText().toString();
                    String nombreFarmacia = currentUser.getDisplayName();
                    intent.putExtra("nombreFarmacia",nombreFarmacia);
                    intent.putExtra("nombreSucursal",nombreSucursal);
                    getActivity().startActivity(intent);
                }else{
                    showMessage("llena el titulo del evento antes de seleccionar la ubicacion en el mapa");
                }
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot direccion: dataSnapshot.getChildren()){
                            DireccionSucursal dir = direccion.getValue(DireccionSucursal.class);
                            String nombreFarmacia = dir.getNombreFarmacia();
                            String nombreSucursal = dir.getNombreSucursal();
                            String key = dir.getDirKey();
                            if(currentUser.getDisplayName().equals(nombreFarmacia) &&
                                    nombreDeFarmaciaSucursal.getEditText().getText().toString().equals(nombreSucursal)){
                                btnMapaDone.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        //metodo RadioButtons
        radios(fragmentView);
        return fragmentView;
    }
    //metodos exportados
    private void crearHorarios() {
        popupHoraAperturaTurnoMañana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int hour=cal.get(Calendar.HOUR_OF_DAY);
                int minute=cal.get(Calendar.MINUTE);
                TimePickerDialog dialog=new TimePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Dialog_MinWidth,
                        horaAperturaTurnoMañana, hour, minute,true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        horaAperturaTurnoMañana =new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                popupHoraAperturaTurnoMañana.setText(twoDigitsHora(hourOfDay)+":"+twoDigitsHora(minute));
                //btnHoraInicio.setVisibility(View.INVISIBLE);
            }
        };
        popupHoraCierreTurnoMañana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int hour=cal.get(Calendar.HOUR_OF_DAY);
                int minute=cal.get(Calendar.MINUTE);
                TimePickerDialog dialog=new TimePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Dialog_MinWidth,
                        horaCierreTurnoMañana, hour, minute,true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        horaCierreTurnoMañana =new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                popupHoraCierreTurnoMañana.setText(twoDigitsHora(hourOfDay)+":"+twoDigitsHora(minute));
                //btnHoraInicio.setVisibility(View.INVISIBLE);
            }
        };
        popupHoraAperturaTurnoTarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int hour=cal.get(Calendar.HOUR_OF_DAY);
                int minute=cal.get(Calendar.MINUTE);
                TimePickerDialog dialog=new TimePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Dialog_MinWidth,
                        horaAperturaTurnoTarde, hour, minute,true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        horaAperturaTurnoTarde =new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                popupHoraAperturaTurnoTarde.setText(twoDigitsHora(hourOfDay)+":"+twoDigitsHora(minute));
                //btnHoraInicio.setVisibility(View.INVISIBLE);
            }
        };
        popupHoraCierreTurnoTarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int hour=cal.get(Calendar.HOUR_OF_DAY);
                int minute=cal.get(Calendar.MINUTE);
                TimePickerDialog dialog=new TimePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Dialog_MinWidth,
                        horaCierreTurnoTarde, hour, minute,true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        horaCierreTurnoTarde =new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                popupHoraCierreTurnoTarde.setText(twoDigitsHora(hourOfDay)+":"+twoDigitsHora(minute));
                //btnHoraInicio.setVisibility(View.INVISIBLE);
            }
        };
    }
    private void evaluarRadioButtons() {
        diasMañana = "";
        if(radioMañanaLunes.isChecked()){
            diasMañana = diasMañana +"lunes, ";}
        if(radioMañanaMartes.isChecked()){
            diasMañana = diasMañana +"martes, ";}
        if(radioMañanaMiercoles.isChecked()){
            diasMañana = diasMañana +"miercoles, ";}
        if(radioMañanaJueves.isChecked()){
            diasMañana = diasMañana +"jueves, ";}
        if(radioMañanaViernes.isChecked()){
            diasMañana = diasMañana +"viernes, ";}
        if(radioMañanaSabado.isChecked()){
            diasMañana = diasMañana +"sabado, ";}
        if(radioMañanaDomingo.isChecked()){
            diasMañana = diasMañana +"domingo ";}
        diasTarde = "";
        if(radioTardeLunes.isChecked()){
            diasTarde = diasTarde +"lunes, ";}
        if(radioTardeMartes.isChecked()){
            diasTarde = diasTarde +"martes, ";}
        if(radioTardeMiercoles.isChecked()){
            diasTarde = diasTarde +"miercoles, ";}
        if(radioTardeJueves.isChecked()){
            diasTarde = diasTarde +"jueves, ";}
        if(radioTardeViernes.isChecked()){
            diasTarde = diasTarde +"viernes, ";}
        if(radioTardeSabado.isChecked()){
            diasTarde = diasTarde +"sabado, ";}
        if(radioTardeDomingo.isChecked()){
            diasTarde = diasTarde +"domingo ";}
    }
    private void radios(View fragmentView) {
        //radios Button
        radioMañanaLunes = fragmentView.findViewById(R.id.radio_lunes_ma);
        radioMañanaLunes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioMañanaLunes.isSelected()){
                    radioMañanaLunes.setSelected(false);
                    radioMañanaLunes.setChecked(false);
                }else{
                    radioMañanaLunes.setSelected(true);
                    radioMañanaLunes.setChecked(true);
                }
            }
        });
        radioMañanaMartes = fragmentView.findViewById(R.id.radio_martes_ma);
        radioMañanaMartes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioMañanaMartes.isSelected()){
                    radioMañanaMartes.setSelected(false);
                    radioMañanaMartes.setChecked(false);
                }else{
                    radioMañanaMartes.setSelected(true);
                    radioMañanaMartes.setChecked(true);
                }
            }
        });
        radioMañanaMiercoles = fragmentView.findViewById(R.id.radio_miercoles_ma);
        radioMañanaMiercoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioMañanaMiercoles.isSelected()){
                    radioMañanaMiercoles.setSelected(false);
                    radioMañanaMiercoles.setChecked(false);
                }else{
                    radioMañanaMiercoles.setSelected(true);
                    radioMañanaMiercoles.setChecked(true);
                }
            }
        });
        radioMañanaJueves = fragmentView.findViewById(R.id.radio_jueves_ma);
        radioMañanaJueves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioMañanaJueves.isSelected()){
                    radioMañanaJueves.setSelected(false);
                    radioMañanaJueves.setChecked(false);
                }else{
                    radioMañanaJueves.setSelected(true);
                    radioMañanaJueves.setChecked(true);
                }
            }
        });
        radioMañanaViernes = fragmentView.findViewById(R.id.radio_viernes_ma);
        radioMañanaViernes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioMañanaViernes.isSelected()){
                    radioMañanaViernes.setSelected(false);
                    radioMañanaViernes.setChecked(false);
                }else{
                    radioMañanaViernes.setSelected(true);
                    radioMañanaViernes.setChecked(true);
                }
            }
        });
        radioMañanaSabado = fragmentView.findViewById(R.id.radio_sabado_ma);
        radioMañanaSabado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioMañanaSabado.isSelected()){
                    radioMañanaSabado.setSelected(false);
                    radioMañanaSabado.setChecked(false);
                }else{
                    radioMañanaSabado.setSelected(true);
                    radioMañanaSabado.setChecked(true);
                }
            }
        });
        radioMañanaDomingo = fragmentView.findViewById(R.id.radio_domingo_ma);
        radioMañanaDomingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioMañanaDomingo.isSelected()){
                    radioMañanaDomingo.setSelected(false);
                    radioMañanaDomingo.setChecked(false);
                }else{
                    radioMañanaDomingo.setSelected(true);
                    radioMañanaDomingo.setChecked(true);
                }
            }
        });
        radioTardeLunes = fragmentView.findViewById(R.id.radio_lunes_ta);
        radioTardeLunes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioTardeLunes.isSelected()){
                    radioTardeLunes.setSelected(false);
                    radioTardeLunes.setChecked(false);
                }else{
                    radioTardeLunes.setSelected(true);
                    radioTardeLunes.setChecked(true);
                }
            }
        });
        radioTardeMartes = fragmentView.findViewById(R.id.radio_martes_ta);
        radioTardeMartes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioTardeMartes.isSelected()){
                    radioTardeMartes.setSelected(false);
                    radioTardeMartes.setChecked(false);
                }else{
                    radioTardeMartes.setSelected(true);
                    radioTardeMartes.setChecked(true);
                }
            }
        });
        radioTardeMiercoles = fragmentView.findViewById(R.id.radio_miercoles_ta);
        radioTardeMiercoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioTardeMiercoles.isSelected()){
                    radioTardeMiercoles.setSelected(false);
                    radioTardeMiercoles.setChecked(false);
                }else{
                    radioTardeMiercoles.setSelected(true);
                    radioTardeMiercoles.setChecked(true);
                }
            }
        });
        radioTardeJueves = fragmentView.findViewById(R.id.radio_jueves_ta);
        radioTardeJueves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioTardeJueves.isSelected()){
                    radioTardeJueves.setSelected(false);
                    radioTardeJueves.setChecked(false);
                }else{
                    radioTardeJueves.setSelected(true);
                    radioTardeJueves.setChecked(true);
                }
            }
        });
        radioTardeViernes = fragmentView.findViewById(R.id.radio_viernes_ta);
        radioTardeViernes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioTardeViernes.isSelected()){
                    radioTardeViernes.setSelected(false);
                    radioTardeViernes.setChecked(false);
                }else{
                    radioTardeViernes.setSelected(true);
                    radioTardeViernes.setChecked(true);
                }
            }
        });
        radioTardeSabado = fragmentView.findViewById(R.id.radio_sabado_ta);
        radioTardeSabado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioTardeSabado.isSelected()){
                    radioTardeSabado.setSelected(false);
                    radioTardeSabado.setChecked(false);
                }else{
                    radioTardeSabado.setSelected(true);
                    radioTardeSabado.setChecked(true);
                }
            }
        });
        radioTardeDomingo = fragmentView.findViewById(R.id.radio_domingo_ta);
        radioTardeDomingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioTardeDomingo.isSelected()){
                    radioTardeDomingo.setSelected(false);
                    radioTardeDomingo.setChecked(false);
                }else{
                    radioTardeDomingo.setSelected(true);
                    radioTardeDomingo.setChecked(true);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void crearSucursal(final String nombreSucursal, final String dirSucursal, final String telefonoSucursal,
                               final String delegado, final  String diasM, String diasT, String horasTurnoM, String horasTurnoT){

        //crear mapa de Sucursal
        Map<String,Object> sucursal = new HashMap<>();
        sucursal.put("NombreFarmacia",currentUser.getDisplayName());
        sucursal.put("FotoFarmacia",currentUser.getPhotoUrl().toString());
        sucursal.put("NombreSucursal",nombreSucursal);
        sucursal.put("DireccionSucursal",dirSucursal);
        sucursal.put("TelefonoSucursal",telefonoSucursal);
        sucursal.put("Delegado",delegado);
        sucursal.put("diasMañana",diasM);
        sucursal.put("diasTarde",diasT);
        sucursal.put("horasMañana",horasTurnoM);
        sucursal.put("horasTarde",horasTurnoT);
        //definir localizacion de datos en firebase
        //mDatabaseReference.child("Farmacias").child(currentUser.getDisplayName()).
        //        child("Sucursales").child(nombreSucursal.trim().toString()).setValue(sucursal).addOnCompleteListener(new OnCompleteListener<Void>() {
        mDatabaseReference.child("Sucursales").push().setValue(sucursal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) { }
        });
        loadingProgress.setVisibility(View.INVISIBLE);
        btnGuardarSucursal.setVisibility(View.VISIBLE);
        showMessage("Creacion de Sucursal correcta");
        limpiarDatosEntrantes();
    }
    private void limpiarDatosEntrantes(){
        nombreDeFarmaciaSucursal.getEditText().setText("");
        dirSucursal.getEditText().setText("");
        delegadoSucursal.getEditText().setText("");
        telefonoSucursal.getEditText().setText("");
        btnMapaDone.setVisibility(View.INVISIBLE);
        limpiarRadios();
        popupHoraAperturaTurnoMañana.setText("08:00");
        popupHoraCierreTurnoMañana.setText("12:00");
        popupHoraAperturaTurnoTarde.setText("14:00");
        popupHoraCierreTurnoTarde.setText("18:00");
    }
    private void limpiarRadios(){
        radioMañanaLunes.setChecked(false);
        radioMañanaMartes.setChecked(false);
        radioMañanaMiercoles.setChecked(false);
        radioMañanaJueves.setChecked(false);
        radioMañanaViernes.setChecked(false);
        radioMañanaSabado.setChecked(false);
        radioMañanaDomingo.setChecked(false);
        radioTardeLunes.setChecked(false);
        radioTardeMartes.setChecked(false);
        radioTardeMiercoles.setChecked(false);
        radioTardeJueves.setChecked(false);
        radioTardeViernes.setChecked(false);
        radioTardeSabado.setChecked(false);
        radioTardeDomingo.setChecked(false);
    }
    // simple metodo para mostrar un Toast general
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
    private String twoDigitsHora(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
}
