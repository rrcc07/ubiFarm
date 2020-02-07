package com.rrcc.ubifarm01;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rrcc.ubifarm01.ClasesDeObjetos.DireccionSucursal;
import com.rrcc.ubifarm01.ClasesDeObjetos.correosHabilitados;

import java.util.HashMap;
import java.util.Map;

public class RegistraFarmaciaActivity extends Activity {

    //declaramnos las variables
    private TextInputLayout nomFarmacia, direccioFarmacia, correoFarmacia, contraseñaFarmacia, contraseñaFarmacia2,
            telefonoFarmacia, nombrePropietario, correoPropietario;
    Button btnRegistrarFarmacia;

    //declaro firebase autetificacion
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference1;
    FirebaseUser currentUser ;

    //declarar registro de foto usuario
    Uri pickedImgUri ;
    ImageView ImgUserPhoto;
    boolean flagImagenSeleccionada = false;
    //CircleImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;

    private ProgressBar loadingProgress;
    private String correoH = "oooo";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_farmacia);

        nomFarmacia = findViewById(R.id.regFarmaciaNombre);
        direccioFarmacia = findViewById(R.id.dirFarmacia);
        correoFarmacia = findViewById(R.id.regFarmaciaCorreo);
        contraseñaFarmacia = findViewById(R.id.regFarmaciaContraseña);
        contraseñaFarmacia2 = findViewById(R.id.regFarmaciaContraseña2);
        telefonoFarmacia = findViewById(R.id.telefonoFarmacia);

        nombrePropietario = findViewById(R.id.nombrePropietario);
        correoPropietario = findViewById(R.id.correoPropietario);

        btnRegistrarFarmacia = findViewById(R.id.btnRegistrarFarmacia);
        loadingProgress = findViewById(R.id.regFarmProgressBar);
        loadingProgress.setVisibility(View.INVISIBLE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase.getReference("CorreosHabilitados");

        //innicializo firease autentificacion
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        btnRegistrarFarmacia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegistrarFarmacia.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                //obteniendo datos del usuario
                final String nombreFarm = nomFarmacia.getEditText().getText().toString();
                final String direccionFarm = direccioFarmacia.getEditText().getText().toString();
                final String correoFarm = correoFarmacia.getEditText().getText().toString();
                final String contraseñaFarm = contraseñaFarmacia.getEditText().getText().toString();
                final String contraseñaFarm2 = contraseñaFarmacia2.getEditText().getText().toString();
                final String telefonoFarm = telefonoFarmacia.getEditText().getText().toString();

                final String nombrePro = nombrePropietario.getEditText().getText().toString();
                final String correoPro = correoPropietario.getEditText().getText().toString();


                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                            correosHabilitados correo = snapshot.getValue(correosHabilitados.class);
                            if(correo.getCorreoFarmacia().equals(correoFarm)){
                                correoH = correoFarm;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                //verificar si los datos no estan vacios
                if (nombreFarm.isEmpty() || direccionFarm.isEmpty() || correoFarm.isEmpty() ||
                        contraseñaFarm.isEmpty() || contraseñaFarm2.isEmpty() || telefonoFarm.isEmpty() ||
                        nombrePro.isEmpty() || correoPro.isEmpty()) {
                    showMessage("Porfavor, llene todos los campos");
                    loadingProgress.setVisibility(View.INVISIBLE);
                    btnRegistrarFarmacia.setVisibility(View.VISIBLE);
                    return;
                }else{
                    if(correoH.equals(correoFarm)){
                        if(contraseñaFarm.equals(contraseñaFarm2)){
                            // todo esta bien y todos los datos estan llenos, ahora podremos crear el usuario
                            crearFarmacia(nombreFarm, direccionFarm, correoFarm, contraseñaFarm,
                                    contraseñaFarm2, telefonoFarm, nombrePro, correoPro);
                        }else{
                            showMessage("las contraseñas deben ser iguales");
                            contraseñaFarmacia.requestFocus();
                            btnRegistrarFarmacia.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                            return;
                        }
                    }
                    else{
                        showMessage("el correo de la farmacia no esta habilitado");
                        correoFarmacia.requestFocus();
                        btnRegistrarFarmacia.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        //iniciamos el dato de la foto
        ImgUserPhoto = findViewById(R.id.regFarmFoto);
        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 21) {
                    verificaSolicitaPermisosAlmacenamiento(); }
                else
                {  abrirGaleria(); }
            }
        });
    }

    private void crearFarmacia(final String nomF,final String dirF, final String correoF,
                               final String conF, final String conF2, final String telF,
                               final String nomP, final String corP){
        btnRegistrarFarmacia.setVisibility(View.INVISIBLE);
        loadingProgress.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(correoF,conF)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //solicitud de id usuario
                            String idFarmacia = mAuth.getCurrentUser().getUid();
                            actualizacionInformacionUsuario( nomF ,pickedImgUri,mAuth.getCurrentUser());
                            //crear mapa de  farmacia Central
                            Map<String,Object> farmacia = new HashMap<>();
                            farmacia.put("idFarmacia",idFarmacia);
                            farmacia.put("nombreDeFarmacia",nomF);
                            farmacia.put("direccioDeFarmacia", dirF);
                            farmacia.put("correoDeFarmacia",correoF);
                            farmacia.put("telefonoDeFarmacia", telF);
                            farmacia.put("propietarioFarmacia",nomP);
                            farmacia.put("correoPropietario",corP);
                            farmacia.put("rol","Farmacia");

                            //definir localizacion de datos en firebase
                            mDatabaseReference.child("Farmacias").child(nomF).setValue(farmacia).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) { }
                            });
                            showMessage("Creacion de farmacia correcta");
                            //limpiarDatosEntrantes();
                        }
                        else{
                            // creacion fallida
                            showMessage("error al crear los datos de farmacia");
                            //agarrar el error de crear cuenta
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            switch (errorCode) {
                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(RegistraFarmaciaActivity.this, "La dirección de correo electrónico está mal escrita", Toast.LENGTH_LONG).show();
                                    correoFarmacia.setError("La dirección de correo electrónico está mal escrita.");
                                    correoFarmacia.requestFocus();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(RegistraFarmaciaActivity.this, "La contraseña no es válida o el usuario no tiene una contraseña.", Toast.LENGTH_LONG).show();
                                    contraseñaFarmacia.setError("password is incorrect ");
                                    contraseñaFarmacia.requestFocus();
                                    contraseñaFarmacia.setHint("");
                                    break;
                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(RegistraFarmaciaActivity.this, "La dirección de correo electrónico ya está en uso por otra cuenta.   ", Toast.LENGTH_LONG).show();
                                    correoFarmacia.setError("El correo ya esta siendo usada por otra cuenta.");
                                    correoFarmacia.requestFocus();
                                    break;

                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                    Toast.makeText(RegistraFarmaciaActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_NOT_FOUND":
                                    Toast.makeText(RegistraFarmaciaActivity.this, "No hay registro de usuario correspondiente a este identificador. El usuario puede haber sido eliminado.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(RegistraFarmaciaActivity.this, "La contraseña no es válida, debe tener al menos 6 caracteres.", Toast.LENGTH_LONG).show();
                                    contraseñaFarmacia.setError("La contraseña no es válida, debe tener al menos 6 caracteres.");
                                    contraseñaFarmacia.requestFocus();
                                    break;
                            }
                        }
                        btnRegistrarFarmacia.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.INVISIBLE);
                    }
                });
    }
    //cargar datos completos del usuario
    private void actualizacionInformacionUsuario(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        // primero necesitamos cargar la foto a firebase storage y pedir la url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Fotos_Farmacia");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // imagen subida con exito, ahora pedimos la imagen
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri contiene la imagen url
                        final UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //showMessage("registro completo");
                                            actualizarDatos();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    //revisar permisos de acceso a foto de la aplicacion
    private void verificaSolicitaPermisosAlmacenamiento() {
        if (ContextCompat.checkSelfPermission(RegistraFarmaciaActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegistraFarmaciaActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegistraFarmaciaActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(RegistraFarmaciaActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            abrirGaleria();
    }

    //retener dato imagen a la aplicacion
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);
            flagImagenSeleccionada = true;
        }
    }
    //abrir galeria del celular
    private void abrirGaleria() {
        //TODO: Abre la galería y espera a que el usuario elija una imagen
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }
    private void actualizarDatos() {
        Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
        //flag para evitar volver a lanzar la pila de activities previos
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivity);
        //finish();
    }
    private void limpiarDatosEntrantes(){
        ImgUserPhoto.setImageResource(R.drawable.userfarmacia);
        nomFarmacia.getEditText().setText("");
        direccioFarmacia.getEditText().setText("");
        correoFarmacia.getEditText().setText("");
        contraseñaFarmacia.getEditText().setText("");
        contraseñaFarmacia2.getEditText().setText("");
        telefonoFarmacia.getEditText().setText("");

        nombrePropietario.getEditText().setText("");
        correoPropietario.getEditText().setText("");
    }
    // simple metodo para mostrar un Toast general
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
