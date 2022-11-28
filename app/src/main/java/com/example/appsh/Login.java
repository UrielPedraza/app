package com.example.appsh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText user, pass;
    Button btningresar, btnregistro;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "Firebase";
    public String bateri, reci, fechacortada, corr;
public String  nombre, apellido ,correo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.txt_nombrePersona);
        pass = findViewById(R.id.txt_passwordPersona);
        btningresar = findViewById(R.id.btningresar);
        btnregistro = findViewById(R.id.Registro);
        System.out.println("Entra login ----------------------------------------------------------");


        Hora();
        listar();
        BroadcastReceiver broadcastReceiverBattery = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Integer integerBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                String bateria = integerBatteryLevel.toString();
                System.out.println("Estado de la bateria =-------------------------------------------------> " + bateria);
                bateri = bateria;
            }
        };
        registerReceiver(broadcastReceiverBattery, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle envia = new Bundle();
                envia.putString("UsuarioOn", reci);
                envia.putString("BateriaBat", bateri);
                System.out.println("-----------------Login--------------------------" + reci);
                System.out.println("----------------------Login---------------------" + bateri);

                entrar();


            }
        });


    }

    public void entrar() {
        String usuario = user.getText().toString().toLowerCase();
        String contraseña = pass.getText().toString().toLowerCase();
        System.out.println("Metodo entrar---------------------------------------------------------------------");
        if (usuario.equals("") || contraseña.equals("")) {

            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        } else {


            {
                DocumentReference docRef = db.collection("BD-Personal").document(usuario.toLowerCase());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists() || usuario.equals("")) {
                                String contrase = document.get("contraseña").toString();
                                String carg = document.get("cargo").toString();
                                if (contraseña.equals(contrase.toLowerCase()) || contraseña.equals("")) {


                                    if (carg.toLowerCase().equals("administrador")) {
                                        Bundle envia = new Bundle();
                                        envia.putString("UsuarioOn", user.getText().toString().toLowerCase());
                                        Intent m = new Intent(Login.this, MenuAdministrador.class);
                                        m.putExtras(envia);
                                        startActivity(m);
                                        Toast.makeText(Login.this, "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();

                                    } else if (carg.toLowerCase().equals("operador")) {
                                        Bundle envia = new Bundle();
                                        envia.putString("UsuarioOn", user.getText().toString().toLowerCase());
                                        envia.putString("fecha", fechacortada.toString().toLowerCase());
                                        System.out.println("-------------------------------Bienvendo-envia la fecha ess---------------------- " + fechacortada);

                                        Intent o = new Intent(Login.this, MenuOperador.class);
                                        System.out.println("-------------------------------Bienvendo----------------------- " + user);
                                        o.putExtras(envia);
                                        startActivity(o);
                                        Toast.makeText(Login.this, "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();

                                    } else if (carg.toLowerCase().equals("supervisor")) {
                                        Bundle envia = new Bundle();
                                        envia.putString("UsuarioOn", user.getText().toString().toLowerCase());
                                        Intent o = new Intent(Login.this, MenuSupervisor.class);
                                        System.out.println("-------------------------------Bienvendo----------------------- " + user);
                                        o.putExtras(envia);
                                        startActivity(o);
                                        Toast.makeText(Login.this, "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Toast.makeText(Login.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();

                                }
                            } else {

                                Toast.makeText(Login.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }

    }


    public void Registra(View view) {
        Intent e = new Intent(this, RegistroNuevoUsuario.class);

        Bundle envia = new Bundle();
        envia.putString("UsuarioOn", reci);
        envia.putString("BateriaBat", bateri);
        e.putExtras(envia);
        startActivity(e);
    }

    private void Hora() {
        Calendar fecha = Calendar.getInstance();

        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int mes = fecha.get(Calendar.MONTH);
        int ano = fecha.get(Calendar.YEAR);
        mes++;
        fechacortada = dia + "-" + mes + "-" + ano;
        Bundle envia = new Bundle();
        envia.putString("fecha", fechacortada);
        System.out.println("..........----------------------..........................................fehcaaaaaaaaaaaaa " + fechacortada);
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


                                System.out.println("1=----------------> " + u.getnombre());
                                System.out.println("1=----------------> " + u.getapellidos());
                                System.out.println("1=----------------> " + u.getcorreo());
                                System.out.println("1=----------------> " + u.getcontraseña());
                                System.out.println("1=----------------> " + u.getcargo());

                                corr = u.getcorreo().toString();
                                if (!u.getcargo().equals("")) {

                                     nombre = u.getnombre().toString().toLowerCase();
                                      apellido = u.getapellidos().toString().toLowerCase();
                                    correo = u.getcorreo().toString().toLowerCase();
                                    String contraseña = u.getcontraseña().toString().toLowerCase();
                                    String cargo = u.getcargo().toString().toLowerCase();
                                    //   String cargo = carg.getText().toString().toLowerCase();
                                    CollectionReference persons = db.collection("BD-Personal");


                                    Map<String, Object> user = new HashMap<>();
                                    user.put("nombre", nombre);
                                    user.put("apellido", apellido);
                                    user.put("correo", correo);
                                    user.put("contraseña", contraseña);
                                    user.put("cargo", cargo);


                                    persons.document(corr).set(user);
                                    guardardatosQRCheckIn();
                                    EliminarUsuario();


                                }
                            }
                        } else {
                            Log.d(TAG, "Error en datos", task.getException());
                        }
                    }
                });


    }
    public void guardardatosQRCheckIn() {


        //   String cargo = carg.getText().toString().toLowerCase();
        CollectionReference persons = db.collection("QRCheckIn");


        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("apellido", apellido);
        user.put("correo", correo);
        user.put("llegada", "");
        user.put("comidainicio", "");
        user.put("comidafin", "");
        user.put("salida", "");

        persons.document(corr ).set(user);
        guardardatosBD();
        EliminarUsuario();

    }
    public void guardardatosBD() {


        //   String cargo = carg.getText().toString().toLowerCase();
        CollectionReference persons = db.collection("BD-Dia");


        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("apellido", apellido);
        user.put("correo", correo);
        user.put("llegadad", "");
        user.put("comidainiciod", "");
        user.put("comidafind", "");
        user.put("salidad", "");

        persons.document(corr ).set(user);

    }

    private void EliminarUsuario() {


        CollectionReference userCol2 = db.collection("BD-Solicitudes");
        userCol2.document(corr).delete();


    }


}
