package com.app.Proyecto.modelos;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "envio")
public class Envio {
	// Atributos
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "origen")
	private String origen;

	@Column(name = "destino")
	private String destino;

	@ManyToOne
	@JoinColumn(name = "clienteId")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "repartidorId")
	private Repartidor repartidor;

	@ManyToOne
	@JoinColumn(name = "administradorId")
	private Administrador administrador;

	@Column(name = "fechaEntrega")
	private LocalDate fechaEntrega;

	@Column(name = "empresaReparto")
	private String empresaReparto;

	@ManyToOne
	@JoinColumn(name = "estadoId")
	private Estado estado;

	@Column(name = "fechaModificacion")
	private LocalDate fechaModificacion;

	// Constructor vacío
	public Envio() {
	}

	// Constructor con parámetros
	public Envio(String origen, String destino, Cliente cliente, Repartidor repartidor, Administrador administrador,
			LocalDate fechaEntrega, String empresaReparto, Estado estado, LocalDate fechaModificacion) {
		this.origen = origen;
		this.destino = destino;
		this.cliente = cliente;
		this.repartidor = repartidor;
		this.administrador = administrador;
		this.fechaEntrega = fechaEntrega;
		this.empresaReparto = empresaReparto;
		this.estado = estado;
		this.fechaModificacion = fechaModificacion;
	}

	// Getters y setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Repartidor getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}

	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}

	public LocalDate getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(LocalDate fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public String getEmpresaReparto() {
		return empresaReparto;
	}

	public void setEmpresaReparto(String empresaReparto) {
		this.empresaReparto = empresaReparto;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public LocalDate getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDate fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

}
