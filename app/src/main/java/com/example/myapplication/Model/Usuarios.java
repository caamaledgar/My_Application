package com.example.myapplication.Model;

public class Usuarios {
    String uid;
    String nombre;
    String correo;
    String imagen;

    public Usuarios() {
    }

    public Usuarios(String uid, String nombre, String correo, String imagen) {
        this.uid = uid;
        this.nombre = nombre;
        this.correo = correo;
        this.imagen = imagen;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


}
