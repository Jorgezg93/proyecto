package com.app.Proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Proyecto.modelos.*;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
	// Buscar si existe un rol por su nombre
	boolean existsByNombre(String nombre);
}