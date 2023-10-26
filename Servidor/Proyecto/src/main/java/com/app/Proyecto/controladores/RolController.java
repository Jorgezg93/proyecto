package com.app.Proyecto.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.Proyecto.modelos.*;
import com.app.Proyecto.repositorios.RolRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RolController {
	@Autowired
    private RolRepository repository;
    
    @GetMapping
    public List<Rol> obtenerRoles() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Rol> obtenerRol(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol newRol) {
    	if(repository.existsByNombre(newRol.getNombre())) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    	}
        repository.save(newRol);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRol);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable Long id, @RequestBody Rol rolActualizado) {
        Optional<Rol> rolExistente = repository.findById(id);
        if (rolExistente.isPresent()) {
        	Rol rol = rolExistente.get();
            rol.setNombre(rolActualizado.getNombre());
            repository.save(rol);
            return ResponseEntity.ok(rol);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Rol> eliminarRol(@PathVariable Long id) {
    	Optional<Rol> rol = repository.findById(id);
    	if(rol.isPresent()) {
    		repository.deleteById(id);
    		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    	}else {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    	}
        
    }

}