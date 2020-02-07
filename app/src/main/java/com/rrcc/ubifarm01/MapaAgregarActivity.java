package com.rrcc.ubifarm01;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rrcc.ubifarm01.ClasesDeObjetos.DireccionSucursal;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapaAgregarActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Button puntoSeleccionado;
    private Marker markerDrag;
    private Marker markerPos;
    DatabaseReference mDatabase;
    private double lat;
    private double lng;
    String nombreFarmacia,nombreSucursal, dir,dirKey;
    boolean flag = false;

    //Variables intentos***********
    private Marker marker;
    //double lat = 0.0;
    //double lng = 0.0;
    String mensaje1;
    String direccion = "";
    //***********
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_agregar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaAgregar);
        mapFragment.getMapAsync(this);

        //verifiacr si esta activo el GPS
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(MapaAgregarActivity.this, "Active su GPS, porfavor",Toast.LENGTH_LONG).show();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        getIncomingIntent();
        //añadir punto
        mDatabase = FirebaseDatabase.getInstance().getReference();
        puntoSeleccionado = findViewById(R.id.btnSeleccionar);
        puntoSeleccionado.setVisibility(View.INVISIBLE);
        puntoSeleccionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Punto Añadido",Toast.LENGTH_SHORT).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("DireccionesSucursales").push();
                // get post unique ID and upadte post key
                String Dirkey = myRef.getKey();
                DireccionSucursal direccionSucursal = new DireccionSucursal(Double.toString(lat),Double.toString(lng),
                                                                                    nombreFarmacia,nombreSucursal,dir);
                direccionSucursal.setDirKey(Dirkey);
                myRef.setValue(direccionSucursal).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        flag = true;
                        onBackPressed();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(flag){
            super.onBackPressed();
        }else{
            Toast.makeText(getApplicationContext(),"Debe posicionar el punto en el mapa, y presinoar AÑADIR DIRECCION",Toast.LENGTH_LONG).show();
        }
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("nombreFarmacia") && getIntent().hasExtra("nombreSucursal")) {
            nombreFarmacia = getIntent().getStringExtra("nombreFarmacia");
            nombreSucursal = getIntent().getStringExtra("nombreSucursal");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();
        //agregar controles de zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);

        LatLng p = new LatLng(-17.3935853, -66.1569588);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 13));

        LatLng plaza = new LatLng(-17.3935853, -66.1569588);
        markerDrag =googleMap.addMarker(new MarkerOptions()
                .position(p)
                .title("Arrastra este marcador sin soltar y selecciona un lugar")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(plaza));
        googleMap.setOnMarkerDragListener(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) { }
    @Override
    public boolean onMarkerClick(Marker marker) { return false; }
    @Override
    public void onMarkerDragStart(Marker marker) { puntoSeleccionado.setVisibility(View.INVISIBLE); }
    @Override
    public void onMarkerDrag(Marker marker) { }
    @Override
    public void onMarkerDragEnd(Marker marker) {
        if(marker.equals(markerDrag)){
            String newTitle = String.format(Locale.getDefault(),
                    getString(R.string.marker_detail_latlng),
                    marker.getPosition().latitude,
                    marker.getPosition().longitude);
            lat=marker.getPosition().latitude;
            lng=marker.getPosition().longitude;
        }
        puntoSeleccionado.setVisibility(View.VISIBLE);
        setLocation(lat,lng);
    }
    public void setLocation(double lat, double log) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (lat != 0.0 && log != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        lat, log, 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    dir = ""+ DirCalle.getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this,"Agregar esta direccion:"+dir,Toast.LENGTH_LONG).show();

    }

                                    /*****************************/
    // activar servicios de GPS
    private void iniciarLocalizacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
            lat = location.getLatitude();
            lng = location.getLongitude();
            AgregarMarcador(lat, lng);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
            return;
        }
        else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ActualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,90900,0,locationListener);
        }

    }
    public void Mensaje(){
        Toast toast = Toast.makeText(this,mensaje1,Toast.LENGTH_SHORT);
        toast.show();
    }
}
