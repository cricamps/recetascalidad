package com.duoc.recetas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recetas")
public class RecetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_cocina", length = 50)
    private String tipoCocina;

    @Column(name = "pais_origen", length = 50)
    private String paisOrigen;

    @Column(length = 20)
    private String dificultad;

    @Column(name = "tiempo_coccion")
    private int tiempoCoccion;

    @Column(name = "descripcion_corta", length = 500)
    private String descripcionCorta;

    @Column(name = "descripcion_larga", columnDefinition = "TEXT")
    private String descripcionLarga;

    @Column(columnDefinition = "TEXT")
    private String ingredientes;

    @Column(columnDefinition = "TEXT")
    private String instrucciones;

    @Column(length = 100)
    private String imagen;

    private boolean popular;

    public RecetaEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getIngredientes() { return ingredientes; }
    public void setIngredientes(String ingredientes) { this.ingredientes = ingredientes; }
    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }
}
