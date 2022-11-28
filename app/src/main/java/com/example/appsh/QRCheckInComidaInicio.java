package com.example.appsh;

public class QRCheckInComidaInicio {

    public String llegada  ;
    public String salida;
    public String comidainicio;
    public String comidafin;

    public QRCheckInComidaInicio( ) {

    }

    @Override
    public String toString() {
        return
                comidainicio ;
    }

    public String getLlegada() {
        return llegada;
    }

    public void setLlegada(String llegada) {
        this.llegada = llegada;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getComidainicio() {
        return comidainicio;
    }

    public void setComidainicio(String comidainicio) {
        this.comidainicio = comidainicio;
    }

    public String getComidafin() {
        return comidafin;
    }

    public void setComidafin(String comidafin) {
        this.comidafin = comidafin;
    }
}
