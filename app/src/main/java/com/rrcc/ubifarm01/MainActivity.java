package com.rrcc.ubifarm01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rrcc.ubifarm01.menuAdministrador.AgregarSucursalFragment;
import com.rrcc.ubifarm01.menuDesarrollador.HabilitarCorreoFragment;
import com.rrcc.ubifarm01.menuDesarrollador.NotificacionesFragment;
import com.rrcc.ubifarm01.menuUsuario.SugerirFarmaciaFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    GoogleMap mMap;
    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    //seleccio de rol
    String rol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //pagina de inicio
        setFragmenMapaFarmacias(new MapaFragment());

        //iniciamos firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //datos entrantes
        getIncomingIntent();
        //cargar NavHeader datos del usuario
        cargarNavHeader();
    }

    //opcion de roles
    private void getIncomingIntent() {
        if(getIntent().hasExtra("invitado")) {
            rol = getIntent().getStringExtra("invitado");
        }
        if(getIntent().hasExtra("usuario")) {
            rol = getIntent().getStringExtra("usuario");
        }
        if(getIntent().hasExtra("Farmacia")) {
            rol = getIntent().getStringExtra("Farmacia");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.nuevaBusqueda) {
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    /*cargamos la foto y datos del usuario y administrador*/
    private void cargarNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        CircleImageView navUserPhot = headerView.findViewById(R.id.navImageUser);
        TextView navUsername = headerView.findViewById(R.id.navNameUser);
        TextView navUserMail = headerView.findViewById(R.id.navEmailUser);
        // ahora usando Glide cargaremos la foto de usuario y datos
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);
        navUsername.setText(currentUser.getDisplayName());
        navUserMail.setText(currentUser.getEmail());
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mapa_farmacias) {
            //Agregamos el mapa dentro del navigationDrawer
            setFragmenMapaFarmacias(new MapaFragment());
            //titulo del fragment
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            Toast.makeText(getApplicationContext(), "Mapas de Farmacia", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_habilitar_correo_farmacias) {
                if(currentUser.getEmail().equals("develasco1996@gmail.com") || currentUser.getEmail().equals("miguel.lopez.aliendre0409@gmail.com")){
                    setFragmentHabilitar(new HabilitarCorreoFragment());
                    //titulo del fragment
                    item.setChecked(true);
                    getSupportActionBar().setTitle(item.getTitle());
                    Toast.makeText(getApplicationContext(), "Habilitar Correo", Toast.LENGTH_SHORT).show();
                }else{
                    showMessage("no puede realizar esta accion, debe ser administrador");
                }
        }else if (id == R.id.nav_agregar_sucursal) {
            if(currentUser.getEmail().equals("ubifarm@gmail.com")){
                showMessage("no puede realizar esta accion, debe ser una Farmacia");
            }else{
            setFragmentAgregarSucursal(new AgregarSucursalFragment());
            //titulo del fragment
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            Toast.makeText(getApplicationContext(), "Agregar Sucursal", Toast.LENGTH_SHORT).show();}
        } else if (id == R.id.nav_notificaciones) {
            if(currentUser.getEmail().equals("develasco1996@gmail.com") || currentUser.getEmail().equals("miguel.lopez.aliendre0409@gmail.com")){
                setFragmentNotificaciones(new NotificacionesFragment());
                //titulo del fragment
                item.setChecked(true);
                getSupportActionBar().setTitle(item.getTitle());
                Toast.makeText(getApplicationContext(), "Notificaciones Administradores", Toast.LENGTH_SHORT).show();
            }else{
                showMessage("no puede realizar esta accion, debe ser administrador"); }
        } else if (id == R.id.nav_sugerirFarmacia) {
            if(currentUser.getEmail().equals("ubifarm@gmail.com")){
                showMessage("no puede realizar esta accion, debe ser Usuario registrado");
            }else{
            setFragmentSugerirFarmacia(new SugerirFarmaciaFragment());
            //titulo del fragment
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            Toast.makeText(getApplicationContext(), "Sugerir Farmacias", Toast.LENGTH_SHORT).show();}
        } else if (id == R.id.nav_lista_farmacia) {
            setFragmenListaFarmacias(new ListaFarmaciasFragment());
            //titulo del fragment
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
            Toast.makeText(getApplicationContext(), "Lista Farmacias", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.nav_salir){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, seleccionarRol.class));
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragmenMapaFarmacias(MapaFragment maps){
        if(maps!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, maps);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    private void setFragmentHabilitar(HabilitarCorreoFragment habilitarCorreoFragment){
        if(habilitarCorreoFragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, habilitarCorreoFragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    private void setFragmentAgregarSucursal(AgregarSucursalFragment agregarSucursalFragment){
        if(agregarSucursalFragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, agregarSucursalFragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    private void setFragmentNotificaciones(NotificacionesFragment notificacionesFragment){
        if(notificacionesFragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, notificacionesFragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    private void setFragmentSugerirFarmacia(SugerirFarmaciaFragment sugerirFarmaciaFragment){
        if(sugerirFarmaciaFragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, sugerirFarmaciaFragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    private void setFragmenListaFarmacias(ListaFarmaciasFragment listaFarmaciasFragment){
        if(listaFarmaciasFragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, listaFarmaciasFragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng plazaPrincipal14 = new LatLng(-17.3935853, -66.1569588);
        mMap.addMarker(new MarkerOptions().position(plazaPrincipal14).title("Plaza 14 de septiembre"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(plazaPrincipal14,14));
    }


    public void showMessage(String message) {
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
    }

}
