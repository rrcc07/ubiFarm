package com.rrcc.ubifarm01.menuDesarrollador;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrcc.ubifarm01.ClasesDeObjetos.Notificaiones;
import com.rrcc.ubifarm01.ClasesDeObjetos.Sucursal;
import com.rrcc.ubifarm01.FarmaciaPrincipal;
import com.rrcc.ubifarm01.R;
import com.rrcc.ubifarm01.adapterItemFarmacia;
import com.rrcc.ubifarm01.adapterItemNotificaciones;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificacionesFragment extends android.support.v4.app.Fragment {

    RecyclerView recyclerListaNotificaciones;
    adapterItemNotificaciones adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Notificaiones> listaNotificaciones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewFragment = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        recyclerListaNotificaciones = viewFragment.findViewById(R.id.notificacionesRV);
        recyclerListaNotificaciones.setHasFixedSize(true);
        recyclerListaNotificaciones.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("SugerenciaDeFarmacias");

        return viewFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaNotificaciones = new ArrayList<>();
                for(final DataSnapshot farmsnap: dataSnapshot.getChildren()){
                    Notificaiones notificacion = farmsnap.getValue(Notificaiones.class);
                    listaNotificaciones.add(notificacion);
                }
                adapter = new adapterItemNotificaciones(getActivity(),listaNotificaciones);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMessage("Seleccion: "+listaNotificaciones.get(recyclerListaNotificaciones.getChildAdapterPosition(view)).getnombreFarmaciaSugerida());
                    }
                });
                recyclerListaNotificaciones.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
