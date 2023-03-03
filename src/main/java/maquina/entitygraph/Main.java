package maquina.entitygraph;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.log4j.Log4j2;
import maquina.entitygraph.dominio.Alquimista;
import maquina.entitygraph.dominio.Pocion;
import maquina.entitygraph.repository.AlquimistaRepository;

/**
 * Main para la ejecución de pruebas personalizadas
 * 
 * @author MaQuiNa1995
 *
 */
@Log4j2
@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String... args) {
		SpringApplication.run(Main.class);
	}

	private Alquimista alquimista;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private AlquimistaRepository alquimistaRepository;

	@Override
	public void run(String... args) throws Exception {

		poblarBaseDatos();

		conEntityGraph();
		sinEntityGraph();
	}

	/**
	 * Usamos este método para poblar la base de datos
	 */
	private void poblarBaseDatos() {

		this.alquimista = new Alquimista();

		Pocion pocion = new Pocion();
		pocion.setNombre("Elixir");
		pocion.setAlquimista(this.alquimista);

		Pocion pocion2 = new Pocion();
		pocion2.setNombre("Elixir++");
		pocion2.setAlquimista(this.alquimista);

		Pocion pocion3 = new Pocion();
		pocion3.setNombre("Antídoto");
		pocion3.setAlquimista(this.alquimista);

		this.alquimista.setNombre("Maquina1995");
		this.alquimista.setPociones(Arrays.asList(pocion, pocion2, pocion3));

		alquimistaRepository.save(this.alquimista);
	}

	private void conEntityGraph() {

		log.info("------------- Trazas con entitygraph ------------- ");

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
	}

	void sinEntityGraph() {

		log.info("------------- Trazas sin entitygraph ------------- ");
		try {
			// Hacemos un find normal para ver la diferencia
			// de consultas con el anterior test
			entityManager.find(Alquimista.class, this.alquimista.getId())
					.getPociones();

		} catch (Exception e) {
			log.error("Como se puede apreciar se da la siguiente excepción ya que al ser lazy,");
			log.error(
					"Hemos accedido a un LazyBag cuando se ha cerrado digamos la conexión dando lugar al siguiente error");
			log.error(e.getMessage());
		}
	}

}
