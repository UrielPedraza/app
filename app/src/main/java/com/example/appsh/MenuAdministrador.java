package com.example.appsh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MenuAdministrador extends AppCompatActivity {
    TextView txtuser, usertxt;

    public String reci,bateri;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuadministrador);

        txtuser = findViewById(R.id.lineauser);
        BroadcastReceiver broadcastReceiverBattery = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Integer integerBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                String bateria = integerBatteryLevel.toString();
                System.out.println("Estado de la bateria =-------------------------------------------------> " + bateria);
                bateri=bateria;
            }
        };
        registerReceiver(broadcastReceiverBattery, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        System.out.println("es es es es -------------"+bateri+".........");




        Bundle recibe = getIntent().getExtras();
        reci = recibe.getString("UsuarioOn");
        txtuser.setText("Usuario en MenuAdministrador: " + reci+" ----------------------------------------------------------Bateria= "+bateri);
        System.out.println("(!--=Usuario MenuAdministrador: " + reci+" ----------------------------------------------------------Bateria= "+bateri);
        Bundle envia = new Bundle();
        envia.putString("UsuarioOn", reci);
        envia.putString("BateriaBat", bateri);
        BateriaUpdate();

    }
/*
    public void irqrlector(View view) {

        Bundle envia = new Bundle();
        envia.putString("UsuarioOn", reci);
        System.out.println("recibe el perro-------------------" + reci);
        Intent m = new Intent(this, LectorQR.class);
        m.putExtras(envia);
        startActivity(m);

    }

    public void irqrgenerador(View view) {
        Intent e = new Intent(this, GeneradorQR.class);
        startActivity(e);
    }


    public void irsalir(View view) {
        Intent w = new Intent(this, Login.class);
        startActivity(w);
    }

    public void iradmincodigos(View view) {
        Intent q = new Intent(this, AdministradorUsuarios.class);
        Bundle envia = new Bundle();
        envia.putString("UsuarioOn", reci);
        envia.putString("BateriaBat", bateri);
        q.putExtras(envia);
        startActivity(q);
    }



    public void iradministracioncodigos(View view) {
        Intent u = new Intent(this, AdministradorCodigos.class);
        Bundle envia = new Bundle();
        envia.putString("UsuarioOn", reci);
        u.putExtras(envia);
        startActivity(u);


    }
    public void irSolicitudes(View view) {
        Intent v= new Intent(this, Solicitudes.class);
        Bundle envia = new Bundle();
        envia.putString("UsuarioOn", reci);
        envia.putString("BateriaBat", bateri);
        v.putExtras(envia);
        startActivity(v);
    }

*/


    public void BateriaUpdate() {
        DocumentReference docRef = db.collection("Codigos").document(reci);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();


                    if (document.exists()) {
                        String nombre = document.get("Nombre").toString();
                        String apellido = document.get("Apellidos").toString();
                        String correo = document.get("Correo").toString();
                        String contra = document.get("Contraseña").toString();
                        String a = document.get("NCodigos").toString();
                        String cargo = document.get("Cargo").toString();





                        CollectionReference cities = db.collection("Codigos");

                        Map<String, Object> data1 = new HashMap<>();
                        data1.put("Nombre", nombre);
                        data1.put("Apellidos", apellido);
                        data1.put("Correo", correo);
                        data1.put("Contraseña", contra);
                        data1.put("NCodigos", a);
                        data1.put("Cargo",cargo);
                        data1.put("Bateria",bateri);
                        System.out.println("baeria dentro de------"+bateri+"...........");

                        cities.document(reci).set(data1);


                        Toast.makeText(MenuAdministrador.this, "Pila Correcto", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(MenuAdministrador.this, "Error en Pila", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


}