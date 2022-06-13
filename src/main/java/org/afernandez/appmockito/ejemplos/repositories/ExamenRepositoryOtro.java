package org.afernandez.appmockito.ejemplos.repositories;

import org.afernandez.appmockito.ejemplos.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryOtro implements IExamenRepository {
	@Override
	public List<Examen> findAll()  {
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Examen guardar(Examen examen) {
		return null;
	}
}
