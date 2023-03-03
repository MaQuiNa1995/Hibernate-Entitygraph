package maquina.entitygraph.repository;

import maquina.entitygraph.dominio.Alquimista;
import maquina.entitygraph.dominio.Pocion;

public class AlquimistaRepositoryTest extends JpaRepositoryImplTest<Long, Alquimista> {

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
	public Alquimista getInstanceDeE() {
		Alquimista alquimista = new Alquimista();
		alquimista.setNombre("MaquiNa1995");
		return alquimista;
	}

	@Override
	public Long getClavePrimariaNoExistente() {
		return Long.MAX_VALUE;
	}

}
