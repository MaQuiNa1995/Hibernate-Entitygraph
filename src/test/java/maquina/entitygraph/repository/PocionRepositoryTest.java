package maquina.entitygraph.repository;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import maquina.entitygraph.dominio.Alquimista;
import maquina.entitygraph.dominio.Pocion;
import maquina.entitygraph.repository.AlquimistaRepository;

class PocionRepositoryTest extends JpaRepositoryImplTest<Long, Pocion> {

	@Autowired
	private AlquimistaRepository alquimistaRepository;

	@Test
	@Transactional
	void testRelaciones() {

		// Se persiste
		Alquimista alquimista = new Alquimista();
		alquimista.setNombre("Maquina1995");

		// Se crea y relaciona la poción
		Pocion pocion = new Pocion();
		pocion.setNombre("Elixir");
		pocion.setAlquimista(alquimista);

		// Se crea y relaciona la poción2
		Pocion pocion2 = new Pocion();
		pocion2.setNombre("Ultrapoción");
		pocion2.setAlquimista(alquimista);

		// Se relaciona alquimista con sus pociones
		alquimista.setPociones(Arrays.asList(pocion, pocion2));

		// Se persiste
		entityManager.persist(alquimista);

		// Comprobamos
		Assertions.assertFalse(rut.findAll()
				.stream()
				.map(Pocion::getAlquimista)
				.filter(e -> e.equals(alquimista))
				.collect(Collectors.toList())
				.isEmpty());

		Assertions.assertFalse(alquimistaRepository.findAll()
				.stream()
				.filter(e -> !e.getPociones()
						.isEmpty())
				.collect(Collectors.toList())
				.isEmpty());
	}

	/**
	 * Al ser la parte esclava de la relación no se puede persistir un objeto
	 * {@link Alquimista} que tenga asociado un {@link Pocion} si queremos
	 * asociarles deberemos persistir previamente un {@link Pocion} y luego al
	 * persistir el {@link Alquimista} enlazarles
	 * <p>
	 * {@link Pocion#getAlquimista()} En este caso la relacion dictamina que este
	 * valor no puede ser nulable asique primero deberíamos hacer el persist de un
	 * {@link Alquimista} y luego persistir el objeto {@link Pocion} habiendo
	 * previamente hecho el {@link Pocion#setAlquimista(Alquimista)}
	 */
	@Override
	public Pocion getInstanceDeE() {
		Pocion pocion = new Pocion();
		pocion.setNombre("Antídoto");

		Alquimista alquimista = new Alquimista();
		alquimista.setNombre("MaQuiNa1995");
		entityManager.persist(alquimista);

		pocion.setAlquimista(alquimista);

		return pocion;
	}

	@Override
	public Long getClavePrimariaNoExistente() {
		return Long.MAX_VALUE;
	}

	@Override
	protected Pocion getInstanceDeTParaModificar(Long id) {
		Pocion personaje = super.getInstanceDeTParaModificar(id);

		personaje.getAlquimista()
				.setNombre("MaKy1995");

		return personaje;
	}

}
