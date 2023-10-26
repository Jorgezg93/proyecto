package com.app.Proyecto.modelos;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "repartidor")
public class Repartidor {
	// Atributos
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "apellidos")
	private String apellidos;

	@Column(name = "dni")
	private String dni;

	@Column(name = "telefono")
	private String telefono;

	@Column(name = "email")
	private String email;

	@Column(name = "latitud")
	private double latitud;

	@Column(name = "longitud")
	private double longitud;

	@Column(name = "fechaActualizacion")
	private LocalDateTime fechaHoraActualizacion;

	@Column(name = "password")
	private String password;

	@ManyToOne
	@JoinColumn(name = "rolId", referencedColumnName = "id")
	private Rol rol;

//Constructor vacío
	public Repartidor() {
	}

//Constructor con parámetros
	public Repartidor(String nombre, String apellidos, String dni, String telefono, String email, double latitud,
			double longitud, LocalDateTime fechaHoraActualizacion, String password, Rol rol) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.telefono = telefono;
		this.email = email;
		this.latitud = latitud;
		this.longitud = longitud;
		this.fechaHoraActualizacion = fechaHoraActualizacion;
		this.password = password;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public LocalDateTime getFechaHoraActualizacion() {
		return fechaHoraActualizacion;
	}

	public void setFechaHoraActualizacion(LocalDateTime fechaHoraActualizacion) {
		this.fechaHoraActualizacion = fechaHoraActualizacion;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}
