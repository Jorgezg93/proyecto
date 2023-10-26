package com.app.Proyecto.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {
	// Atributos
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "email")
	private String email;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "apellidos")
	private String apellidos;

	@Column(name = "dni")
	private String dni;

	@Column(name = "telefono")
	private String telefono;

	@Column(name = "direccion")
	private String direccion;

	@Column(name = "password")
	private String password;

	@ManyToOne
	@JoinColumn(name = "rol_id", referencedColumnName = "id")
	private Rol rol;

	// Constructor vacío
	public Cliente() {
	}

	// Constructor con parámetros
	public Cliente(String nombre, String apellidos, String dni, String telefono, String direccion, String password,
			String email, Rol rol) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.telefono = telefono;
		this.direccion = direccion;
		this.password = password;
		this.email = email;
		this.rol = rol;
	}

	// Getters y setters
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}