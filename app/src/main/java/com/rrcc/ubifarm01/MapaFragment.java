package com.rrcc.ubifarm01;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrcc.ubifarm01.ClasesDeObjetos.DireccionSucursal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class MapaFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {

    //private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private MapView mMapView;
    private Marker marker;
    View mView;

    private DatabaseReference databaseReference;
    private ArrayList<Marker> listaTmpMarker = new ArrayList<>();
    private ArrayList<Marker> listaRealMarker = new ArrayList<>();
    private MarkerOptions markerOptions;

    LocationManager locationManager;

    //Variables intentos***********
    double lat1 = 0.0;
    double lng1 = 0.0;
    String mensaje1;
    String direccion = "";

    //***********
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_mapa, container, false);
        //verifiacr si esta activo el GPS
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //verifiacr si esta activo el GPS
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showMessage("Active su GPS, porfavor");
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.maps2);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        //miUbicacion();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng plazaPrincipal14 = new LatLng(-17.3935057, -66.1449397);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(plazaPrincipal14,16));
        marker = mMap.addMarker(new MarkerOptions()
                .position(plazaPrincipal14)
                .title("Mi Ubicacion")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ubication)));

        googleMap.setOnInfoWindowClickListener(this);

        databaseReference.child("DireccionesSucursales").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(Marker marker: listaRealMarker){
                    marker.remove();
                }
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    DireccionSucursal direccionSucursal = snapshot.getValue(DireccionSucursal.class);
                    Double lat = Double.valueOf(direccionSucursal.getLat());
                    Double lng = Double.valueOf(direccionSucursal.getLng());
                    markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(lat,lng))
                            .title(direccionSucursal.getNombreFarmacia())
                            .snippet(direccionSucursal.getDireccionEscrita())
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                    listaTmpMarker.add(mMap.addMarker(markerOptions));
                }
                listaRealMarker.clear();
                listaRealMarker.addAll(listaTmpMarker);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mMap.setOnInfoWindowClickListener(this);
    }
    @Override
    public void onInfoWindowClick(Marker markerIn) {
        if(markerIn.equals(markerOptions)){
            Intent intent = new Intent(getContext(), FarmaciaPrincipal.class);
            showMessage(markerOptions.getTitle());
            startActivity(intent);
        }
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }


            //*****************        ****************//

    // activar servicios de GPS
    private void iniciarLocalizacion() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        final boolean gpsActivado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsActivado) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    public void setLocation(Location location) {
        //obtener direccion de la ubicacion
        if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion = (DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //agregar marcador en el mapa
    private void AgregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate MiUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 14);
        if (marker != null) marker.remove();
        marker = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("My direccion: " + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ubication)));
        mMap.animateCamera(MiUbicacion);
    }

    //agregar la ubicacion
    private void ActualizarUbicacion(Location location) {
        if (location != null) {
            lat1 = location.getLatitude();
            lng1 = location.getLongitude();
            AgregarMarcador(lat1, lng1);
        }
    }

    //control de GPS
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            ActualizarUbicacion(location);
            setLocation(location);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        @Override
        public void onProviderEnabled(String provider) {
            mensaje1 = ("GPS Activado");
            Mensaje();
        }
        @Override
        public void onProviderDisabled(String provider) {
            mensaje1 = ("GPS desactivado");
            iniciarLocalizacion();
            Mensaje();
        }
    };
    //obtener ubicacion
    private static int PETICION_PERMISO_LOCALIZACION = 101;

    private void miUbicacion() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
            return;
        }
        else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ActualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,990900,0,locationListener);
        }
    }
    public void Mensaje(){
        Toast toast = Toast.makeText(getActivity(),mensaje1,Toast.LENGTH_SHORT);
        toast.show();
    }
}
