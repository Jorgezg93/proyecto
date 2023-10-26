package com.dam.proyecto_integrado.funciones;

//Clase que almacena una referencia al usuario a lo largo de toda la aplicación
public class UsuarioSingleton {
    private static UsuarioSingleton instance;
    private Usuario usuario;

    private UsuarioSingleton() {
    }

    public static UsuarioSingleton getInstance() {
        if (instance == null) {
            instance = new UsuarioSingleton();
        }
        return instance;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
