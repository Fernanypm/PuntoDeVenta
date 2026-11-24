package com.fernando.puntodeventa;

public class Provedor {
    private int id_proveedor;
    private String nombre_provedor;
    private  int contacto;

    public Provedor(int id_proveedor,String nombre_provedor, int contacto){
        this.id_proveedor = id_proveedor;
        this.nombre_provedor = nombre_provedor;
        this.contacto= contacto;
    }
    public  int getId_proveedor() { return id_proveedor;}
    public String getNombre_provedor(){ return  nombre_provedor;}
    public int getContacto(){ return contacto;}

}

