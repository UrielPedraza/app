
package com.example.appsh;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Date;

public class MenuSupervisor extends AppCompatActivity {


    public String Hrs;

    TextView resultxt, resultxt2;
    public String aux1, aux2, aux3, horacortada, fechacortada, qrasis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menusupervisor);

        tiempo tiempo = new tiempo();
        tiempo.execute();

        ImageView viewIm = findViewById(R.id.imageview);
        resultxt = findViewById(R.id.resultadotxt);
        resultxt2 = findViewById(R.id.resultadotxt2);

    }

    private void Hora() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        int dia = today.monthDay;
        int mes = today.month;
        int ano = today.year;

        mes = mes + 1;

        aux1 = new Date().toString();

        horacortada = aux1.substring(11, 19);
        fechacortada = dia + "-" + mes + "-" + ano;



        resultxt.setText(horacortada);
        resultxt2.setText(fechacortada);
        qrasis = horacortada + "-" + fechacortada;

    }

    public void lecto() {
        ImageView viewIm = findViewById(R.id.imageview);

        try {
            BarcodeEncoder bardcodeencoder = new BarcodeEncoder();


            Bitmap bitmap = bardcodeencoder.encodeBitmap(qrasis, BarcodeFormat.QR_CODE, 300, 350);

            viewIm.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hilo() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void ejecutar() {
        tiempo tiempo = new tiempo();
        tiempo.execute();
    }

    public class tiempo extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i = 1; i <= 1; i++) {
                hilo();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ejecutar();
            Hora();
            lecto();
            }


    }


}