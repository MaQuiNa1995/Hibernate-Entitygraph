package maquina.entitygraph.configuration;

import java.util.Locale;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

/**
 * Clase usada para no tener que definir el nombre de las columnas en hibernate,
 * transforma los nombres a nuestro antojo
 * <p>
 * En este caso transforma los nombres a mayúscula y los pone snakeCase
 * <p>
 * <a href="https://www.baeldung.com/hibernate-naming-strategy">Mas Info
 * Aqui</a>
 * 
 * @author MaQuiNa1995
 *
 */
@Component
public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy {

	@Override
	public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnv) {
		return this.convertToSnakeCase(identifier);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnv) {
		return this.convertToSnakeCase(identifier);
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnv) {
		return this.convertToSnakeCase(identifier);
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnv) {
		return this.convertToSnakeCase(identifier);
	}

	@Override
	public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnv) {
		return this.convertToSnakeCase(identifier);
	}

	private Identifier convertToSnakeCase(Identifier identifierFromHibernate) {

		Identifier identifier = null;

		// Se hace la comprobación de nula porque al no tener definido el schema por
		// ejemplo viene null
		if (identifierFromHibernate != null) {
			String newName = identifierFromHibernate.getText()
					.replaceAll("([a-z])([A-Z])", "$1_$2")
					.toUpperCase(Locale.ROOT);

			identifier = Identifier.toIdentifier(newName);
		}
		return identifier;
	}
}
