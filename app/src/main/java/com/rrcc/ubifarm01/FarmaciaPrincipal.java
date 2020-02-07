package com.rrcc.ubifarm01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FarmaciaPrincipal extends AppCompatActivity {

    private TextView nombreFarmacia, direccionSucursal,telefonoSucursal,delegado,horaM,horaT,diaM,diaT;
    private ImageView fotoFarmacia;

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri uri;
    Button editarFarmacia,eliminarFarmacia;

    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    String nombreFarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmacia_principal);
        getIncomingIntent();

        //iniciamos firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("nombreFarmacia") && getIntent().hasExtra("nombreSucursal") && getIntent().hasExtra("direccionSucursal")
        && getIntent().hasExtra("telefonoSucursal") && getIntent().hasExtra("delegado")
        && getIntent().hasExtra("fotoFarmacia") && getIntent().hasExtra("horaM") && getIntent().hasExtra("horaT")
        && getIntent().hasExtra("diasMañana") && getIntent().hasExtra("diasTarde")) {

            nombreFarm = getIntent().getStringExtra("nombreFarmacia");
            String nombreSucu = getIntent().getStringExtra("nombreSucursal");
            String direccionSucu = getIntent().getStringExtra("direccionSucursal");
            String telefonoSucursal = getIntent().getStringExtra("telefonoSucursal");
            String delegado = getIntent().getStringExtra("delegado");
            String fotoFarmacia = getIntent().getStringExtra("fotoFarmacia");
            String horaMa = getIntent().getStringExtra("horaM");
            String horaTa = getIntent().getStringExtra("horaT");
            String diasMa = getIntent().getStringExtra("diasMañana");
            String diasTa = getIntent().getStringExtra("diasTarde");

            setearDatos(nombreSucu,direccionSucu,telefonoSucursal,delegado,fotoFarmacia,
                    horaMa, horaTa, diasMa, diasTa);
        }
    }

    private void setearDatos(String nombreSucu, String direccionSucu, String telefonoS, String dele, final String fotoFarm,
                            String horaMa, String horaTa, String diasM, String diasT) {
        //Datos del Activity
        nombreFarmacia = findViewById(R.id.nombreF);
        direccionSucursal = findViewById(R.id.direccionS);
        telefonoSucursal = findViewById(R.id.telefonoFarm);
        delegado = findViewById(R.id.delegadoSucu);
        fotoFarmacia = findViewById(R.id.imageFarmacia);
        horaM = findViewById(R.id.horarioM);
        horaT = findViewById(R.id.horarioT);
        diaM = findViewById(R.id.horariosMa);
        diaT = findViewById(R.id.horariosTa);

        nombreFarmacia.setText(nombreSucu);
        direccionSucursal.setText(direccionSucu);
        telefonoSucursal.setText(telefonoS);
        delegado.setText(dele);
        horaM.setText(horaMa.substring(0,5) + " - " + horaMa.substring(6,11));
        horaT.setText(horaTa.substring(0,5) + " - " + horaTa.substring(6,11));
        diaM.setText(diasM);
        diaT.setText(diasT);

        // ahora usando Glide cargaremos la foto de farmacia
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Glide.with(this).load(fotoFarm).centerCrop().into(fotoFarmacia);
    }

    //metodo para mostrar mesajes (Toast)
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
}
