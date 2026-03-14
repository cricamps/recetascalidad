package com.duoc.recetas.model;

import java.util.List;

public class Receta {

    private int id;
    private String nombre;
    private String tipoCocina;
    private String paisOrigen;
    private String dificultad;
    private int tiempoCoccion; // minutos
    private String descripcionCorta;
    private String descripcionLarga;
    private List<String> ingredientes;
    private List<String> instrucciones;
    private String imagen;
    private boolean popular;

    public Receta() {}

    public Receta(int id, String nombre, String tipoCocina, String paisOrigen,
                  String dificultad, int tiempoCoccion, String descripcionCorta,
                  String descripcionLarga, List<String> ingredientes,
                  List<String> instrucciones, String imagen, boolean popular) {
        this.id = id;
        this.nombre = nombre;
        this.tipoCocina = tipoCocina;
        this.paisOrigen = paisOrigen;
        this.dificultad = dificultad;
        this.tiempoCoccion = tiempoCoccion;
        this.descripcionCorta = descripcionCorta;
        this.descripcionLarga = descripcionLarga;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.imagen = imagen;
        this.popular = popular;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoCocina() { return tipoCocina; }
    public void setTipoCocina(String tipoCocina) { this.tipoCocina = tipoCocina; }

    public String getPaisOrigen() { return paisOrigen; }
    public void setPaisOrigen(String paisOrigen) { this.paisOrigen = paisOrigen; }

    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }

    public int getTiempoCoccion() { return tiempoCoccion; }
    public void setTiempoCoccion(int tiempoCoccion) { this.tiempoCoccion = tiempoCoccion; }

    public String getDescripcionCorta() { return descripcionCorta; }
    public void setDescripcionCorta(String descripcionCorta) { this.descripcionCorta = descripcionCorta; }

    public String getDescripcionLarga() { return descripcionLarga; }
    public void setDescripcionLarga(String descripcionLarga) { this.descripcionLarga = descripcionLarga; }

    public List<String> getIngredientes() { return ingredientes; }
    public void setIngredientes(List<String> ingredientes) { this.ingredientes = ingredientes; }

    public List<String> getInstrucciones() { return instrucciones; }
    public void setInstrucciones(List<String> instrucciones) { this.instrucciones = instrucciones; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }
}
