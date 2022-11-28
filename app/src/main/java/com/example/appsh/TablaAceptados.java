package com.example.appsh;

import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TablaAceptados extends AppCompatActivity {
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

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listar();
            }
        });
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
                                Toast.makeText(TablaAceptados.this, u.getnombre()+" =  " + u.getcargo().toString(), Toast.LENGTH_SHORT).show();
                                if (!u.getcargo().equals("")) {


                                    listCodigos.add(u);
                                    codigosArrayAdapter = new ArrayAdapter<BDPendientes>(TablaAceptados.this, android.R.layout.simple_list_item_1, listCodigos);
                                    listausuarios.setAdapter(codigosArrayAdapter);
                                } else {
                                    Toast.makeText(TablaAceptados.this, u.getnombre() + " No ha sido autorizado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error en datos", task.getException());
                        }
                    }
                });
    }


}





