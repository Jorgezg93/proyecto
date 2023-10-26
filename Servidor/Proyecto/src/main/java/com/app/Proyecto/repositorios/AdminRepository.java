package com.app.Proyecto.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Proyecto.modelos.*;

@Repository
public interface AdminRepository extends JpaRepository<Administrador, Long>{
	//Permite comprobar si existe un administrador buscando por email
	boolean existsByEmail(String email);
	//Permite la búsqueda personalizada por email
	Optional<Administrador> findByEmail(String email);
}