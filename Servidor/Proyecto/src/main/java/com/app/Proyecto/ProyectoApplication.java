package com.app.Proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class ProyectoApplication {

	// Método main que lanza la aplicación y carga todos los módulos
	public static void main(String[] args) {
		SpringApplication.run(ProyectoApplication.class, args);
	}

}
