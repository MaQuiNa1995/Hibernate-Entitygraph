package maquina.entitygraph.dominio;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import maquina.entitygraph.audit.AuditManager;

/**
 * Clase creada para que las tablas que queramos auditar puedan extender de ella
 * y guarde información útil sobre ellas, solo serviría para acciones que no
 * sean borrados físicos , ya que con estos se borra toda la entidad de base de
 * datos para llevar un historico completo debemos usar otra manera:
 * <a href="https://www.baeldung.com/database-auditing-jpa">como esta</a>
 * <p>
 * <a href=
 * "https://vladmihalcea.com/how-to-emulate-createdby-and-lastmodifiedby-from-spring-data-using-the-generatortype-hibernate-annotation/">Más
 * info aquí</a>
 * 
 * @author MaQuiNa1995
 *
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractAuditable implements Serializable {

	/**
	 * Marcamos la columna para que cada vez que se haga
	 * {@link GenerationTime#INSERT} vaya a {@link AuditManager} y meta el usuario
	 * que ha tocado la entidad
	 */
	@GeneratorType(type = AuditManager.class, when = GenerationTime.INSERT)
	protected String creadoPor;

	/**
	 * Marcamos la columna para que salga el log de la fecha y hora en la que se
	 * creó la entidad esta anotación soporta múltiples tipos de fecha
	 * <p>
	 * <a href=
	 * "https://stackoverflow.com/questions/221611/creation-timestamp-and-last-update-timestamp-with-hibernate-and-mysql#221827">Mas
	 * Info</a>
	 */
	@CreationTimestamp
	protected LocalDateTime fechaCreacion;

	/**
	 * Marcamos la columna para que cada vez que se haga cualquier operación
	 * {@link GenerationTime#ALWAYS} vaya a {@link AuditManager} y meta el usuario
	 * que ha tocado la entidad
	 * 
	 */
	@GeneratorType(type = AuditManager.class, when = GenerationTime.ALWAYS)
	protected String modificadoPor;

	/**
	 * Marcamos la columna para que salga el log de la fecha y hora en la que se
	 * modificó la entidad esta anotación soporta múltiples tipos de fecha
	 * <p>
	 * <a href=
	 * "https://stackoverflow.com/questions/221611/creation-timestamp-and-last-update-timestamp-with-hibernate-and-mysql#221827">Mas
	 * Info</a>
	 */
	@UpdateTimestamp
	protected LocalDateTime fechaModificacion;
}
