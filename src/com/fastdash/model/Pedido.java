package com.fastdash.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Pedido {

    private int idPedido;
    private int idUsuario;
    private Timestamp fechaPedido;
    private BigDecimal total;
    private String estado;

    public Pedido() {
    }

    public Pedido(int idPedido, int idUsuario, Timestamp fechaPedido, BigDecimal total, String estado) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.fechaPedido = fechaPedido;
        this.total = total;
        this.estado = estado;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Timestamp getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pedido{"
                + "idPedido=" + idPedido
                + ", idUsuario=" + idUsuario
                + ", fechaPedido=" + fechaPedido
                + ", total=" + total
                + ", estado='" + estado + '\''
                + '}';
    }
}
