package com.example.appsh;

public class BDPendientes {

    public String uid;
    public String nombre;
    public String apellidos;
    public String correo;
    public String contraseña;

    public String cargo;



    public BDPendientes() {
    }

    public BDPendientes(String uid, String nombre, String apellidos, String correo, String contraseña , String cargo ) {
        this.uid = uid;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contraseña = contraseña;

        this.cargo =cargo;


    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getnombre() {
        return nombre;
    }

    public void setnombre(String nombre) {
        this.nombre = nombre;
    }

    public String getapellidos() {
        return apellidos;
    }

    public void setapellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getcorreo() {
        return correo;
    }

    public void setcorreo(String correo) {
        this.correo = correo;
    }

    public String getcontraseña() {
        return contraseña;
    }

    public void setcontraseña(String contraseña) {
        this.contraseña = contraseña;
    }


    public String getcargo() {return cargo;}
    public void setcargo(String cargo) {
        this.cargo = cargo;}



    @Override
    public String toString() {
        return "Usuario: "+nombre +" "+ apellidos+ "\t   Cargo: "+cargo  ;
    }
}

