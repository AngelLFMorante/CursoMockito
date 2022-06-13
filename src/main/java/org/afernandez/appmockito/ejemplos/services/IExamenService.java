package org.afernandez.appmockito.ejemplos.services;

import org.afernandez.appmockito.ejemplos.models.Examen;

import java.util.Optional;

public interface IExamenService {
	Optional<Examen> findExamenPorNombre(String nombre) ;
	Examen findExamenPorNombreConPreguntas(String nombre);

	Examen guardar(Examen examen);
}
