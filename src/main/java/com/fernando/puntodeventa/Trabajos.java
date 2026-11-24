package com.fernando.puntodeventa;

public class Trabajos {
    private int id_job;
    private String name_job;


    public Trabajos(int id_job, String name_job) {
        this.id_job = id_job;
        this.name_job=name_job;

    }

    public int getId_job(){return id_job;};

    public String getName_job(){return name_job;}
}
