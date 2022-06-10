package org.afernandez.appmockito.ejemplos.repositories;

import org.afernandez.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public interface IExamenRepositoryImpl implements IExamenRespository{

	@Override
	public List<Examen> findAll(){
		return Arrays.asList(new Examen(5L, "Matemáticas"),
				new Examen(6L, "Lenguaje"),
				new Examen(7L, "Historia"));
	}
}
