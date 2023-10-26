package com.app.Proyecto.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Proyecto.modelos.*;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {
	// Buscar si existe un repartidor por su email
	boolean existsByEmail(String email);

	// Buscar un repartidor por email
	Optional<Repartidor> findByEmail(String email);
}
