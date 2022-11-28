package com.example.appsh;

public class QRCheckIn {

    public String llegada  ;
    public String salida;
    public String comidainicio;
    public String comidafin;
    public String nombre;
    public String apellido;
    public String correo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public QRCheckIn( ) {

    }

    @Override
    public String toString() {
        return
                llegada ;
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
