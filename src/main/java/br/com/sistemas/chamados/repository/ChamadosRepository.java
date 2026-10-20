package br.com.sistemas.chamados.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemas.chamados.entity.Chamados;

public interface ChamadosRepository extends JpaRepository<Chamados, Long> {
    boolean existsByTitulo(String titulo);
    boolean existsByTituloAndIdNot(String titulo, Long id);
}
