package com.app.Proyecto.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.Proyecto.modelos.*;
import com.app.Proyecto.repositorios.AdminRepository;

import java.util.List;
import java.util.Optional;

//Controlador para el modelo de Administradores
@RestController
@RequestMapping("/administradores")
public class AdministradorController {

	// Implementación del repositorio que proporciona métodos para facilitar las
	// peticiones
	@Autowired
	private AdminRepository repository;

	// Endpoint que devuelve todos los administradores
	@GetMapping
	public List<Administrador> obtenerAdministradores() {
		return repository.findAll();
	}

	// Endpoint que devuelve un administrador buscando por id
	@GetMapping("/buscar/{id}")
	public Optional<Administrador> obtenerAdministrador(@PathVariable Long id) {
		return repository.findById(id);
	}

	// Endpoint que devuelve un administrador buscando por email
	@GetMapping("/buscarEmail/{email}")
	public Administrador obtenerAdminEmail(@PathVariable String email) {
		Optional<Administrador> administrador = repository.findByEmail(email);
		if (administrador.isPresent()) {
			return administrador.get();
		} else {
			return null;
		}
	}

	// Endpoint que almacena un nuevo administrador
	@PostMapping
	public ResponseEntity<Administrador> crearAdministrador(@RequestBody Administrador nuevoAdministrador) {
		if (repository.existsByEmail(nuevoAdministrador.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		repository.save(nuevoAdministrador);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAdministrador);
	}

	// Endpoint para modificar un administrador por completo
	@PutMapping("/{id}")
	public ResponseEntity<Administrador> actualizarAdministrador(@PathVariable Long id,
			@RequestBody Administrador administradorActualizado) {
		Optional<Administrador> administrador = repository.findById(id);
		if (administrador.isPresent()) {
			Administrador administradorExistente = administrador.get();
			administradorExistente.setNombre(administradorActualizado.getNombre());
			administradorExistente.setApellidos(administradorActualizado.getApellidos());
			administradorExistente.setDni(administradorActualizado.getDni());
			administradorExistente.setTelefono(administradorActualizado.getTelefono());
			administradorExistente.setEmail(administradorActualizado.getEmail());
			repository.save(administradorExistente);
			return ResponseEntity.ok(administradorExistente);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Endpoint para eliminar un administrador buscando por id
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarAdministrador(@PathVariable Long id) {
		Optional<Administrador> administrador = repository.findById(id);
		if (administrador.isPresent()) {
			repository.delete(administrador.get());
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}