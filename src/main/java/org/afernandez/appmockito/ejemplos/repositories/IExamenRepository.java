package org.afernandez.appmockito.ejemplos.repositories;

import org.afernandez.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface IExamenRepository {
	List<Examen> findAll() ;
	//simular guardado
	Examen guardar(Examen examen);
}
