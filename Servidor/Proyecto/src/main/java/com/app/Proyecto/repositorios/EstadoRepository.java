package com.app.Proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Proyecto.modelos.*;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
	// Buscar si existe un estado según el nombre
	boolean existsByNombre(String nombre);

}