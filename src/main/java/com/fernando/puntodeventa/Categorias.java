package com.fernando.puntodeventa;

public class Categorias {
    private int id_category;
    private String name_category;

    public Categorias(int id_category, String name_category){
        this.id_category=id_category;
        this.name_category = name_category;
    }
    public int getId_category(){ return id_category;}
    public String getName_category() {return  name_category;}
}
