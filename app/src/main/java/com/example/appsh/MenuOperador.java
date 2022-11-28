package com.example.appsh;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuOperador extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private List<QRCheckIn> listCodigos = new ArrayList<>();
    private List<QRCheckInComidaInicio> listCalidad = new ArrayList<>();
    private List<QRCheckInComidaFin> listproduccion = new ArrayList<>();


    ArrayAdapter<QRCheckIn> codigosArrayAdapter;
    ArrayAdapter<QRCheckInComidaInicio> codigosArrayAdapterpro;
    ArrayAdapter<QRCheckInComidaFin> codigosArrayAdaptercali;
    String TAG = "Firebase";
    Button btnllegada, btncomidainicio, btncomidafin, btnsalida;
    public String reci, qrcode = "";
    EditText nom, apell, corr, contra, codi, carg;
    ListView listausuarios, listausuarios2, listausuarios3;
    QR userselect;
    public String bateri, fech;
    public String auxboton;
    public String llegadad;
    public String comidainiciod;
    public String comidafind;
    public String salidad, fechahoy, horastotal, fechahoy3, fechahoy4;

    public String conllegada, consalida;

    TextView llega, cominicio, comfin, sali;
    Pattern patron;
    Pattern patronp;
    Pattern patronc;

    Matcher matcher;

    Boolean resultadoA;

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    public String hrfinal, horallegada = "03:00:02", horasalida = "23:00:02";
    public int hrtotal = 0, mt = 0, aux = 0, he = Integer.parseInt(horallegada.substring(0, 2)), me = Integer.parseInt(horallegada.substring(3, 5)),
            hs = Integer.parseInt(horasalida.substring(0, 2)), ms = Integer.parseInt(horasalida.substring(3, 5));
    public boolean con = true;
    public String fecha = "11-11-2022", fechahoyo = "11-11-2022";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuoperador);


        btnllegada = findViewById(R.id.btnscanllegada);
        btncomidainicio = findViewById(R.id.btnscancomidainicio);
        btncomidafin = findViewById(R.id.btnscancomidafin);
        btnsalida = findViewById(R.id.btnscansalida);
        llega = findViewById(R.id.viewllegada);
        cominicio = findViewById(R.id.viewlcomidaincio);
        comfin = findViewById(R.id.viewcomidasalida);
        sali = findViewById(R.id.viewsalida);

        Bundle recibe = getIntent().getExtras();
        reci = recibe.getString("UsuarioOn");
        System.out.println("muestraesto```````````````` " + reci);
        bateri = recibe.getString("BateriaBat");
        Bundle recibefecha = getIntent().getExtras();
        fech = recibefecha.getString("fecha");
        System.out.println("fechaa``````````````` " + fech);


        System.out.println("(2-----)Usuario: " + recibe + " ----------------------------------------------------------Bateria= " + bateri);
        mostrarhora();
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

        BateriaUpdate();
        btnllegada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrador1 = new IntentIntegrator(MenuOperador.this);
                integrador1.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador1.setPrompt("lector Funcionado");
                integrador1.setCameraId(0);
                integrador1.setBeepEnabled(true);
                integrador1.setBarcodeImageEnabled(true);
                auxboton = "i";


                integrador1.initiateScan();


            }
        });


        btncomidainicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrador3 = new IntentIntegrator(MenuOperador.this);
                integrador3.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador3.setPrompt("lector Funcionado");
                integrador3.setCameraId(0);
                integrador3.setBeepEnabled(true);
                integrador3.setBarcodeImageEnabled(true);
                auxboton = "ci";

                integrador3.initiateScan();


            }
        });


        btncomidafin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrador2 = new IntentIntegrator(MenuOperador.this);
                integrador2.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador2.setPrompt("lector Funcionado");
                integrador2.setCameraId(0);
                integrador2.setBeepEnabled(true);
                integrador2.setBarcodeImageEnabled(true);
                auxboton = "cf";

                integrador2.initiateScan();


            }
        });

        btnsalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrador2 = new IntentIntegrator(MenuOperador.this);
                integrador2.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador2.setPrompt("lector Funcionado");
                integrador2.setCameraId(0);
                integrador2.setBeepEnabled(true);
                integrador2.setBarcodeImageEnabled(true);
                auxboton = "f";

                integrador2.initiateScan();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        qrcode = result.getContents().toString();
        if (result != null) {
            if (result.getContents() == null) {
                System.out.println("Lectrua cancela--------------------------->");
                Toast.makeText(this, "Lectura Cancelada", Toast.LENGTH_LONG).show();
            }


            System.out.println("Lectrua correcta--------------------------->");

            entrar();
        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    public void agregaasistencia() {
        DocumentReference docRef = db.collection("QRCheckIn").document(reci);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //Pattern patron = Pattern.compile("A-+[0-9]+");
                    switch (auxboton) {
                        case "i":
                            patron = Pattern.compile("[0-9]+:+[0-9]+:+[0-9]+-+[0-9]+-+[0-9]+-+[0-9]+");
                            Matcher matcher = patron.matcher(qrcode);

                            Boolean resultadoA = matcher.matches();

                            System.out.println("cumple con los requisistos ->>>>>>>>>" + resultadoA);

                            if (resultadoA == true) {
                                System.out.println("valor dentro -------------------------> " + qrcode);


                                llegadad = qrcode;

                                fechahoy = qrcode.substring(9, 19);
                                conllegada = qrcode.substring(9, 19);
                                diavalido();
                                Toast.makeText(MenuOperador.this, "QR Agregado", Toast.LENGTH_SHORT).show();
                                listCodigos.clear();


                                System.out.println("---------------------------El QR se grego--------------------");
                            }
                            break;

                        case "ci":
                            patron = Pattern.compile("[0-9]+:+[0-9]+:+[0-9]+-+[0-9]+-+[0-9]+-+[0-9]+");
                            matcher = patron.matcher(qrcode);

                            resultadoA = matcher.matches();

                            System.out.println("cumple con los requisistos ->>>>>>>>>" + resultadoA);

                            if (resultadoA == true) {
                                System.out.println("valor dentro -------------------------> " + qrcode);

                                comidainiciod = qrcode;

                                fechahoy = qrcode.substring(9, 19);

                                diavalidocoi();

                                Toast.makeText(MenuOperador.this, "QR Agregado", Toast.LENGTH_SHORT).show();
                                listCodigos.clear();


                                System.out.println("---------------------------El QR se grego--------------------");
                            }
                            break;

                        case "cf":
                            patron = Pattern.compile("[0-9]+:+[0-9]+:+[0-9]+-+[0-9]+-+[0-9]+-+[0-9]+");
                            matcher = patron.matcher(qrcode);

                            resultadoA = matcher.matches();

                            System.out.println("cumple con los requisistos ->>>>>>>>>" + resultadoA);

                            if (resultadoA == true) {
                                System.out.println("valor dentro -------------------------> " + qrcode);

                                comidafind = qrcode;

                                fechahoy = qrcode.substring(9, 19);
                                diavalidocof();
                                Toast.makeText(MenuOperador.this, "QR Agregado", Toast.LENGTH_SHORT).show();
                                listCodigos.clear();


                                System.out.println("---------------------------El QR se grego--------------------");
                            }
                            break;

                        case "f":
                            patron = Pattern.compile("[0-9]+:+[0-9]+:+[0-9]+-+[0-9]+-+[0-9]+-+[0-9]+");
                            matcher = patron.matcher(qrcode);

                            resultadoA = matcher.matches();

                            System.out.println("cumple con los requisistos ->>>>>>>>>" + resultadoA);

                            if (resultadoA == true) {
                                System.out.println("valor dentro -------------------------> " + qrcode);

                                salidad = qrcode;

                                fechahoy = qrcode.substring(9, 19);
                                diavalidosa();
                                Toast.makeText(MenuOperador.this, "QR Agregado", Toast.LENGTH_SHORT).show();
                                listCodigos.clear();


                                System.out.println("---------------------------El QR se grego--------------------");
                            }
                            break;
                    }


                } else {
                    Toast.makeText(MenuOperador.this, "QR No Valido", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void entrar() {
        {
            DocumentReference docRef = db.collection("BD-Personal").document(qrcode);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        System.out.println("qr---------------------------------" + qrcode);


                        if (document.exists()) {
                            String nombre = document.get("Usuario").toString();
                            Toast.makeText(MenuOperador.this, "El codigo " + qrcode + " ya fue registrado por " + nombre, Toast.LENGTH_LONG).show();
                            actualiza();
                        } else {
                            guardardatos();
                            agregaasistencia();
                            actualiza();
                        }
                    }
                }
            });
        }

    }


    public void guardardatos() {
        CollectionReference persons = db.collection("CodigosQR");


        Map<String, Object> user = new HashMap<>();

        user.put("Usuario", reci.toLowerCase());
        //String dt = nom.getText().toString()+apell.getText().toString()+corr.getText().toString();
        persons.document(qrcode).set(user);
        listCodigos.clear();

        Toast.makeText(this, "QR Guardado", Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainlector, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.icon_actualizar:

                actualiza();
                Toast.makeText(this, "Datos Actualizados", Toast.LENGTH_SHORT).show();


                break;

        }
        return true;
    }

    private void actualiza() {
        listCodigos.clear();
        listproduccion.clear();
        listCalidad.clear();

    }

    public void BateriaUpdate() {
        DocumentReference docRef = db.collection("BD-Personal").document(reci);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();


                    if (document.exists()) {
                        String nombre = document.get("nombre").toString();
                        String apellido = document.get("apellido").toString();
                        String correo = document.get("correo").toString();
                        String contra = document.get("contraseña").toString();
                        String cargo = document.get("cargo").toString();


                        CollectionReference cities = db.collection("BD-Personal");

                        Map<String, Object> data1 = new HashMap<>();
                        data1.put("nombre", nombre);
                        data1.put("apellido", apellido);
                        data1.put("correo", correo);
                        data1.put("contraseña", contra);

                        data1.put("cargo", cargo);
                        data1.put("bateria", bateri);
                        System.out.println("baeria dentro de------" + bateri + "...........");

                        cities.document(reci).set(data1);


                    } else {
                    }

                }
            }
        });
    }


    public void diavalido() {
        DocumentReference docRef = db.collection("BD-Dia").document(reci);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    mostrarhora();
                    if (document.exists()) {
                        String nombre = document.get("nombre").toString();
                        String apellido = document.get("apellido").toString();
                        String correo = document.get("correo").toString();
                        String lle = document.get("llegadad").toString();
                        String coi = document.get("comidainiciod").toString();
                        String cof = document.get("comidafind").toString();
                        String sa = document.get("salidad").toString();
                        if (lle == "") {
                            CollectionReference cities = db.collection("BD-Dia");

                            Map<String, Object> data1 = new HashMap<>();
                            data1.put("nombre", nombre);
                            data1.put("apellido", apellido);
                            data1.put("correo", correo);
                            data1.put("llegadad", llegadad);

                            data1.put("comidainiciod", coi);
                            data1.put("comidafind", cof);
                            data1.put("salidad", sa);

                            cities.document(reci).set(data1);

                            DocumentReference washingtonRef = db.collection("QRCheckIn").document(reci);
                            washingtonRef.update("llegada", FieldValue.arrayUnion(qrcode + "*"));

                            Toast.makeText(MenuOperador.this, "QR registrado  ", Toast.LENGTH_SHORT).show();
                            llega.setText(qrcode.substring(0, 8));
                        } else {

                            fechahoy = lle.substring(9, 19);

                            System.out.println("usuario es ==========------>>>>>>>>> " + reci);
                            System.out.println("fechas----->>>>>> " + fechahoy + "  " + fech);
                            if (fech.length() == 9) {
                                fech = 0 + fech;
                            }
                            System.out.println("queda a si peroooo  --------------" + fech);
                            if (fechahoy.equals(fech)) {

                                System.out.println("Ya has escaneado un QR");
                                Toast.makeText(MenuOperador.this, "Ya has escaneado un QR", Toast.LENGTH_LONG).show();
                            } else {
                                CollectionReference cities = db.collection("BD-Dia");

                                Map<String, Object> data1 = new HashMap<>();
                                data1.put("nombre", nombre);
                                data1.put("apellido", apellido);
                                data1.put("correo", correo);
                                data1.put("llegadad", llegadad);

                                data1.put("comidainiciod", coi);
                                data1.put("comidafind", cof);
                                data1.put("salidad", sa);

                                cities.document(reci).set(data1);

                                DocumentReference washingtonRef = db.collection("QRCheckIn").document(reci);
                                washingtonRef.update("llegada", FieldValue.arrayUnion(qrcode + "*"));

                                Toast.makeText(MenuOperador.this, "QR registrado  ", Toast.LENGTH_SHORT).show();
                                llega.setText(qrcode.substring(0, 8));
                            }
                        }
                    } else {

                        Toast.makeText(MenuOperador.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void diavalidocoi() {
        DocumentReference docRef = db.collection("BD-Dia").document(reci);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    mostrarhora();
                    if (document.exists()) {
                        String nombre = document.get("nombre").toString();
                        String apellido = document.get("apellido").toString();
                        String correo = document.get("correo").toString();
                        String lle = document.get("llegadad").toString();
                        String coi = document.get("comidainiciod").toString();
                        String cof = document.get("comidafind").toString();
                        String sa = document.get("salidad").toString();

                        if (coi == "") {
                            CollectionReference cities = db.collection("BD-Dia");

                            Map<String, Object> data1 = new HashMap<>();
                            data1.put("nombre", nombre);
                            data1.put("apellido", apellido);
                            data1.put("correo", correo);
                            data1.put("llegadad", lle);
                            data1.put("comidainiciod", comidainiciod);

                            data1.put("comidafind", cof);
                            data1.put("salidad", sa);
                            cities.document(reci).set(data1);
                            DocumentReference washingtonRef = db.collection("QRCheckIn").document(reci);
                            washingtonRef.update("comidainicio", FieldValue.arrayUnion(qrcode + "*"));
                            Toast.makeText(MenuOperador.this, "QR registrado  ", Toast.LENGTH_SHORT).show();
                            cominicio.setText(qrcode.substring(0, 8));
                        } else {
                            if (fech.length() == 9) {
                                fech = 0 + fech;
                            }
                            fechahoy = coi.substring(9, 19);

                            System.out.println("usuario es ==========------>>>>>>>>> " + reci);
                            System.out.println("fechas----->>>>>> " + fechahoy + "  " + fech);
                            if (fechahoy.equals(fech)) {

                                System.out.println("Ya has escaneado un QR");
                                Toast.makeText(MenuOperador.this, "Ya has escaneado un QR", Toast.LENGTH_LONG).show();
                            } else {
                                CollectionReference cities = db.collection("BD-Dia");

                                Map<String, Object> data1 = new HashMap<>();
                                data1.put("nombre", nombre);
                                data1.put("apellido", apellido);
                                data1.put("correo", correo);
                                data1.put("llegadad", lle);
                                data1.put("comidainiciod", comidainiciod);
                                data1.put("comidafind", cof);
                                data1.put("salidad", sa);

                                cities.document(reci).set(data1);
                                DocumentReference washingtonRef = db.collection("QRCheckIn").document(reci);
                                washingtonRef.update("comidainicio", FieldValue.arrayUnion(qrcode + "*"));

                                Toast.makeText(MenuOperador.this, "QR registrado  ", Toast.LENGTH_SHORT).show();
                                cominicio.setText(qrcode.substring(0, 8));
                            }
                        }
                    } else {

                        Toast.makeText(MenuOperador.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void diavalidocof() {
        DocumentReference docRef = db.collection("BD-Dia").document(reci);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    mostrarhora();
                    if (document.exists()) {
                        String nombre = document.get("nombre").toString();
                        String apellido = document.get("apellido").toString();
                        String correo = document.get("correo").toString();
                        String lle = document.get("llegadad").toString();
                        String coi = document.get("comidainiciod").toString();
                        String cof = document.get("comidafind").toString();
                        String sa = document.get("salidad").toString();
                        if (cof == "") {
                            CollectionReference cities = db.collection("BD-Dia");

                            Map<String, Object> data1 = new HashMap<>();
                            data1.put("nombre", nombre);
                            data1.put("apellido", apellido);
                            data1.put("correo", correo);
                            data1.put("llegadad", lle);
                            data1.put("comidainiciod", coi);
                            data1.put("comidafind", comidafind);

                            data1.put("salidad", sa);

                            cities.document(reci).set(data1);
                            DocumentReference washingtonRef = db.collection("QRCheckIn").document(reci);
                            washingtonRef.update("comidafin", FieldValue.arrayUnion(qrcode + "*"));


                            Toast.makeText(MenuOperador.this, "Codigo QR registrado ", Toast.LENGTH_LONG).show();
                            comfin.setText(qrcode.substring(0, 8));
                        } else {
                            if (fech.length() == 9) {
                                fech = 0 + fech;
                            }
                            fechahoy = cof.substring(9, 19);

                            System.out.println("Ya has escaneado un QR");
                            Toast.makeText(MenuOperador.this, "Ya has escaneado un QR", Toast.LENGTH_LONG).show();
                            if (fechahoy.equals(fech)) {

                                System.out.println("Ya has escaneado un QR");
                                Toast.makeText(MenuOperador.this, "Ya has escaneado un QR", Toast.LENGTH_LONG).show();
                            } else {
                                CollectionReference cities = db.collection("BD-Dia");

                                Map<String, Object> data1 = new HashMap<>();
                                data1.put("nombre", nombre);
                                data1.put("apellido", apellido);
                                data1.put("correo", correo);
                                data1.put("llegadad", lle);
                                data1.put("comidainiciod", coi);
                                data1.put("comidafind", comidafind);
                                data1.put("salidad", sa);


                                cities.document(reci).set(data1);
                                DocumentReference washingtonRef = db.collection("QRCheckIn").document(reci);
                                washingtonRef.update("comidafin", FieldValue.arrayUnion(qrcode + "*"));


                                Toast.makeText(MenuOperador.this, "Codigo QR registrado ", Toast.LENGTH_LONG).show();
                                comfin.setText(qrcode.substring(0, 8));
                            }
                        }
                    } else {

                        Toast.makeText(MenuOperador.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void diavalidosa() {
        DocumentReference docRef = db.collection("BD-Dia").document(reci);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {





                    DocumentSnapshot document = task.getResult();

                    mostrarhora();
                    if (document.exists()) {
                        String nombre = document.get("nombre").toString();
                        String apellido = document.get("apellido").toString();
                        String correo = document.get("correo").toString();
                        String lle = document.get("llegadad").toString();
                        String coi = document.get("comidainiciod").toString();
                        String cof = document.get("comidafind").toString();
                        String sa = document.get("salidad").toString();

                        if (sa == "") {
                            hora();
                            System.out.println("fech========"+fech+"fechoy======"+fechahoy);
                            CollectionReference cities = db.collection("BD-Dia");

                            Map<String, Object> data1 = new HashMap<>();
                            data1.put("nombre", nombre);
                            data1.put("apellido", apellido);
                            data1.put("correo", correo);
                            data1.put("llegadad", lle);
                            data1.put("comidainiciod", coi);
                            data1.put("comidafind", cof);
                            data1.put("salidad", salidad);
                            data1.put("horastotal", hrfinal);
                            System.out.println("Hrafinal======="+hrfinal);
                            cities.document(reci).set(data1);

                            DocumentReference washingtonRef = db.collection("QRCheckIn").document(reci);
                            washingtonRef.update("salida", FieldValue.arrayUnion(qrcode + "*"));

                            Toast.makeText(MenuOperador.this, "QR registrado ", Toast.LENGTH_SHORT).show();
                            sali.setText(qrcode.substring(0, 8));
                        } else {
                            if (fech.length() == 9) {
                                fech = 0 + fech;

                            }
                            fechahoy = sa.substring(9, 19);

                            System.out.println("usuario es ==========------>>>>>>>>> " + reci);
                            System.out.println("fechas----->>>>>> " + fechahoy + "  " + fech);
                            if (fechahoy.equals(fech)) {

                                System.out.println("Ya has escaneado un QR");
                                Toast.makeText(MenuOperador.this, "Ya has escaneado un QR", Toast.LENGTH_LONG).show();
                            } else {
                                hora();
                                CollectionReference cities = db.collection("BD-Dia");

                                Map<String, Object> data1 = new HashMap<>();
                                data1.put("nombre", nombre);
                                data1.put("apellido", apellido);
                                data1.put("correo", correo);
                                data1.put("llegadad", lle);
                                data1.put("comidainiciod", coi);
                                data1.put("comidafind", cof);
                                data1.put("salidad", salidad);

                                cities.document(reci).set(data1);

                                DocumentReference washingtonRef = db.collection("QRCheckIn").document(reci);
                                washingtonRef.update("salida", FieldValue.arrayUnion(qrcode + "*"));

                                Toast.makeText(MenuOperador.this, "QR registrado  ", Toast.LENGTH_LONG).show();
                                sali.setText(qrcode.substring(0, 8));



                                //----------------------------------HORAS TOTALES---------------------------------------------------------













                            }
                        }
                    } else {

                        Toast.makeText(MenuOperador.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }




    public void hora() {


        he= Integer.parseInt(llegadad.substring(0, 2));
        hs= Integer.parseInt(salidad.substring(0, 2));

        fecha=llegadad.substring(9,19);
        fechahoyo=salidad.substring(9,19);


        me = Integer.parseInt(llegadad.substring(3, 5));
        ms = Integer.parseInt(salidad.substring(3, 5));
        System.out.println( " aux " + aux + " he " + he + " hs " + hs+ "fech----------------------------------->"+fech+" fechahoy---------------------------->"+fechahoy);

        int inicio = he, fin = 0;


        if (inicio == 24) {
            inicio = 1;
            fin = 2;
            con = false;
            aux++;
            he = inicio;

        }
        while (he != hs && con == true) {
            he++;
            aux++;
            inicio++;
            fin = inicio + 1;

            if (inicio == 24 || inicio == 00) {
                inicio = 1;
                fin = 2;
                con = false;
                aux++;
                he = inicio;
                System.out.println("inicio " + inicio + " fin " + fin + " aux " + aux + " he " + he + " hs " + hs);
            }
        }

        while (he != hs && con == false) {
            he++;
            aux++;
            if (he == hs) {
                con = true;
            }
        }


        System.out.println(  fech+" fechahoy---------------------------->"+fechahoy);
        System.out.println(  fecha+" fechahoyooooo---------------------->"+fechahoyo);
        if (fecha.equals(fechahoyo)) {

        } else{
            aux = aux + 24;
            hrfinal=String.valueOf(aux);
            System.out.println("trabajo24 =-----> " + aux + "hrs");
            System.out.println(  fecha+" fechahoydentro---------------------------->"+fechahoyo);
        }

        //-----------------------Minutos--------------------------
        if (me == 00 && ms == 00) {
            hrfinal=aux + ":" + me + 0;
            System.out.println("trabajo sin minutos =-----> " + hrfinal);
        }

        if (me > ms) {
            System.out.println("me= " + me + " ms= " + ms);
            mt = me - ms;

            hrtotal = aux * 60;
            hrtotal = hrtotal - 60;
            mt = mt - 60;
            mt = mt * -1;
            hrtotal = hrtotal / 60;
            hrfinal = String.valueOf(hrtotal) + ":" + String.valueOf(mt);
            System.out.println("La hora final es > ----------------->  " + hrfinal);
        }

        if (me<ms) {
            mt=ms-me;
            hrfinal=aux+":"+mt;
            System.out.println("La hora final es < ----------------->  " +hrfinal);
        }

        System.out.println("trabajo fin=-----> " + aux + "hrs");
    }


    public void mostrarhora() {
        DocumentReference docRef = db.collection("BD-Dia").document(reci);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();


                    if (document.exists()) {

                        String lle = document.get("llegadad").toString();
                        String coi = document.get("comidainiciod").toString();
                        String cof = document.get("comidafind").toString();
                        String sa = document.get("salidad").toString();
                        if (lle != "") {

                            llega.setText(lle.substring(0, 8));

                        } else {
                            llega.setText("");
                        }


                        if (coi != "") {


                            cominicio.setText(coi.substring(0, 8));

                        } else {
                            cominicio.setText("");
                        }


                        if (cof != "") {


                            comfin.setText(cof.substring(0, 8));

                        } else {
                            comfin.setText("");
                        }


                        if (sa != "") {


                            sali.setText(sa.substring(0, 8));

                        } else {
                            sali.setText("");
                        }


                    }

                }

            }
        });
    }

}

