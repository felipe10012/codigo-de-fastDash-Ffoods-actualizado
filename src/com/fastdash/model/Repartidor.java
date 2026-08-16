package com.fastdash.model;

public class Repartidor {

    private int idRepartidor;
    private int idRestaurante;
    private String nombre;
    private String telefono;
    private String vehiculo;

    public Repartidor() {
    }

    public Repartidor(int idRepartidor, int idRestaurante, String nombre, String telefono, String vehiculo) {
        this.idRepartidor = idRepartidor;
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.telefono = telefono;
        this.vehiculo = vehiculo;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public String toString() {
        return "Repartidor{"
                + "idRepartidor=" + idRepartidor
                + ", idRestaurante=" + idRestaurante
                + ", nombre='" + nombre + '\''
                + ", telefono='" + telefono + '\''
                + ", vehiculo='" + vehiculo + '\''
                + '}';
    }
}
