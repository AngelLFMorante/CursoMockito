package org.afernandez.appmockito.ejemplos.services;

import org.afernandez.appmockito.ejemplos.models.Examen;

public interface IExamenService {
	Examen findExamenPorNombre(String nombre);
}
