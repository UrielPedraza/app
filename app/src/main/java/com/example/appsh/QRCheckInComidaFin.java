package com.example.appsh;

public class QRCheckInComidaFin {

    public String llegada  ;
    public String salida;
    public String comidainicio;
    public String comidafin;

    public QRCheckInComidaFin( ) {

    }

    @Override
    public String toString() {
        return
                comidafin ;
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
