package br.com.sistemas.chamados.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemas.chamados.entity.Comentarios;

public interface ComentarioRepository extends JpaRepository<Comentarios, Long> {
    List<Comentarios> findByChamadoIdOrderByDataAsc(Long chamadoId);
}