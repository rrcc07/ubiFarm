package com.rrcc.ubifarm01;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrcc.ubifarm01.ClasesDeObjetos.Farmacia;
import com.rrcc.ubifarm01.ClasesDeObjetos.Sucursal;

import java.util.ArrayList;
import java.util.List;


public class ListaFarmaciasFragment extends android.support.v4.app.Fragment {

    RecyclerView recyclerListaFarmacias;
    adapterItemFarmacia adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Sucursal> listaSucursales;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewFragment = inflater.inflate(R.layout.fragment_lista_farmacias, container, false);

        recyclerListaFarmacias = viewFragment.findViewById(R.id.listaFarmaciasRV);
        recyclerListaFarmacias.setHasFixedSize(true);
        recyclerListaFarmacias.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sucursales");

        return viewFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaSucursales = new ArrayList<>();
                for(final DataSnapshot farmsnap: dataSnapshot.getChildren()){
                    Sucursal sucursal = farmsnap.getValue(Sucursal.class);
                    listaSucursales.add(sucursal);
                }
                adapter = new adapterItemFarmacia(getActivity(),listaSucursales);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMessage("Seleccion: "+listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getNombreSucursal());

                        Intent intent = new Intent(getActivity(), FarmaciaPrincipal.class);
                        intent.putExtra("nombreFarmacia",listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getNombreFarmacia());
                        intent.putExtra("nombreSucursal",listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getNombreSucursal());
                        intent.putExtra("direccionSucursal",listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getDireccionSucursal());
                        intent.putExtra("telefonoSucursal",listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getTelefonoSucursal());
                        intent.putExtra("delegado",listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getDelegado());
                        intent.putExtra("fotoFarmacia",listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getFotoFarmacia());
                        intent.putExtra("horaM",listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getHorasMañana());
                        intent.putExtra("horaT",listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getHorasTarde());
                        intent.putExtra("diasMañana",listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getDiasMañana());
                        intent.putExtra("diasTarde",listaSucursales.get(recyclerListaFarmacias.getChildAdapterPosition(view)).getDiasTarde());

                        startActivity(intent);
                    }
                });
                recyclerListaFarmacias.setAdapter(adapter);

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
