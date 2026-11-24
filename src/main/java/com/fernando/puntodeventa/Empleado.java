package com.fernando.puntodeventa;

public class Empleado {
    private int id;
    private int idPuesto;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String fecha;
    private String contraseña;
    private String disponibilidad;

    // Constructor (El creador del molde)
    public Empleado(int id, int idPuesto, String nombre, String primerApellido, String segundoApellido, String fecha, String contraseña, String disponibilidad) {
        this.id = id;
        this.idPuesto = idPuesto;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.fecha = fecha;
        this.contraseña = contraseña;
        this.disponibilidad = disponibilidad;
    }

    // GETTERS (Las llaves que usa la tabla para sacar los datos)
    public int getId() { return id; }
    public int getIdPuesto() { return idPuesto; }
    public String getNombre() { return nombre; }
    public String getPrimerApellido() { return primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public String getFecha() { return fecha; }
    public String getContraseña() {return contraseña;}
    public String getDisponibilidad() {return disponibilidad;}
}
