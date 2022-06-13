package org.afernandez.appmockito.ejemplos.services;

import org.afernandez.appmockito.ejemplos.models.Examen;
import org.afernandez.appmockito.ejemplos.repositories.IExamenRepository;
import org.afernandez.appmockito.ejemplos.repositories.IPreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockingDetails;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) //con esto también hacemos una extension de mockito que es lo mismo que tenemos en el setUp
	//tenemos que tener mockito-jupiter en la dependencia del pom
class ExamenServiceImplTest {

	/**
	 * NO SE PUEDE HACER MOCK DE CUALQUIER METODO SOLO:
	 * PUBLIC O DEFAULT CUANDO ESTAMOS DENTRO DEL MISMO PACKAGE,
	 * NO SE PUDE HACER UNA SIMULACION DE UN METODO PRIVADO, NI TAMPOCO QUE SEA STATIC, NI TAMPOCO FINAL
	 */

	@Mock //le indicamos que es un mock
	IExamenRepository repository; //este es un repository (mock)
	@Mock
	IPreguntaRepository preguntaRepository; //este es un repository (mock)

	@InjectMocks //crea la instancia del servicio y crea los dos objetos. Ya no hace falta poner el new ExamenServiceImpl
	//ya lo hace automático la llamada a los repositories por el constructor.
			//Antes teniamos IExamenService, pero al ser una interfaz no nos deja, con lo que tenemos que poner la clase Impl
	ExamenServiceImpl service;//Esto es un servicio

	@BeforeEach
	void setUp() {
//		MockitoAnnotations.openMocks(this); //habilitamos las anotaciones de mock para esta clase. Hay otra forma
		//aquí inicializamos las instancias ya que lo utilizaremos en todos los métodos.
		//quitamos Mockito.mock para hacerlo static, por eso solo aparece mock.
//		repository = mock(IExamenRepository.class); //lo que crea es una instancia de la clase o de la interfaz que vamos a implementar.
//		preguntaRepository = mock(IPreguntaRepository.class);
//		service = new ExamenServiceImpl(repository, preguntaRepository);
	}

	@Test
	void findExamenPorNombreTest()  {

		//Lo he comentado por que lo he abstraido para llevarlo a otra clase llamada "Datos"
//		List<Examen> datos = Arrays.asList(
//				new Examen(5L, "Matemáticas"),
//				new Examen(6L, "Lenguaje"),
//				new Examen(7L, "Historia")
//		);
		//Cuando se invoque el metodo de repository, entonces devolvemos datos.
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		/*
		Lo que hacemos con mockito es simular algo que no tenemos visualmente como pueden ser clases o interfaces externas a nuestro proyecto
		con lo que en los test podemos simular la entrada y la salida con datos.
		Esto nos permite tener mas aislado nuestro proyecto, ya que los datos que pedimos ya pasarían sus pruebas, aqui solo simulamos.
		 */
//		Examen examen = service.findExamenPorNombre("Matemáticas"); //lo hemos cambiado por Optional
		Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");
		//assertNotNull(examen); //no hace falta el notnull con optional se hace con True para ver si está presente
		assertTrue(examen.isPresent());
		assertEquals(5L, examen.orElseThrow().getId());
		assertEquals("Matemáticas", examen.orElseThrow().getNombre());
	}

	@Test
	void findExamenPorNombreListaVaciaTest()  {
		//quitamos Mockito.mock para hacerlo static, por eso solo aparece mock.
//		IExamenRepository repository = mock(IExamenRepository.class); //lo que crea es una instancia de la clase o de la interfaz que vamos a implementar.
//		IExamenService service = new ExamenServiceImpl(repository);
		List<Examen> datos = Collections.emptyList(); //otro contexto y no dependo de una implementacion ni de una clase

		when(repository.findAll()).thenReturn(datos);
		Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");

		assertFalse(examen.isPresent()); //esto nos indica que realmente no hay nada y la lista esta vacia
	}

	@Test
	void testPreguntasExamen() {
		//estamos simulando de cada mock.
		//5L es la posicion que queremos buscar, su id.
		//podemos poner anyLong() y eso representa cualquier valor numerico de tipo Long
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(5L)).thenReturn(Datos.PREGUNTAS);
		Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("aritmética"));
	}

	@Test
	void testPreguntasExamenVerify() {
		//vamos a utilizar el verify, para verificar si actuan los parámetrs dentro del metodo. Mock
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(5L)).thenReturn(Datos.PREGUNTAS);
		Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("aritmética"));

		//viene de mockito, pero lo tenemos ya importado estaticamente
		verify(repository).findAll(); //el repository nunca se pone el punto dentro del parentesis siempre fuera.
		verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
	}

	@Test
	void testNoExisteExamenVerify() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(5L)).thenReturn(Datos.PREGUNTAS);

		Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas2");
		assertNull(examen); //Tendremos un null por que no existe ese nombre.

		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
	}

	@Test
	void testGuardarExamen() {

		//BDD behaviour development driven
		//ESto es el given , la precondiciones de nuestro metodo de prueba
		Examen newExamen = Datos.EXAMEN; //tenemos un examen
		newExamen.setPreguntas(Datos.PREGUNTAS); //asignamos preguntas

		//when(repository.guardar(any(Examen.class))).thenReturn(Datos.EXAMEN); //va a devolver el examen cualquiera, en este caso solo habia uno.
		when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){
			Long secuencia = 8L; //le decimos que parta desde la id 8 porque ya tenemos pruebas hechas.
			@Override
			public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
				Examen examen = invocationOnMock.getArgument(0); //siempre parte desde 0
				examen.setId(secuencia++);
				return examen;
			}
			//Con esto ya no dependemos del mismo objeto de prueba datos.examen, el id era null.

		}); //asignamos una id ficticia

		//esto es cuando se ejecuta y queremos probar algo se le llama When
		//When
		Examen examen = service.guardar(newExamen); //va a devolver el mismo.

		//Esto es para validar se le llaman Then
		//Then
		assertNotNull(examen.getId());
		assertEquals(8L, examen.getId());
		assertEquals("Física", examen.getNombre());
		verify(repository).guardar(any(Examen.class));
		verify(preguntaRepository).guardarVarias(anyList());

		//Esto se le dice que estamos trabajando en entorno impulsado al comportamiento.
	}

	@Test
	void testManejoException(){
		//Manejo de excepciones
		when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
		when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);
		//devuelve exception
		Exception exception = assertThrows(IllegalArgumentException.class, ()-> {
			service.findExamenPorNombreConPreguntas("Matemáticas");
		});
		assertEquals(IllegalArgumentException.class, exception.getClass());

		//podemos validar tambien
		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(null);
	}


	}