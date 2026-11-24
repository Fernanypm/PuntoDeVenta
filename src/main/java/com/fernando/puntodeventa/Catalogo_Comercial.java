package com.fernando.puntodeventa;

public class Catalogo_Comercial {
    private int id_producto;
    private String nombre_product;
    private int id_provedor;
    private int id_categoria;
    private double costo_u;
    private double precio_u;
    private double precio_c;
    private double IVA;

    public Catalogo_Comercial(int id_producto, String nombre_product, int id_provedor, int id_categoria,double costo_u,double precio_u,double precio_c,double IVA){
        this.id_producto = id_producto;
        this.nombre_product = nombre_product;
        this.id_provedor = id_provedor;
        this.id_categoria = id_categoria;
        this.costo_u = costo_u;
        this.precio_u = precio_u;
        this.precio_c = precio_c;
        this.IVA = IVA;
    }

    public int getId_producto() {
        return id_producto;
    }

    public String getNombre_product() {
        return nombre_product;
    }

    public int getId_provedor() {
        return id_provedor;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public double getCosto_u() {
        return costo_u;
    }

    public double getPrecio_u() {
        return precio_u;
    }

    public double getPrecio_c() {
        return precio_c;
    }

    public double getIVA() {
        return IVA;
    }
}
