package com.vinosmza.bebidas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre, imagen, bodega, varietal, categoria;
    private double precio;

    public Producto() {}

    public Producto(String nombre, String imagen, String bodega, String varietal, String categoria, double precio) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.bodega = bodega;
        this.varietal = varietal;
        this.categoria = categoria;
        this.precio = precio;
    }

    public Producto(String nombre, double precio, String bodega, String varietal, String categoria) {
        this.nombre = nombre;
        this.bodega = bodega;
        this.varietal = varietal;
        this.categoria = categoria;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getBodega() {
        return bodega;
    }

    public void setBodega(String bodega) {
        this.bodega = bodega;
    }

    public String getVarietal() {
        return varietal;
    }

    public void setVarietal(String varietal) {
        this.varietal = varietal;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
