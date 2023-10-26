package com.app.Proyecto.controladores;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.Proyecto.modelos.*;
import com.app.Proyecto.repositorios.RepartidorRepository;

//Controlador para el modelo de Repartidor
@RestController
@RequestMapping("/repartidores")
public class RepartidorController {

	// Implementación de repositorios que proporcionan métodos para facilitar las
	// peticiones
	@Autowired
	private RepartidorRepository repository;

	// Endpoint que devuelve todos los repartidores
	@GetMapping
	public List<Repartidor> obtenerRepartidores() {
		return repository.findAll();
	}

	// Endpoint que devuelve un repartidor buscando por id
	@GetMapping("/buscar/{id}")
	public Optional<Repartidor> obtenerRepartidor(@PathVariable Long id) {
		return repository.findById(id);
	}

	// Endpoint que devuelve un repartidor buscando por email
	@GetMapping("/buscarEmail/{email}")
	public Repartidor obtenerReparEmail(@PathVariable String email) {
		Optional<Repartidor> repartidor = repository.findByEmail(email);
		if (repartidor.isPresent()) {
			return repartidor.get();
		} else {
			return null;
		}
	}

	// Endpoint que almacena un nuevo repartidor
	@PostMapping
	public ResponseEntity<Repartidor> crearRepartidor(@RequestBody Repartidor nuevoRepartidor) {
		if (repository.existsByEmail(nuevoRepartidor.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		repository.save(nuevoRepartidor);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRepartidor);
	}

	// Endpoint que modifica un repartidor por completo
	@PutMapping("/{id}")
	public ResponseEntity<Repartidor> actualizarRepartidor(@PathVariable Long id,
			@RequestBody Repartidor repartidorActualizado) {
		Optional<Repartidor> repartidor = repository.findById(id);
		if (repartidor.isPresent()) {
			Repartidor repartidorExistente = repartidor.get();
			repartidorExistente.setNombre(repartidorActualizado.getNombre());
			repartidorExistente.setApellidos(repartidorActualizado.getApellidos());
			repartidorExistente.setDni(repartidorActualizado.getDni());
			repartidorExistente.setTelefono(repartidorActualizado.getTelefono());
			repartidorExistente.setEmail(repartidorActualizado.getEmail());
			repartidorExistente.setLatitud(repartidorActualizado.getLatitud());
			repartidorExistente.setLongitud(repartidorActualizado.getLongitud());
			repartidorExistente.setFechaHoraActualizacion(repartidorActualizado.getFechaHoraActualizacion());
			repository.save(repartidorExistente);
			return ResponseEntity.ok(repartidorExistente);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Endpoint que modifica la ubicacion y fecha de modificación de un repartidor
	@PutMapping("/{id}/{latitud}/{longitud}/{fechaModificacion}")
	public ResponseEntity<Repartidor> actualizarRepartidorUbicacion(@PathVariable Long id, @PathVariable Double latitud,
			@PathVariable Double longitud, @PathVariable LocalDateTime fechaModificacion) {
		Optional<Repartidor> repartidor = repository.findById(id);
		if (repartidor.isPresent()) {
			Repartidor repartidorExistente = repartidor.get();
			repartidorExistente.setFechaHoraActualizacion(fechaModificacion);
			repartidorExistente.setLatitud(latitud);
			repartidorExistente.setLongitud(longitud);
			repository.save(repartidorExistente);
			return ResponseEntity.ok(repartidorExistente);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Endpoint que elimina un repartidor buscando por id
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarRepartidor(@PathVariable Long id) {
		Optional<Repartidor> repartidor = repository.findById(id);
		if (repartidor.isPresent()) {
			repository.delete(repartidor.get());
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}