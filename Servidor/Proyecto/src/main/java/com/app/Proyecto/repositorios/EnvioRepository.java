package com.app.Proyecto.repositorios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Proyecto.modelos.*;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
	// Buscar envíos según su fecha de entrega
	List<Envio> findAllByFechaEntrega(LocalDate fechaEntrega);

	// Buscar envíos según su estado
	List<Envio> findAllByEstadoId(Long estado_id);

	// Buscar envíos según el repartidor
	List<Envio> findAllByRepartidorId(Long repartidorId);

	// Buscar envíos según el repartidor y el estado
	List<Envio> findAllByRepartidorAndEstado(Long repartidorId, Long estadoId);

	// Buscar envíos según el administrador asignado
	List<Envio> findAllByAdministradorId(Long administradorId);

	// Buscar envíos según el cliente receptor
	List<Envio> findAllByClienteId(Long clienteId);

	// Buscar envíos según su fecha de modicación
	List<Envio> findAllByFechaModificacion(LocalDate today);

	// Buscar envíos según el repartidor y el cliente receptor
	List<Envio> findAllByRepartidorAndEstado(Optional<Repartidor> repartidor, Optional<Estado> estado);

	// Buscar envíos según el repartidor y el cliente receptor
	List<Envio> findAllByRepartidorAndCliente(Optional<Repartidor> repartidor, Optional<Cliente> cliente);

}