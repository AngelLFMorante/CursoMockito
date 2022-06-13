package org.afernandez.appmockito.ejemplos.repositories;

import org.afernandez.appmockito.ejemplos.Datos;
import org.afernandez.appmockito.ejemplos.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class ExamenRepositoryImpl implements IExamenRepository {
	@Override
	public List<Examen> findAll()  {
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("ExamenRepositoryImpl.findAll");
		return Datos.EXAMENES;
	}

	@Override
	public Examen guardar(Examen examen) {
		System.out.println("ExamenRepositoryImpl.guardar");
		return Datos.EXAMEN;
	}
}
