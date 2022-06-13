package org.afernandez.appmockito.ejemplos.services;

import org.afernandez.appmockito.ejemplos.Datos;
import org.afernandez.appmockito.ejemplos.models.Examen;
import org.afernandez.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.afernandez.appmockito.ejemplos.repositories.IExamenRepository;
import org.afernandez.appmockito.ejemplos.repositories.IPreguntaRepository;
import org.afernandez.appmockito.ejemplos.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

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
	ExamenRepositoryImpl respositoryImpl;

	@Mock
	IPreguntaRepository preguntaRepository; //este es un repository (mock)
	@Mock
	PreguntaRepositoryImpl preguntaRepositoryImpl;

	@InjectMocks //crea la instancia del servicio y crea los dos objetos. Ya no hace falta poner el new ExamenServiceImpl
	//ya lo hace automático la llamada a los repositories por el constructor.
			//Antes teniamos IExamenService, pero al ser una interfaz no nos deja, con lo que tenemos que poner la clase Impl
	ExamenServiceImpl service;//Esto es un servicio

	@Captor
	ArgumentCaptor<Long> captor;

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

	/*
	Argument matcher es un caracteristica de mockito que te permite saber si coincide el valor real que se pasa por argumento, como service
	y los comparamos con los definidos en el mock, por ejemplo en el when o en el verify. si lo pasa bien si no falla.
	 */
	@Test
	void testArgumentMatchers(){
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		service.findExamenPorNombreConPreguntas("Matemáticas");

		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg.equals(5L))); // va a comprobar que tenga valor 5 en la id.
		verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg >= 5L)); // otra forma.
		verify(preguntaRepository).findPreguntasPorExamenId(eq(5L)); // otra forma.
	}

	//creamos clase anidada
	public static class MiArgsMatchers implements ArgumentMatcher<Long>{
		//ya tenemos una clase personalizada, podria ser externa la clase.
		private Long argument;

		@Override
		public boolean matches(Long argument) {
			this.argument = argument;
			return argument != null && argument > 0;
		}

		//podemos personalizar el mensaje de error con el toString

		@Override
		public String toString() {
			return "Es para un mensaje personalizado de error " +
					"que imprime mockito en caso de que falle el test," +
					 argument + " debe ser un entero positivo";
		}
	}

		@Test
		void testArgumentMatchers2(){
			when(repository.findAll()).thenReturn(Datos.EXAMENES);
			when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
			service.findExamenPorNombreConPreguntas("Matemáticas");

			verify(repository).findAll();
			verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers())); // otra forma de hacerlo llamando a la clase personalizada que hemos creado.

		}

		//Tambien podemos caputar los argumentos con Argument Captor
		@Test
		void testArgumentCaptor(){
			when(repository.findAll()).thenReturn(Datos.EXAMENES);
	//		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
			service.findExamenPorNombreConPreguntas("Matemáticas");

			//capturamos, el tipo es Long porque es de tipo Long
	//		ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class); //esto lo podemos ahcer con anotaciones, por eso esta comentado
			verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());

			assertEquals(5L, captor.getValue());
		}

	//este test es para comprobar  los errores cuando un metodo no devuelve nada
		@Test
		void testDoThrow(){
			Examen examen = Datos.EXAMEN;
			examen.setPreguntas(Datos.PREGUNTAS);
			//Cuando queremos una excepcion, debemos colocar primero el "hacer excepcion, colocar la calse d la excepcion, luego el when cerramos y invocamos al metodo"
			doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());

			assertThrows(IllegalArgumentException.class, () -> {
				service.guardar(examen);
			});


		}
		@Test
		void testDoAnswer(){
			when(repository.findAll()).thenReturn(Datos.EXAMENES);
			//ESto lo comentamos por que lo podemos ahcer con el doAnswer
//			when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
			doAnswer(invocation -> {
				Long id = invocation.getArgument(0);
				return id == 5L ? Datos.PREGUNTAS : null;  //para ese examen si es el id 5 le damos las preguntas si no null. algo distinto pero similar a lo que tenemos mas arriba
			}).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

			Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
			assertEquals(5L, examen.getId());
			assertEquals("Matemáticas", examen.getNombre());
		}

		//Hay que tener cuidado con los metodos reales, solo lo utilizaremos si necesitamos crear un metodo real que es una libreria externa etc.
	@Test
	void  testDoCallRealMethod(){
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		//when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		//si esto lo descomentamos y comentamos el docall veremos que no imprime nada por que ahi lo está simulando y en docall llama al metodo reeal.

		doCallRealMethod().when(preguntaRepositoryImpl).findPreguntasPorExamenId(anyLong()); // esto no es un simulacro es una llamada real al metodo.
		//las interfaces al no tener metodo reales no puede invocarlo, porque es una clase abstacta o interfaz, ya que no cuenta de implementacion real.
		//hay un soutm para que nos salga en la consola si realmente utiliza ese metodo.

		Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
		assertEquals(5L, examen.getId());
		assertEquals("Matemáticas", examen.getNombre());
	}
}