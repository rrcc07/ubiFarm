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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rrcc.ubifarm01.ClasesDeObjetos.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Registrarse extends Activity {

    //declarar datos
    private TextInputLayout usuario, correo, contraseña, contraseña2;
    TextView btnSeleccionarFoto;
    //declarar botones
    private Button btnRegistrarse;
    //progressBar
    private ProgressBar loadingProgress;
    //declaro firebase autetificacion
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    FirebaseUser currentUser ;

    //declarar registro de foto usuario
    Uri pickedImgUri ;
    ImageView ImgUserPhoto;
    boolean flagImagenSeleccionada = false;
    //CircleImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        //inicializar datos
        usuario=(TextInputLayout) findViewById(R.id.regUsuarioNombre);
        correo= (TextInputLayout) findViewById(R.id.regUsuarioCorreo);
        contraseña= (TextInputLayout) findViewById(R.id.regUsuarioContraseña);
        contraseña2= (TextInputLayout) findViewById(R.id.regUsurioContraseña2);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        loadingProgress = findViewById(R.id.regProgressBar);
        loadingProgress.setVisibility(View.INVISIBLE);

        btnSeleccionarFoto=findViewById(R.id.btn_selec_imagen_usuario);
        //innicializo firease autentificacion
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegistrarse.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                //obteniendo datos del usuario
                final String name = usuario.getEditText().getText().toString();
                final String email = correo.getEditText().getText().toString();
                final String password = contraseña.getEditText().getText().toString();
                final String password2 = contraseña2.getEditText().getText().toString();

                if( email.isEmpty() || name.isEmpty() || password.isEmpty()  || password.isEmpty() || password2.isEmpty() || flagImagenSeleccionada == false) {
                    // a veces muestra error: todos los datos deben estar llenos
                    // necesitamos mostrar el mensaje de error
                    showMessage("Porfavor verifique que los campos esten llenados") ;
                    if(flagImagenSeleccionada==false) { showMessage("seleccione una foto de Usuario");}
                    btnRegistrarse.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
                else {
                    if(password.equals(password2)){
                        //todo esta bien y todos los datos estan llenos, ahora podremos crear el usuario
                        registrarUsuario(email, name, password, password2);
                    }
                    else{
                        showMessage("las contraseñas deben ser iguales");
                        btnRegistrarse.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.INVISIBLE);
                        return;
                    }
                }
            }
        });
        //iniciamos el dato de la foto
        ImgUserPhoto = findViewById(R.id.regUsuarioFoto);
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

    //registrar usuario
    private void registrarUsuario(final String email, final String name, String password, String password2) {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //showMessage("Cuenta crada con exito!!!");
                            finish(); // no volver a abrir el activity
                            actualizacionInformacionUsuario( name ,pickedImgUri,mAuth.getCurrentUser());
                            //id de usuario
                            String idUsuario = mAuth.getCurrentUser().getUid();
                            //crear mapa de  datos usuario
                            Map<String,Object> usuario = new HashMap<>();
                            usuario.put("idUsuario",idUsuario);
                            usuario.put("nombreUsuario", name);
                            usuario.put("correoUsuario",email);
                            usuario.put("rolUsuario","Usuario");
                            //definir localizacion de datos en firebase
                            mDatabaseReference.child("Usuarios").child(idUsuario).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {

                                }
                            });
                        }
                        else{
                            // cuenta creada fallida
                            showMessage("creacion de cuenta fallida ");
                            //agarrar el error de crear cuenta
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            switch (errorCode) {
                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(Registrarse.this, "La dirección de correo electrónico está mal escrita", Toast.LENGTH_LONG).show();
                                    correo.setError("The email address is badly formatted.");
                                    correo.requestFocus();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(Registrarse.this, "La contraseña no es válida o el usuario no tiene una contraseña.", Toast.LENGTH_LONG).show();
                                    contraseña.setError("password is incorrect ");
                                    contraseña.requestFocus();
                                    contraseña.setHint("");
                                    break;
                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(Registrarse.this, "La dirección de correo electrónico ya está en uso por otra cuenta.   ", Toast.LENGTH_LONG).show();
                                    correo.setError("The email address is already in use by another account.");
                                    correo.requestFocus();
                                    break;
                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                    Toast.makeText(Registrarse.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_NOT_FOUND":
                                    Toast.makeText(Registrarse.this, "No hay registro de usuario correspondiente a este identificador. El usuario puede haber sido eliminado.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(Registrarse.this, "La contraseña no es válida, debe tener al menos 6 caracteres.", Toast.LENGTH_LONG).show();
                                    contraseña.setError("The password is invalid it must 6 characters at least");
                                    contraseña.requestFocus();
                                    break;

                                case "ERROR_INVALID_CUSTOM_TOKEN":
                                    Toast.makeText(Registrarse.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                    Toast.makeText(Registrarse.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_CREDENTIAL":
                                    Toast.makeText(Registrarse.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_MISMATCH":
                                    Toast.makeText(Registrarse.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_REQUIRES_RECENT_LOGIN":
                                    Toast.makeText(Registrarse.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                    Toast.makeText(Registrarse.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_DISABLED":
                                    Toast.makeText(Registrarse.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_TOKEN_EXPIRED":
                                    Toast.makeText(Registrarse.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_USER_TOKEN":
                                    Toast.makeText(Registrarse.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_OPERATION_NOT_ALLOWED":
                                    Toast.makeText(Registrarse.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                    break;
                            }
                            btnRegistrarse.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    //cargar datos completos del usuario
    private void actualizacionInformacionUsuario(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        // primero necesitamos cargar la foto a firebase storage y pedir la url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Fotos_Usuario");
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
        if (ContextCompat.checkSelfPermission(Registrarse.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Registrarse.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Registrarse.this,"Porfavor, acepte los permisos requeridos",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(Registrarse.this,
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
    public static String getHourPhone() {
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formatteHour = df.format(dt.getTime());
        return formatteHour;
    }
    // simple metodo para mostrar un Toast general
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
