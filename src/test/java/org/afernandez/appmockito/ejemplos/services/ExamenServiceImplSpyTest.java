package org.afernandez.appmockito.ejemplos.services;

import org.afernandez.appmockito.ejemplos.models.Examen;
import org.afernandez.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.afernandez.appmockito.ejemplos.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)//Si quitamos esta extension no funcionaria
class ExamenServiceImplSpyTest {

	@Spy
	ExamenRepositoryImpl respositoryImpl;

	@Spy
	PreguntaRepositoryImpl preguntaRepositoryImpl;

	@InjectMocks //se puede utilizar para ambos, ya sea spy o no
	ExamenServiceImpl service;

	//No hay que abusar del Spy,ya que no tenemos el control total, solo se utiliza cuando sea el 100% y dependamos de esa funcionalidad de esa llamada original de terceros.

	@Test
	void testSpy(){
		//diferencia mock vs spy:
		/*
		el mock es simulado y todas las pruebas que hagamos las tenemos que mockear con el when, do etc,
		con mock podemos utilizar interface, abstract o la misma clase concreta, por que los métodos siempre se van a simular,
		y siempre le vamos a dar una implementación falsa.
		El spy simula con when y do pero las llamadas que se hacen son las reales a los métodos, el spy requiere que sea desde una clase concreta y no
		desde una clase abstracta o interfaz porque va a llamar a metodos reales, y no va haber metodos reales.
		 */
		//Tambien tiene anotaciones @Spy
//		IExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
//		IPreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
//		IExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);
		//vamos a simular:
		//hacemos una invocacion falsa un mock.
		//lo que pasa que hace una llamada real por el preguntaRepository, pero luego en el examenService hace la invocacion falsa simulada al pasarle Datos.Preguntas

		//when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

		//para trabajar con spy es mejor hacerlo con Do en vez de When
		//aqui ya lo hacemos simulado la mayor parte y ya no hace la llamada al metodo por soutm
		List<String> preguntas = Arrays.asList(
				"aritmética",
				"integrales",
				"derivadas",
				"trigonometría",
				"geometría"
		);
		doReturn(preguntas).when(preguntaRepositoryImpl).findPreguntasPorExamenId(anyLong());

		//No estamos simulando nada, estamos directamente asignandole el resultado a examen desde el examenService, sin utilizar when ni nada.
		Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");

		assertEquals(5, examen.getId());
		assertEquals( "Matemáticas", examen.getNombre());
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("aritmética"));

		//los verify siempre se van a llamar, ya sea real o simulado.
		verify(respositoryImpl).findAll();
		verify(preguntaRepositoryImpl).findPreguntasPorExamenId(anyLong());
	}
}