package maquina.entitygraph.dominio;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase que extiende de {@link AbstractEntidadSimple} para obtener sus
 * atributos si queremos sobreescribir el nombre de algun campo de esta clase
 * debemos usar el {@link AttributeOverride}
 * 
 * @author MaQuiNa1995
 *
 */
@ToString(callSuper = true)
@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AttributeOverride(name = "id", column = @Column(name = "ID_POCION"))
public class Pocion extends AbstractEntidadSimple<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1201699757021907081L;

	/**
	 * Parte Dominante
	 * <p>
	 * name = nombre de la columna en base de datos que hace referencia a
	 * {@link Alquimista}
	 * <p>
	 * Por defecto es {@link FetchType#EAGER}
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_ALQUIMISTA")
	private Alquimista alquimista;

	@Override
	public String toString() {
		return "Pocion [alquimista=" + alquimista + ", nombre=" + nombre + "]";
	}

}
