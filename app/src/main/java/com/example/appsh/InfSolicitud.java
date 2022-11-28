package com.example.appsh;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class InfSolicitud extends AppCompatActivity {
    private List<BDPendientes> listCodigos = new ArrayList<>();
    ArrayAdapter<BDPendientes> codigosArrayAdapter;
    String TAG = "Firebase";
    ListView listausuarios;
    Button actualizar;
    String carg = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablaaceptados);
        listausuarios = findViewById(R.id.listahor);
        actualizar = findViewById(R.id.btnactulizar);
        listar();


    }

    private void listar() {
        db.collection("BD-Solicitudes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "=>" + document.getData());
                                BDPendientes u = new BDPendientes();

                                u.setnombre(document.get("nombre").toString().toLowerCase());
                                u.setapellidos(document.get("apellido").toString().toLowerCase());
                                u.setcorreo(document.get("correo").toString().toLowerCase());
                                u.setcontraseña(document.get("contraseña").toString().toLowerCase());
                                u.setcargo(document.get("cargo").toString().toLowerCase());
                                Toast.makeText(InfSolicitud.this, u.getnombre() + " =  " + u.getcargo().toString(), Toast.LENGTH_SHORT).show();
                                if (!u.getcargo().equals("")) {

                                    AlertDialog.Builder alerta = new AlertDialog.Builder(InfSolicitud.this);
                                    alerta.setMessage("Felicidades su solicitud fue aceptada ¡BIENVENIDO A SHERPA!")
                                            .setCancelable(false)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                    //Termina el proceso y cierra ventana ---->    finish();
                                                }
                                            });
                                    AlertDialog titulo = alerta.create();
                                    titulo.setTitle("Felicidades");
                                    titulo.show();
                                } else {
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(InfSolicitud.this);
                                    alerta.setMessage("Su solicitud aun no ha sido aceptada")
                                            .setCancelable(false)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                    //Termina el proceso y cierra ventana ---->    finish();
                                                }
                                            });
                                    AlertDialog titulo = alerta.create();
                                    titulo.setTitle("Por Favor Espere");
                                    titulo.show();
                                    Toast.makeText(InfSolicitud.this, u.getnombre() + " Su solicitud aun no ha sido aceptada ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error en datos", task.getException());
                        }
                    }
                });
    }




}





