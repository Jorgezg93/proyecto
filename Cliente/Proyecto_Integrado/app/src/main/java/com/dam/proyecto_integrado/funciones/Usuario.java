package com.dam.proyecto_integrado.funciones;

public class Usuario {
    //Atributos
    private int id;
    private String email;
    private String contraseña;
    private String nombre;
    private int rol;

    //Constructor por defecto
    public Usuario() {
    }

    //Constructor con parámetros
    public Usuario(int id, String email, String contraseña, int rol, String nombre) {
        this.id = id;
        this.email = email;
        this.contraseña = contraseña;
        this.rol = rol;
        this.nombre = nombre;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
}