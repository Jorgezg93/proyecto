package com.app.Proyecto.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.app.Proyecto.modelos.*;
import com.app.Proyecto.repositorios.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private ClienteRepository repository;

    @GetMapping
    public List<Cliente> obtenerClientes() {
        return repository.findAll();
    }

    @GetMapping("/buscar/{id}")
    public Optional<Cliente> obtenerClienteId(@PathVariable Long id) {
        return repository.findById(id);
        
    }

    @GetMapping("/buscarEmail/{email}")
    public Cliente obtenerClienteEmail(@PathVariable String email) {
    	Optional<Cliente> cliente = repository.findByEmail(email);
    	if (cliente.isPresent()) {
            return cliente.get();
        } else {
            return null;
        }
    }
    
    @GetMapping("/buscarNick/{email}")
    public Cliente obtenerClienteNombre(@PathVariable String nombre) {
    	Optional<Cliente> cliente = repository.findByNombre(nombre);
    	if (cliente.isPresent()) {
            return cliente.get();
        } else {
            return null;
        }
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente nuevoCliente) {
        if(repository.existsByEmail(nuevoCliente.getEmail())) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        repository.save(nuevoCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteActualizado) {
        Optional<Cliente> cliente = repository.findById(id);
        if (cliente.isPresent()) {
        	Cliente clienteCambio = cliente.get();
        	clienteCambio.setNombre(clienteActualizado.getNombre());
        	clienteCambio.setApellidos(clienteActualizado.getApellidos());
        	clienteCambio.setDni(clienteActualizado.getDni());
        	clienteCambio.setTelefono(clienteActualizado.getTelefono());
        	clienteCambio.setDireccion(clienteActualizado.getDireccion());
        	repository.save(clienteCambio);
            return ResponseEntity.ok(clienteCambio);
            } else {
            return ResponseEntity.notFound().build();
            }
            }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
    	Optional<Cliente> cliente = repository.findById(id);
    	if (cliente.isPresent()) {
    		repository.delete(cliente.get());
    		return ResponseEntity.noContent().build();
    	}else {
    		return ResponseEntity.noContent().build();
    	}   
            }
            
            
}