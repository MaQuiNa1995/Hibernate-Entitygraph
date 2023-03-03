package maquina.entitygraph.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import maquina.entitygraph.dominio.Pocion;

public interface PocionRepository extends JpaRepository<Pocion, Long> {

}