package maquina.entitygraph.repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import maquina.entitygraph.dominio.Alquimista;
import maquina.entitygraph.dominio.Pocion;

@DataJpaTest
@ComponentScan(value = "maquina.hibernate.repository")
class EntityGraphExampleTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private AlquimistaRepository alquimistaRepository;

	private Alquimista alquimista;

	/**
	 * Usamos este método para poblar la base de datos
	 */
	@BeforeEach
	void setUp() {

		this.alquimista = new Alquimista();

		Pocion pocion = new Pocion();
		pocion.setNombre("Elixir");

		Pocion pocion2 = new Pocion();
		pocion2.setNombre("Elixir++");

		Pocion pocion3 = new Pocion();
		pocion3.setNombre("Antídoto");

		this.alquimista.setNombre("Maquina1995");
		this.alquimista.setPociones(Arrays.asList(pocion, pocion2, pocion3));

		alquimistaRepository.save(this.alquimista);
	}

	@Test
	void testConEntityGraph() {

		// Se crea un entity grap de la entidad Alquimista
		EntityGraph<Alquimista> entityGraph = entityManager.createEntityGraph(Alquimista.class);

		// Por defecto al hacer un find by id se cogen todos los atributos de la entidad
		// de la que estemos creado el grafo
		// por lo tanto aqui tendremos que incluir los campos de relaciones que queremos
		// sacar de la base de datos
		// al ser lazy la relación si no hicieramos la consulta por grafo nos daría la
		// excepción que mostramos en el otro test de esta clase
		entityGraph.addSubgraph("pociones")
				.addAttributeNodes("nombre");

		// Se debe crear un mapa con las properties para indicar en nuestro find que
		// vamos a usar un grafo en la consulta
		// de key puede tener:
		// - javax.persistence.fetchgraph
		// - javax.persistence.loadgraph
		//
		// y de value debe tener nuestro grafo creado
		Map<String, Object> properties = Collections.singletonMap("javax.persistence.fetchgraph", entityGraph);

		// Indicamos en nuestro find que queremos usar un mapa con la configuración (uso
		// del grafo)
		entityManager.find(Alquimista.class, this.alquimista.getId(), properties);

		// Assertion inutil lo importante está en las anteriores líneas a modo didactico
		Assertions.assertTrue(true);
	}

	@Test
	void testSinEntityGraph() {

		// Hacemos un find normal para ver la diferencia
		// de consultas con el anterior test
		entityManager.find(Alquimista.class, this.alquimista.getId());

		// Assertion inutil lo importante está en las anteriores líneas a modo didactico
		Assertions.assertTrue(true);
	}

}
