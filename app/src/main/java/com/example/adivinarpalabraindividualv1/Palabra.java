package com.example.adivinarpalabraindividualv1;

import java.io.Serializable;

public class Palabra implements Serializable {

    private String nombre, descripcion;

    public Palabra() {
    }

    public Palabra(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Palabra: " + nombre;
    }
}

