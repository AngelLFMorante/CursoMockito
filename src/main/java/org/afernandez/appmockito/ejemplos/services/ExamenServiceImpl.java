package org.afernandez.appmockito.ejemplos.services;

import org.afernandez.appmockito.ejemplos.models.Examen;
import org.afernandez.appmockito.ejemplos.repositories.IExamenRepository;
import org.afernandez.appmockito.ejemplos.repositories.IPreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements IExamenService{

	private IExamenRepository examenRespository;
	private IPreguntaRepository preguntaRepository; //ESto podríamos como simular que está en otro servidor o lo cogemos por via api rest

	public ExamenServiceImpl(IExamenRepository examenRespository, IPreguntaRepository preguntaRepository) {
		this.examenRespository = examenRespository;
		this.preguntaRepository = preguntaRepository;
	}

	@Override
	public Optional<Examen> findExamenPorNombre(String nombre)  {

		//Buscamos en el repository, y filtramos por el stream y con lambda.
		return examenRespository.findAll()
				.stream()
				.filter(e -> e.getNombre().contains(nombre))
				.findFirst();

		//Lo hemos comentado ya que hemos cambiado el devolver Examen por OPTIONAL
//		Examen examen = null; //inicializamos examen en null
//		//comprobamos que ha obtenido resultados examenOptional.
//		if(examenOptional.isPresent()){
//			examen = examenOptional.orElseThrow(); //si no obtuvise resultado o tuviese error, devolveria una excepcion
//		}

//		return examen;//devolvemos el resultado


	}

	@Override
	public Examen findExamenPorNombreConPreguntas(String nombre) {
		Optional<Examen> examenOptional = findExamenPorNombre(nombre);

		Examen examen = null;
		if(examenOptional.isPresent()){
			examen = examenOptional.orElseThrow();
			List<String> preguntas =preguntaRepository.findPreguntasPorExamenId(examen.getId());
			examen.setPreguntas(preguntas);
		}
		return examen;
	}

	@Override
	public Examen guardar(Examen examen) {
		//comprobamos si el examen contiene preguntas
		if(!examen.getPreguntas().isEmpty()){
			preguntaRepository.guardarVarias(examen.getPreguntas());
		}
		//guardamos y devolvemos el examen.
		return examenRespository.guardar(examen);
	}

}
