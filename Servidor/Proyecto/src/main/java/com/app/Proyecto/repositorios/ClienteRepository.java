package com.app.Proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Proyecto.modelos.*;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	// Método que permite buscar si un cliente existe mediante su email
	boolean existsByEmail(String email);

	// Método que permite buscar un cliente mediante email
	Optional<Cliente> findByEmail(String email);

	// Método que permite buscar un cliente mediante nombre
	Optional<Cliente> findByNombre(String nombre);
}