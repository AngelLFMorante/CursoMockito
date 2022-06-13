package org.afernandez.appmockito.ejemplos.repositories;

import org.afernandez.appmockito.ejemplos.Datos;

import java.util.List;

public class PreguntaRepositoryImpl implements IPreguntaRepository{
	@Override
	public List<String> findPreguntasPorExamenId(Long id) {
		System.out.println("PreguntaRepositoryImpl.findPreguntasPorExamenId");
		return Datos.PREGUNTAS;
	}

	@Override
	public void guardarVarias(List<String> preguntas) {
		System.out.println("PreguntaRepositoryImpl.guardarVarias");
	}
}
