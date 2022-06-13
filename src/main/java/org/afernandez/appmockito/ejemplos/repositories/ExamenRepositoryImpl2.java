package org.afernandez.appmockito.ejemplos.repositories;

import org.afernandez.appmockito.ejemplos.models.Examen;

import java.util.Collections;
import java.util.List;

public class ExamenRepositoryImpl2 implements IExamenRepository {

	@Override //CUANDO APLICAMOS MOCKITO ESTA CLASE DA IGUAL, NO NOS HACE FALTA, PORQUE LO MOCKEAMOS CON LA INTERFAZ
	public List<Examen> findAll(){
		return Collections.emptyList(); /*Arrays.asList(
				new Examen(5L, "Matemáticas"),
				new Examen(6L, "Lenguaje"),
				new Examen(7L, "Historia")
		);*/
	}

	@Override
	public Examen guardar(Examen examen) {
		return null;
	}
}
