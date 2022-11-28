package com.example.appsh;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistroNuevoUsuario extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    EditText nom, apell, corr, contra;
    TextView carg;
    Button btnfinalizar, btninfo, btnusuarionuevo;
    String TAG = "Firebase";
    Boolean con = true;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String correoinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registronuevousuario);


        nom = findViewById(R.id.txtnombre);
        apell = findViewById(R.id.txtapellidos);
        corr = findViewById(R.id.txtcorreo);
        contra = findViewById(R.id.txtcontraseña);
        //carg = findViewById(R.id.txtcargo);
        btnfinalizar = findViewById(R.id.finalizar);
       /* btninfo = findViewById(R.id.btninfrmacionsoli);
        //btnusuarionuevo=findViewById(R.id.btnnuevo);

        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listar();
            }
        });
        */
        btnfinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                existe();


            }

        });/*
        btnusuarionuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarcaja();
            }
        });*/

    }

    private void limpiarcaja() {
        nom.setText("");
        apell.setText("");
        corr.setText("");
        contra.setText("");


        con = true;

    }

    private void validacion() {
        String nombre = nom.getText().toString().toLowerCase();
        String apellido = apell.getText().toString().toLowerCase();
        String correo = corr.getText().toString().toLowerCase();
        String contraseñ = contra.getText().toString().toLowerCase();


        if (nombre.equals("")) {
            nom.setError("Dato requerido");
        } else if (apellido.equals("")) {
            apell.setError("Dato requerido");
        } else if (correo.equals("")) {
            corr.setError("Dato requqerido");
        } else if (contraseñ.equals("")) {
            contra.setError("Dato requerido");
        }
    }

    public void guardardatos() {
        String nombre = nom.getText().toString().toLowerCase();
        String apellido = apell.getText().toString().toLowerCase();
        String correo = corr.getText().toString().toLowerCase();
        String contraseña = contra.getText().toString().toLowerCase();
        //   String cargo = carg.getText().toString().toLowerCase();
        CollectionReference persons = db.collection("BD-Solicitudes");
        correoinfo = correo;

        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("apellido", apellido);
        user.put("correo", correo);
        user.put("contraseña", contraseña);
        user.put("cargo", "");


        persons.document(corr.getText().toString().toLowerCase()).set(user);



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
                                //  Toast.makeText(Registro.this, u.getnombre() + " =  " + u.getcargo().toString(), Toast.LENGTH_SHORT).show();

                                if (correoinfo.equals(u.getcorreo())) {


                                    if (!u.getcargo().equals("")) {

                                        carg.setText(u.getcargo().toString());

                                        AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroNuevoUsuario.this);
                                        alerta.setMessage("Felicidades su solicitud fue aceptada ¡BIENVENIDO A SHERPA ")
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
                                        AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroNuevoUsuario.this);
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
                                        // Toast.makeText(Registro.this, u.getnombre() + " Su solicitud aun no ha sido aceptada ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "Error en datos", task.getException());
                        }
                    }
                });
    }


    private void existe() {

        if (con == true) {

            String nombre = nom.getText().toString().toLowerCase();
            String apellido = apell.getText().toString().toLowerCase();
            String correo = corr.getText().toString().toLowerCase();
            String contraseña = contra.getText().toString().toLowerCase();
            // String cargo = carg.getText().toString().toLowerCase();


            if (nombre.equals("") || apellido.equals("") || correo.equals("") || contraseña.equals("")) {
                validacion();
            } else {
                guardardatos();

                AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroNuevoUsuario.this);
                alerta.setMessage("Su solicitud fue enviada, un administrador la autorizara mas tarde, ¡Gracias!")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Bundle envia = new Bundle();
                                envia.putString("UsuarioOn", correoinfo);
                                Intent m = new Intent(RegistroNuevoUsuario.this, Consultapeticion.class);
                                m.putExtras(envia);
                                startActivity(m);


                                //Termina el proceso y cierra ventana ---->    finish();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Felicidades");
                titulo.show();


                // limpiarcaja();
                con = false;
            }


        } else {
            Toast.makeText(this, "Tu solicitud ya fue enviada", Toast.LENGTH_SHORT).show();
        }
    }



}

