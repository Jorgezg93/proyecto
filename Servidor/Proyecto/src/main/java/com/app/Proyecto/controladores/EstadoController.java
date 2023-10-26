package com.app.Proyecto.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.Proyecto.modelos.*;
import com.app.Proyecto.repositorios.*;

import java.util.List;
import java.util.Optional;

//Controlador para el modelo de Estado
@RestController
@RequestMapping("/estados")
public class EstadoController {

	// Implementación de repositorios que proporcionan métodos para facilitar las
	// peticiones
	@Autowired
	private EstadoRepository repository;

	// Endpoint que devuelve todos los estados
	@GetMapping
	public List<Estado> obtenerEstados() {
		return repository.findAll();
	}

	// Endpoint que devuelve un estado buscando por id
	@GetMapping("/{id}")
	public ResponseEntity<Estado> obtenerEstado(@PathVariable Long id) {
		Optional<Estado> estado = repository.findById(id);
		if (estado.isPresent()) {
			return ResponseEntity.ok(estado.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Endpoint para almacenar un nuevo estado
	@PostMapping
	public ResponseEntity<Estado> crearEstado(@RequestBody Estado nuevoEstado) {
		if (repository.existsByNombre(nuevoEstado.getNombre())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		repository.save(nuevoEstado);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstado);
	}

	// Endpoint para modificar el nombre de un estado
	@PutMapping("/{id}")
	public ResponseEntity<Estado> actualizarEstado(@PathVariable Long id, @RequestBody Estado estadoActualizado) {
		Optional<Estado> estado = repository.findById(id);
		if (estado.isPresent()) {
			Estado estadoCambio = estado.get();
			estadoCambio.setNombre(estadoActualizado.getNombre());
			repository.save(estadoCambio);
			return ResponseEntity.ok(estadoCambio);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Endpoint para eliminar un estado buscando por id
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarEstado(@PathVariable Long id) {
		Optional<Estado> estado = repository.findById(id);
		if (estado.isPresent()) {
			repository.delete(estado.get());
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}