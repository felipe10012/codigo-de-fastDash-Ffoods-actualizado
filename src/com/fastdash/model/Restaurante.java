package com.fastdash.model;

public class Restaurante {

    private int idRestaurante;
    private String nombre;
    private String direccion;
    private String telefono;

    public Restaurante() {
    }

    public Restaurante(int idRestaurante, String nombre, String direccion, String telefono) {
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Restaurante{"
                + "idRestaurante=" + idRestaurante
                + ", nombre='" + nombre + '\''
                + ", direccion='" + direccion + '\''
                + ", telefono='" + telefono + '\''
                + '}';
    }
}
