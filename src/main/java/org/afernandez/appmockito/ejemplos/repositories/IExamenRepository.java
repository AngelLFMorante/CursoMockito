package org.afernandez.appmockito.ejemplos.repositories;

import org.afernandez.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface IExamenRespository {
	List<Examen> findAll() throws InterruptedException;
}
