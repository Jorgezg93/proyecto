package com.app.Proyecto.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.Proyecto.modelos.*;
import com.app.Proyecto.repositorios.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//Controlador para el modelo de Envíos
@RestController
@RequestMapping("/envios")
public class EnvioController {
	// Implementación de repositorios que proporcionan métodos para facilitar las
	// peticiones
	@Autowired
	private EnvioRepository repository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private RepartidorRepository repartidorRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	// Endpoint que devuelve todos los envíos
	@GetMapping
	public List<Envio> obtenerEnvios() {
		return repository.findAll();
	}

	// Endpoint que devuelve un envío buscando por id
	@GetMapping("/buscarId/{id}")
	public Optional<Envio> obtenerEnvioId(@PathVariable Long id) {
		return repository.findById(id);
	}

	// Endpoint que devuelve un envío buscando por fecha de entrega
	@GetMapping("/buscarFecha/{fechaEntrega}")
	public List<Envio> obtenerEnvioFecha(@PathVariable LocalDate fechaEntrega) {
		return repository.findAllByFechaEntrega(fechaEntrega);
	}

	// Endpoint que devuelve un envío buscando por repartidor
	@GetMapping("/buscarRepartidor/{repartidor_id}")
	public List<Envio> obtenerEnvioRepartidor(@PathVariable Long repartidor_id) {
		return repository.findAllByRepartidorId(repartidor_id);
	}

	// Endpoint que devuelve un envío buscando por repartidor y estado
	@GetMapping("/buscarRepartidorEstado/{repartidor_id}/{estado_id}")
	public List<Envio> obtenerEnvioRepartidorEstado(@PathVariable Long repartidor_id, @PathVariable Long estado_id) {
		Optional<Repartidor> repartidor = repartidorRepository.findById(repartidor_id);
		Optional<Estado> estado = estadoRepository.findById(estado_id);
		if (repartidor != null && estado != null) {
			return repository.findAllByRepartidorAndEstado(repartidor, estado);
		} else {
			// Manejo de error si el objeto Repartidor no existe
			return Collections.emptyList();
		}
	}

	// Endpoint que devuelve un envío buscando por repartidor y cliente receptor
	@GetMapping("/buscarRepartidorCliente/{repartidor_id}/{cliente_id}")
	public List<Envio> obtenerEnvioRepartidorCliente(@PathVariable Long repartidor_id, @PathVariable Long cliente_id) {
		Optional<Repartidor> repartidor = repartidorRepository.findById(repartidor_id);
		Optional<Cliente> cliente = clienteRepository.findById(cliente_id);
		if (repartidor != null && cliente != null) {
			return repository.findAllByRepartidorAndCliente(repartidor, cliente);
		} else {
			// Manejo de error si el objeto Repartidor no existe
			return Collections.emptyList();
		}
	}

	// Endpoint que devuelve un envío buscando por id de cliente
	@GetMapping("/buscarCliente/{cliente_id}")
	public List<Envio> obtenerEnvioCliente(@PathVariable Long cliente_id) {
		return repository.findAllByClienteId(cliente_id);
	}

	// Endpoint que devuelve un envío buscando por id de administrador
	@GetMapping("/buscarAdministrador/{administrador_id}")
	public List<Envio> obtenerEnvioAdministrador(@PathVariable Long administrador_id) {
		return repository.findAllByAdministradorId(administrador_id);
	}

	// Endpoint que devuelve un envío buscando por id de estado
	@GetMapping("/buscarEstado/{estado_id}")
	public List<Envio> obtenerEnvioEstado(@PathVariable Long estado_id) {
		return repository.findAllByEstadoId(estado_id);
	}

	// Endpoint que devuelve un envío buscando fecha del dia en que se ejecuta
	@GetMapping("/modificados-hoy")
	public List<Envio> getEnviosModificadosHoy() {
		LocalDate fechaHoy = LocalDate.now();

		return repository.findAllByFechaModificacion(fechaHoy);
	}

	// Endpoint que almacena un nuevo envío
	@PostMapping
	public ResponseEntity<Envio> crearEnvio(@RequestBody Envio nuevoEnvio) {
		repository.save(nuevoEnvio);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEnvio);
	}

	// Endpoint para modificar un envío por completo
	@PutMapping("/{id}")
	public ResponseEntity<Envio> actualizarEnvio(@PathVariable Long id, @RequestBody Envio envioActualizado) {
		Optional<Envio> envio = repository.findById(id);
		LocalDate modificacion = LocalDate.now();
		if (envio.isPresent()) {
			Envio envioExistente = envio.get();
			envioExistente.setOrigen(envioActualizado.getOrigen());
			envioExistente.setDestino(envioActualizado.getDestino());
			envioExistente.setCliente(envioActualizado.getCliente());
			envioExistente.setRepartidor(envioActualizado.getRepartidor());
			envioExistente.setAdministrador(envioActualizado.getAdministrador());
			envioExistente.setFechaEntrega(envioActualizado.getFechaEntrega());
			envioExistente.setEmpresaReparto(envioActualizado.getEmpresaReparto());
			envioExistente.setEstado(envioActualizado.getEstado());
			envioExistente.setFechaModificacion(modificacion);
			repository.save(envioExistente);
			return ResponseEntity.ok(envioExistente);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Endpoint para modificar la fecha de modificación de un envío
	@PutMapping("/cambiarModificacion/{id}/{fechaModificacion}")
	public ResponseEntity<Envio> actualizarFechaModificacion(@PathVariable Long id,
			@PathVariable LocalDate fechaModificacion) {
		Optional<Envio> envio = repository.findById(id);
		if (envio.isPresent()) {
			Envio envioExistente = envio.get();
			envioExistente.setFechaModificacion(fechaModificacion);
			repository.save(envioExistente);
			return ResponseEntity.ok(envioExistente);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Endpoint para modificar el estado de un envío
	@PutMapping("/{id}/{estadoId}")
	public ResponseEntity<Envio> actualizarEstadoEnvio(@PathVariable Long id, @PathVariable Long estadoId) {
		Optional<Envio> envio = repository.findById(id);
		Optional<Estado> estado = estadoRepository.findById(estadoId);
		if (envio.isPresent()) {
			Envio envioExistente = envio.get();
			Estado estadoExistente = estado.get(); // Obtén el objeto Estado válido

			envioExistente.setEstado(estadoExistente);
			repository.save(envioExistente);
			return ResponseEntity.ok(envioExistente);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Endpoint para modificar el estado y el repartidor de un envío
	@PutMapping("{id}/{estadoId}/{repartidorId}")
	public ResponseEntity<Envio> actualizarRepartidorEnvio(@PathVariable Long id, @PathVariable Long estadoId,
			@PathVariable Long repartidorId) {
		Optional<Envio> envio = repository.findById(id);
		Optional<Repartidor> repartidor = repartidorRepository.findById(repartidorId);
		Optional<Estado> estado = estadoRepository.findById(estadoId);
		if (envio.isPresent()) {
			Envio envioExistente = envio.get();
			Repartidor repartidorExistente = repartidor.get(); // Obtén el objeto Estado válido
			Estado estadoExistente = estado.get();

			envioExistente.setRepartidor(repartidorExistente);
			envioExistente.setEstado(estadoExistente);
			repository.save(envioExistente);
			return ResponseEntity.ok(envioExistente);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Endpoint para eliminar un envío buscando por id
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarEnvio(@PathVariable Long id) {
		Optional<Envio> envio = repository.findById(id);
		if (envio.isPresent()) {
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}