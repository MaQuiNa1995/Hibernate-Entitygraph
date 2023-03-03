package maquina.entitygraph.dominio;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase que extiende de {@link AbstractEntidadSimple} para obtener sus
 * atributos si queremos sobreescribir el nombre de algun campo de esta clase
 * debemos usar el {@link AttributeOverride}
 * 
 * @author MaQuiNa1995
 */
@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AttributeOverride(name = "id", column = @Column(name = "ID_ALQUIMISTA"))
public class Alquimista extends AbstractEntidadSimple<Long> {

	/** 
	 *  
	 */
	private static final long serialVersionUID = -5371981248296997762L;

	/**
	 * Parte esclava
	 * <p>
	 * mappedBy = nombre del campo en {@link Pocion} que referencia a
	 * {@link Alquimista}
	 * <p>
	 * cascade = {@link CascadeType#ALL} se debe hacer cascade all para que de una
	 * manera sencilla se mantenga la integridad de la base de datos
	 * <p>
	 * orphanRemoval = se debe establecer a true para que las entidades que pierdan
	 * la relación (es decir se queden huérfanas) puedan ser borradas
	 * automáticamente
	 */
	@OneToMany(mappedBy = "alquimista", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pocion> pociones;

	@Override
	public String toString() {
		return "Alquimista [pociones=" + pociones + ", nombre=" + nombre + "]";
	}

}
