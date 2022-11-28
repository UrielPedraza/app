package com.example.appsh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Consultapeticion extends AppCompatActivity {
    String TAG = "Firebase";
    public String correoinfo;
    boolean val = true, aux = true;
    TextView nom, apell, corr, contra, carg, txtin;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String reci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultapeticion);

        nom = findViewById(R.id.tvnombre);
        apell = findViewById(R.id.tvapellidos);
        corr = findViewById(R.id.tvcorreo);
        contra = findViewById(R.id.tvcontraseña);
        carg = findViewById(R.id.tvcargo);
        txtin = findViewById(R.id.txtinf);
        Bundle recibe = getIntent().getExtras();
        reci = recibe.getString("UsuarioOn");
        System.out.println("Usuario en MenuAdministrador: " + reci + " ----------------------------------------------------------usuario= " + reci);
        correoinfo = reci;


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
                                //  Toast.makeText(Registro.this, u.getnombre() + " =  " + u.getcargo().toString(), Toast.LENGTH_SHORT).show();


                                if (correoinfo.equals(u.getcorreo())) {
                                    nom.setText(u.getnombre());
                                    apell.setText(u.getapellidos());
                                    corr.setText(correoinfo);
                                    contra.setText(u.getcontraseña());
                                    carg.setText(u.getcargo().toString());


                                    if (!u.getcargo().equals("")) {
                                        txtin.setTextColor(Color.parseColor("#1E7A20"));
                                        txtin.setText("Aceptado");

                                        aux = false;

                                    } else {
                                        txtin.setTextColor(Color.parseColor("#811C1C"));
                                        txtin.setText("Su solicitud aun no ha sido aceptada");

                                        listar();

                                    }

                                    if (!u.getcargo().equals("")) {

                                        EliminarUsuario();
                                        AlertDialog.Builder alerta = new AlertDialog.Builder(Consultapeticion.this);
                                        alerta.setMessage("Su solicitud fue aceptada ¡BIENVENIDO A SHERPA ")
                                                .setCancelable(false)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {


                                                        finish();
                                                        Bundle envia = new Bundle();
                                                        envia.putString("UsuarioOn", correoinfo);
                                                        Intent m = new Intent(Consultapeticion.this, Login.class);
                                                        m.putExtras(envia);
                                                        startActivity(m);

                                                        guardardatos();

                                                    }
                                                });
                                        AlertDialog titulo = alerta.create();
                                        titulo.setTitle("Felicidades");
                                        titulo.show();
                                        aux = false;
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "Error en datos", task.getException());
                        }
                    }
                });
    }

    public void guardardatos() {
        String nombre = nom.getText().toString().toLowerCase();
        String apellido = apell.getText().toString().toLowerCase();
        String correo = corr.getText().toString().toLowerCase();
        String contraseña = contra.getText().toString().toLowerCase();
        String cargo = carg.getText().toString().toLowerCase();
        //   String cargo = carg.getText().toString().toLowerCase();
        CollectionReference persons = db.collection("BD-Personal");
        correoinfo = correo;

        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("apellido", apellido);
        user.put("correo", correo);
        user.put("contraseña", contraseña);
        user.put("cargo", cargo);


        persons.document(corr.getText().toString().toLowerCase()).set(user);
        guardardatosQRCheckIn();
        EliminarUsuario();

    }

    public void guardardatosQRCheckIn() {
        String nombre = nom.getText().toString().toLowerCase();
        String apellido = apell.getText().toString().toLowerCase();
        String correo = corr.getText().toString().toLowerCase();

        //   String cargo = carg.getText().toString().toLowerCase();
        CollectionReference persons = db.collection("QRCheckIn");
        correoinfo = correo;

        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("apellido", apellido);
        user.put("correo", correo);
        user.put("llegada", "");
        user.put("comidainicio", "");
        user.put("comidafin", "");
        user.put("salida", "");

        persons.document(corr.getText().toString().toLowerCase()).set(user);
        guardardatosBD();
        EliminarUsuario();

    }
    public void guardardatosBD() {
        String nombre = nom.getText().toString().toLowerCase();
        String apellido = apell.getText().toString().toLowerCase();
        String correo = corr.getText().toString().toLowerCase();

        //   String cargo = carg.getText().toString().toLowerCase();
        CollectionReference persons = db.collection("BD-Dia");
        correoinfo = correo;

        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("apellido", apellido);
        user.put("correo", correo);
        user.put("llegadad", "");
        user.put("comidainiciod", "");
        user.put("comidafind", "");
        user.put("salidad", "");

        persons.document(corr.getText().toString().toLowerCase()).set(user);

    }

    private void EliminarUsuario() {


        CollectionReference userCol2 = db.collection("BD-Solicitudes");
        userCol2.document(corr.getText().toString().toLowerCase()).delete();


    }
}