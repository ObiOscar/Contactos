package com.contactos.movil.computacion.uss.contactos.Modelo;

import android.widget.ImageView;

/**
 * Created by Oscar on 29/01/2018.
 */
public class Contacto {
    public  int id;
    public String nombre;
    public String telefono;
    public String email;
    public ImageView imagen;

    public Contacto() {

    }

    public Contacto(String email, String telefono, String nombre,ImageView imagen, int id) {
        this.email = email;
        this.telefono = telefono;
        this.nombre = nombre;
        this.imagen = imagen;
        this.id = id;
    }

    public Contacto(String nombre, String telefono, String email,ImageView imagen) {
        super();
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public ImageView getImagen(){return imagen; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImagen(ImageView imagen){this.imagen=imagen;}

    @Override
    public String toString() {
        return this.nombre;
    }
}
