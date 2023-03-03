package maquina.entitygraph.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import maquina.entitygraph.dominio.AbstractEntidadSimple;

/**
 * Clase de test genérica para hacer test al vuelo de método tipicos de un CRUD
 * <p>
 * Lecciones Aprendidas:
 * <p>
 * si no pones el {@link TransactionalTestExecutionListener} spring no puede
 * crear el transactionManager en test con este error me tiré 1 tarde entera
 * <p>
 * Me daba un error de:
 * <p>
 * javax.persistence.TransactionRequiredException: No EntityManager with actual
 * transaction available for current thread - cannot reliably process 'persist'
 * call
 * <p>
 * <a href=
 * "https://github.com/MaQuiNa1995/Ejemplo-HSQL/commit/2cb42961e4834f20a3b772cd64a3cee5547f6326">Este
 * es el commit</a> donde arreglé el error
 * <p>
 * Según una regla de sonar los test de Jnit 5 deben tener el modificador
 * <code>Package</code>
 * <p>
 * Mas info de la regla: JUnit5 test classes and methods should have default
 * package visibility (java:S5786)
 * 
 * @author MaQuiNa1995
 *
 * @param <K> Genérico que representa el Id de la entidad que tiene que
 *            obligatoriamente implementar {@link Serializable}
 * @param <E> Genérico que representa un objeto que esté anotado con
 *            {@link javax.persistence.Entity}
 */
@DataJpaTest
@ComponentScan(value = "maquina.hibernate.repository")
abstract class JpaRepositoryImplTest<K extends Serializable, E extends AbstractEntidadSimple<K>> {

	@Autowired
	protected TestEntityManager entityManager;

	@Autowired
	protected JpaRepository<E, K> rut;

	protected abstract E getInstanceDeE();

	protected abstract K getClavePrimariaNoExistente();

	@Test
	@Transactional
	void addTest() {
		E instancia = this.getInstanceDeE();
		Assertions.assertNull(instancia.getId());
		instancia = rut.save(instancia);
		Assertions.assertNotNull(instancia.getId());
	}

	@Test
	@Transactional
	void readTest() {
		K clavePrimaria = this.generaDatoLectura();

		Optional<E> resultado = rut.findById(clavePrimaria);

		Assertions.assertTrue(resultado.isPresent());
		Assertions.assertEquals(this.getInstanceDeE(), resultado.get());
	}

	@Test
	@Transactional(readOnly = true)
	void readNoExisteTest() {
		K clavePrimaria = this.getClavePrimariaNoExistente();

		Assertions.assertFalse(rut.findById(clavePrimaria)
				.isPresent());
	}

	@Test
	@Transactional
	void findAllTest() {

		Assertions.assertEquals(0, rut.count());

		IntStream.range(0, 2)
				.forEach(e -> this.generaDatoLectura());

		List<E> resultado = rut.findAll();

		Assertions.assertEquals(2, resultado.size());
	}

	@Test
	@Transactional
	void updateTest() {
		K clavePrimaria = this.generaDatoLectura();
		E objetoUpdate = this.getInstanceDeTParaModificar(clavePrimaria);

		rut.save(objetoUpdate);
		E enBBDD = this.entityManager.find(this.getClassFromGeneric(), clavePrimaria);
		Assertions.assertEquals(this.getInstanceDeTParaModificar(clavePrimaria), enBBDD);
	}

	@Test
	@Transactional
	void deleteTest() {
		K clavePrimaria = this.generaDatoLectura();

		Assertions.assertNotEquals(0, rut.count());

		rut.deleteById(clavePrimaria);

		AbstractEntidadSimple<K> objetoBd = this.entityManager.find(this.getClassFromGeneric(), clavePrimaria);

		Assertions.assertNull(objetoBd);
	}

	/**
	 * Se hace {@link EntityManager#flush()} para que cree los campos de
	 * {@link maquina.hibernate.dominio.AbstractAuditable}
	 * 
	 * @return
	 */
	@Transactional
	private K generaDatoLectura() {
		E instancia = getInstanceDeE();
		return (K) this.entityManager.persistAndGetId(instancia);
	}

	protected E getInstanceDeTParaModificar(K id) {
		E objetoModificar = this.getInstanceDeE();
		objetoModificar.setId(id);
		objetoModificar.setNombre("MaKy1995");
		return objetoModificar;
	}

	private Class<E> getClassFromGeneric() {
		return (Class<E>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
	}

}
