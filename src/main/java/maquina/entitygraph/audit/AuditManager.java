package maquina.entitygraph.audit;

import org.hibernate.Session;
import org.hibernate.tuple.ValueGenerator;

/**
 * Implementación muy básica del logging de entidades para saber quien modificó
 * o creó una entidad esto se suele usar con spring security pero de momento lo
 * dejo asi para una prueba de concepto
 * <p>
 * Siempre nos va a devolver el valor "MaQuiNa1995"
 * 
 * @author MaQuiNa1995
 *
 */
public class AuditManager implements ValueGenerator<String> {

	@Override
	public String generateValue(Session session, Object owner) {
		return "MaQuiNa1995";
	}

}